package main;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

public class DeserializeMarketplace implements JsonDeserializer<Marketplace> {
    public HashMap<Integer, Game> gameIDs;
    public HashMap<String, AbstractUser> users;
    private static final String auctionSale = "auctionSale";
    private static final String gamesOnSale = "gamesOnSale";


    public void correctMarketType(JsonObject jsonObject) {
        if (!(jsonObject.has(auctionSale) || jsonObject.has(gamesOnSale))){
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
    }

    private JsonObject removeDuplicateSellers(JsonObject json) {

        Set<String> sellers = json.keySet();

        JsonObject jsonNew = new JsonObject();
        for (String s : sellers) { //for each new seller
            if (!jsonNew.has(s) && this.users.containsKey(s) && s.length() <= 15) {
                JsonElement games = json.get(s);
                jsonNew.add(s, games);
            }
        }
        return jsonNew;
    }


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

    private AbstractUser findUser(String name) {
        return users.get(name);
    }

    @Override
    public Marketplace deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        correctMarketType(jsonObject);

        boolean auction = jsonObject.get(auctionSale).getAsBoolean();
        JsonObject sellersInMarket = jsonObject.get(gamesOnSale).getAsJsonObject();
        JsonObject uniqueSellers = removeDuplicateSellers(sellersInMarket); //now this has no duplicate sellers

        Set<String> sellers = uniqueSellers.keySet(); // get all sellers
        HashMap<AbstractUser, ArrayList<Game>> market = new HashMap<AbstractUser, ArrayList<Game>>();

        for (String s : sellers) {
//            AbstractUser user = findUser(s); //add this if hashmap <user obj, list.
            JsonArray gameIDList = uniqueSellers.get(s).getAsJsonArray();
            ArrayList<Game> gamesSoldByUSer = gameIDsToGames(s, gameIDList);
            market.put(s, gamesSoldByUSer);
        }

        return new Marketplace(auction, market);
    }
}
