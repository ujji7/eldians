package main;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private static final String id = "game";
    private static final String credit = "accountBalance";
    private static final String inventory = "inventory";
    private static final String transactionHistory = "transactionHistory";
    private static final String buyType = "BS";
    private static final String sellType = "SS";
    private static final String adminType = "AA";
    private static final String fullType = "FS";

    public void setGameIDs(HashMap<Integer, Game> ids) {
        this.gameIDs = ids;
    }

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
    

    // https://www.programcreek.com/java-api-examples/?class=org.json.JSONArray&method=forEach
    protected JsonArray getIntegersFromObject(JsonArray array) {
        JsonArray jsonObjects = new JsonArray();
        for (JsonElement jsonArrayObject : array) {
            if (jsonArrayObject instanceof JsonObject) {
                try {
                    Integer uniqueID = ((JsonObject) jsonArrayObject).get(id).getAsInt();
                    jsonObjects.add(uniqueID);
                } catch (Exception e) {
//                    array.remove(jsonArrayObject);
                }
            }
        }
        return jsonObjects;
    }

    private static boolean userVerifier(String name, String type, Double credit, JsonArray inventory) {

        if (name.length() > 15) { //check name
            return false;
        } else if (!((type.equals(adminType)) || (type.equals(buyType)) || (type.equals(sellType)) ||
                (type.equals(fullType)))) {
            return false;
        } else if (credit > 999999.99) {
            return false;
        } else return !type.equals(sellType) || inventory.size() == 0;
    }


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

    private static AbstractUser makeUser(String name, String type, Double credit, ArrayList<Game> inventory,
                                    ArrayList<String> history) {

        switch (type) {
            case buyType: {
                BuyUser.UserBuilder builder = new BuyUser.UserBuilder(name);
                builder.balance(credit);
                builder.inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
            case sellType: {
                SellUser.UserBuilder builder = new SellUser.UserBuilder(name);
                builder.balance(credit);
                builder.transactionHistory(history);
                return builder.build();
            }
            case adminType: {
                AdminUser.UserBuilder builder = new AdminUser.UserBuilder(name);
                builder.balance(credit);
                builder.inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
            case fullType: {
                FullStandardUser.UserBuilder builder = new FullStandardUser.UserBuilder(name);
                builder.balance(credit);
                builder.inventoryGames(inventory);
                builder.transactionHistory(history);
                return builder.build();
            }
        }
        return null;
    }

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
