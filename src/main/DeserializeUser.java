package main;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** A DeserializeUser class that given a json file of User objects, creates them. It ensures that improper/incomplete
 * formatting and input of users are not created.
 * This was adapted from a website post written by Lokesh Gupta here:
 * https://howtodoinjava.com/gson/custom-serialization-deserialization/
 *
 */
public class DeserializeUser implements JsonDeserializer<AbstractUser> {
    private HashMap<Integer, Game> gameIDs;
    private static final String name = "username";
    private static final String type = "type";
    private static final String id = "uniqueID";
    private static final String credit = "accountBalance";
    private static final String inventory = "inventory";
    private static final String transactionHistory = "transactionHistory";
    private static final String buyType = "BS";
    private static final String sellType = "SS";
    private static final String adminType = "AA";
    private static final String fullType = "FS";
    private static final double MAX_CREDIT = 999999.99;
    private static final int MAX_USERNAME_LENGTH = 15;

    /** Setter method for the game IDs attribute.
     * 
     * @param ids the game ids to be set
     */
    // Code adapted from Stack Overflow post: How to copy HashMap (not shallow copy) in Java:
    // https://stackoverflow.com/questions/28288546/how-to-copy-hashmap-not-shallow-copy-in-java/28288729
    public void setGameIDs(HashMap<Integer, Game> ids) {
        HashMap<Integer, Game> copy = new HashMap<Integer, Game>();
        for (Map.Entry<Integer, Game> entry : ids.entrySet())
        {
            copy.put(entry.getKey(),entry.getValue());
        }
        this.gameIDs = copy;
    }

    /** Return true if the JsonObject User has all necessary requirements (name, type, credit, inventory, & transaction 
     * history) and all attributes match the required types.
     * 
     * @param jsonObject Json object with the given attributes
     * @return true if the Json Object can be turned into a user correctly, false otherwise.
     */
    public Boolean correctUserTypes(JsonObject jsonObject) {

        if (!(jsonObject.has(name) && jsonObject.has(type) && jsonObject.has(credit) && jsonObject.has(inventory) &&
                jsonObject.has(transactionHistory))) {
            return false;
        }

        try {
            jsonObject.get(name).getAsString();
            jsonObject.get(type).getAsString();
            jsonObject.get(credit).getAsDouble();
            jsonObject.get(inventory).getAsJsonArray();

            JsonArray transactionArray = jsonObject.get(transactionHistory).getAsJsonArray();

            Iterator<JsonElement> iteratorT = transactionArray.iterator();
            while (iteratorT.hasNext()) {
                JsonElement t = iteratorT.next();
                try {
                    t.getAsString();
                } catch (Exception e) {
                    iteratorT.remove();
                }
            }

        } catch(Exception e){
            return false;
        }
        return true;
    }
    

    // Code adapted from Program Creek post: How to iterate through Json Array of Json Objects:
    // https://www.programcreek.com/java-api-examples/?class=org.json.JSONArray&method=forEach
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
                } catch (Exception e) {
                }
            }
        }
        return jsonObjects;
    }

    /** Return true if the attributes of the user match the requirements specified.
     * 
     * @param name name of the user
     * @param type of the user
     * @param credit of the user
     * @param inventory of the user
     * @return True if the attributes of the user match the requirements
     */
    private static boolean userVerifier(String name, String type, Double credit, JsonArray inventory) {

        if (name.length() > MAX_USERNAME_LENGTH) { //check name
            return false;
        } else if (!((type.equals(adminType)) || (type.equals(buyType)) || (type.equals(sellType)) ||
                (type.equals(fullType)))) {
            return false;
        } else if (credit > MAX_CREDIT || credit < 0) {
            return false;
        } else return !type.equals(sellType) || inventory.size() == 0;
    }


    /** Return a list of Game objects made from the unique ids found in inventory
     * 
     * @param inventory Json array of unique ids
     * @return ArrayList of Game objects corresponding to the unique ids found in inventory
     */
    public ArrayList<Game> gameIDsToGames(JsonArray inventory) {
        ArrayList<Game> inventoryGames = new ArrayList<Game>();

        for (JsonElement i : inventory) {
            int uniqueID = i.getAsInt();
            if (gameIDs.get(uniqueID) != null) {
                inventoryGames.add(gameIDs.get(uniqueID));
            }
        }
        return inventoryGames;
    }

    /** Make and return the user with the given parameters
     * 
     * @param name name of the user 
     * @param type user type
     * @param credit credit of the user
     * @param inventory inventory of the user, empty if its a seller type
     * @param history user's transaction history
     * @return User with the given attributes
     */
    private static AbstractUser makeUser(String name, String type, Double credit, ArrayList<Game> inventory,
                                    ArrayList<String> history) {
        switch (type) {
            case buyType: {
                BuyUser.UserBuilder builder = new BuyUser.UserBuilder(name).balance(credit).inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
            case sellType: {
                SellUser.UserBuilder builder = new SellUser.UserBuilder(name).balance(credit);
                builder.transactionHistory(history);
                return builder.build();
            }
            case adminType: {
                AdminUser.UserBuilder builder = new AdminUser.UserBuilder(name).balance(credit);
                builder.inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
            case fullType: {
                FullStandardUser.UserBuilder builder = new FullStandardUser.UserBuilder(name).balance(credit);
                builder.inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
        }
        return null;
    }


    /** Return a User object using the json element or null if a User cannot be created using the attributes from the 
     * passed in element.
     *
     * @param json the json element to be turned into a User object, if applicable
     * @param typeOfT The type of object to turn the json element into (Abstract user object)
     * @param context the context for deserializing to be used in this custom deserializer
     * @return A User object using the json element or null if a User cannot be created.
     * @throws JsonParseException if there is an error in the file reading
     */
    @Override
    public AbstractUser deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!correctUserTypes(jsonObject)) { //check that all values exist and are of correct type
            return null;
        }

        String nameUser = jsonObject.get(name).getAsString();
        String typeUser = jsonObject.get(type).getAsString();
        double creditUser = jsonObject.get(credit).getAsDouble();
        JsonArray inventoryUser = jsonObject.get(inventory).getAsJsonArray();
        JsonArray transactionHistoryUser = jsonObject.get(transactionHistory).getAsJsonArray();
        ArrayList<String> transactionHistoryList = new ArrayList<String>();

        if (!userVerifier(nameUser, typeUser, creditUser, inventoryUser)) {
            return null;
        }

        JsonArray listInventoryIDs = getIntegersFromObject(inventoryUser);

        ArrayList<Game> inventoryGameObjects = gameIDsToGames(listInventoryIDs);

        for (JsonElement i : transactionHistoryUser) {
            transactionHistoryList.add(i.getAsString());
        }

        return makeUser(nameUser, typeUser, creditUser, inventoryGameObjects, transactionHistoryList);
    }
}
