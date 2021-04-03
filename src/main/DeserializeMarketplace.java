package main;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/** A DeserializeMarketplace class that given a json file of a Marketplace object, creates it. It ensures that
 * improper/incomplete formatting and input of games does not prevent a marketplace from being created.
 * This was adapted from a website post written by Lokesh Gupta here:
 * https://howtodoinjava.com/gson/custom-serialization-deserialization/
 *
 */

    // SET INITIALIZING MARKET TO ORIGINALLY WITH FALSE AUCTION SALE
public class DeserializeMarketplace implements JsonDeserializer<Marketplace> {
    private HashMap<Integer, Game> gameIDs;
    private HashMap<String, AbstractUser> users;
    private static final String id = "uniqueID";
    private static final String Uid = "Uid";
    private static final String auctionSale = "auctionSale";
    private static final String gamesOnSale = "gamesOnSale";
    private static final int MAX_USERNAME_LENGTH = 15;

    /** Setter method for the game IDs attribute.
     *
     * @param ids the game ids to be set
     */
    public void setGameIDs(HashMap<Integer, Game> ids) {
        HashMap<Integer, Game> copy = new HashMap<Integer, Game>();
        for (Map.Entry<Integer, Game> entry : ids.entrySet())
        {
            copy.put(entry.getKey(),entry.getValue());
        }
        this.gameIDs = copy;
    }

    /** Setter method for the game users attribute
     * 
     * @param u the users to be set
     */
    public void setUsers(HashMap<String, AbstractUser> u) {
        HashMap<String, AbstractUser> copy = new HashMap<String, AbstractUser>();
        for (Map.Entry<String, AbstractUser> entry : u.entrySet())
        {
            copy.put(entry.getKey(),entry.getValue());
        }
        this.users = copy;
    }

    /** If the given jsonObject does not have the correct attributes to be turned into a Marketplace object, (auction 
     * Sale, games on sale), or the attributes are not of the required type, set the correct attributes to create a 
     * working Marketplace.
     * 
     * @param jsonObject json Object to check for correct market attributes
     */
    public void correctMarketType(JsonObject jsonObject) {
        if (!(jsonObject.has(auctionSale) || jsonObject.has(gamesOnSale))){ //has none of them
            jsonObject.addProperty(auctionSale, false);
            JsonObject obj = new JsonObject();
            jsonObject.add(gamesOnSale, obj);
            return;
        }
        if (jsonObject.has(auctionSale)) {
            try {
                boolean auction = jsonObject.get(auctionSale).getAsBoolean();
            } catch (Exception e) {
                jsonObject.remove(auctionSale);
                jsonObject.addProperty(auctionSale, false);
            }
        } else {
            jsonObject.addProperty(auctionSale, false);
        }

        if (!jsonObject.has(gamesOnSale)) { //add in a games on sale
            JsonObject obj = new JsonObject();
            jsonObject.add(gamesOnSale, obj);

        }

        correctUidType(jsonObject);
    }


    /** If the Uid in the market does not exist, is not an integer, or is negative, set it to 0.
     * 
     * @param jsonObject Market object
     */
    private void correctUidType(JsonObject jsonObject) {
        if (jsonObject.has(Uid)) {
            try {
                int UidValue = jsonObject.get(Uid).getAsInt();
                if (UidValue < 0) {
                    jsonObject.remove(Uid);
                    jsonObject.addProperty(Uid, 0);
                }
            } catch (Exception e) {
                jsonObject.remove(Uid);
                jsonObject.addProperty(Uid, 0);
            }
        } else {
            jsonObject.addProperty(Uid, 0);
        }
    }

    /** Remove duplicate sellers from the games on sale attribute
     * 
     * @param json json object of the games on sale attribute in Marketplace
     * @return a new json object after removing any duplicate sellers, or sellers whose names do not match the 
     * requirements.
     */
    private JsonObject removeDuplicateSellers(JsonObject json) {

        Set<String> sellers = json.keySet();

        JsonObject jsonNew = new JsonObject();
        for (String s : sellers) { //for each new seller
            if (!jsonNew.has(s) && this.users.containsKey(s) && s.length() <= MAX_USERNAME_LENGTH) {
                JsonElement games = json.get(s);
                jsonNew.add(s, games);
            }
        }
        return jsonNew;
    }


    /** Return an array list of game objects, that this user is selling. Checks for constraints such as if the unique 
     * id is in the games list and the Game's seller matches the given seller name.
     * 
     * @param name of the seller
     * @param gamesSelling Json array filled with unique id's corresponding to game objects
     * @return an array list of Game objects, that this user is selling.
     */
    public ArrayList<Game> gameIDsToGames(String name, JsonArray gamesSelling) {
        ArrayList<Game> gamesOnSale = new ArrayList<Game>();

        for (JsonElement i : gamesSelling) {
            int uniqueID = i.getAsInt();
            if (gameIDs.get(uniqueID) != null && gameIDs.get(uniqueID).getSupplierID().equals(name)) {
                gamesOnSale.add(gameIDs.get(uniqueID));
            }
        }
        return gamesOnSale;
    }

    /** Return a Json array of integers from a json array of objects, where the integer is the json element.
     * @param array Json array of json objects
     * @return Return Json array of integers from the json objects
     */
    protected JsonArray getIntegersFromObject(JsonArray array) {
        JsonArray jsonObjects = new JsonArray();
        for (JsonElement jsonArrayObject : array) {
            if (jsonArrayObject instanceof JsonObject) {
                try {
                    Integer uniqueID = ((JsonObject) jsonArrayObject).get(id).getAsInt();
                    jsonObjects.add(uniqueID);
                } catch (Exception ignored) {
                }
            }
        }
        return jsonObjects;
    }

    /** Return a Marketplace object using the json element or an Empty Marketplace if a Marketplace cannot be created 
     * using the attributes from the passed in element.
     *
     * @param json the json element to be turned into a Marketplace object.
     * @param typeOfT The type of object to turn the json element into (Marketplace object)
     * @param context the context for deserializing to be used in this custom deserializer
     * @return A Marketplace object using the json element or an empty Marketplace if a Marketplace cannot be created.
     * @throws JsonParseException if there is an error in the file reading.
     */
    @Override
    public Marketplace deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        correctMarketType(jsonObject);

        boolean auction = jsonObject.get(auctionSale).getAsBoolean();
        int UID = jsonObject.get(Uid).getAsInt();
        JsonObject sellersInMarket = jsonObject.get(gamesOnSale).getAsJsonObject();
        JsonObject uniqueSellers = removeDuplicateSellers(sellersInMarket); //now this has no duplicate sellers

        Set<String> sellers = uniqueSellers.keySet(); // get all sellers
        HashMap<String, ArrayList<Game>> market = new HashMap<String, ArrayList<Game>>();
        
        for (String s : sellers) {
//            AbstractUser user = findUser(s); //add this if hashmap <user obj, list.
            JsonArray gameIDList = uniqueSellers.get(s).getAsJsonArray();
//            System.out.println("gameIDList" + gameIDList);
            JsonArray listOfIntegers = getIntegersFromObject(gameIDList);
//            System.out.println("list of integers" + listOfIntegers);
            ArrayList<Game> gamesSoldByUSer = gameIDsToGames(s, listOfIntegers);
            System.out.println(s + gamesSoldByUSer);
            market.put(s, gamesSoldByUSer);
        }
        
        Marketplace mkt = new Marketplace(auction, market);
        mkt.setUid(UID);
        return mkt;
    }
}
