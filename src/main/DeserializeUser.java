package main;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//https://howtodoinjava.com/gson/custom-serialization-deserialization/

public class DeserializeUser implements JsonDeserializer<AbstractUser> {
    public HashMap<Integer, Game> gameIDs;
    private static final String name = "name";
    private static final String type = "type";
    private static final String credit = "credit";
    private static final String inventory = "inventory";
    private static final String transactionHistory = "transaction history";
    private static final String buyType = "BS";
    private static final String sellType = "SS";
    private static final String adminType = "AA";
    private static final String fullType = "FS";

    public Boolean correctUserTypes(JsonObject jsonObject) {
        if (!(jsonObject.has(name) && jsonObject.has(type) && jsonObject.has(credit) &&
                jsonObject.has(transactionHistory))) {
            return false;
        }
        try {
            jsonObject.get(name).getAsString();
            String typeUser = jsonObject.get(type).getAsString();
            jsonObject.get(credit).getAsDouble();
            if (typeUser.equals(sellType) && jsonObject.has(inventory)) {
                return false;
            }
            if (jsonObject.has(inventory)) { //if its a diff type other than seller, itll have inventory
                JsonArray inventoryGame = jsonObject.get(inventory).getAsJsonArray();
                removeNonIntegers(inventoryGame);
            }

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
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // helper for check types

    /** Remove items from array that are not integer types.
     *
     * @param array to be removed of integers
     */
    private void removeNonIntegers(JsonArray array) {
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()) {
            JsonElement u = iterator.next();
            try {
                u.getAsInt();
            } catch (Exception e) {
                iterator.remove();
            }
        }
    }

    private static boolean userVerifier(String name, String type, Double credit) {

        if (name.length() > 15) { //check name
            return false;
        } else if (!((type.equals(adminType)) || (type.equals(buyType)) || (type.equals(sellType)) ||
                (type.equals(fullType)))) {
            return false;
        } else return !(credit > 999999.99);
//        } else return !type.equals(sellType) || inventory.size() == 0;
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

    private static AbstractUser makeBasicUser(String name, String type, Double credit, ArrayList<Game> inventory,
                                    ArrayList<String> history) {


        if (type.equals(buyType)) {
            return new BuyUserBuilder(name).credit(credit).inventoryGames(inventory).transactionHistory(history);
        } else if (type.equals(adminType)) {
            return new AdminUserBuilder(name).credit(credit).inventoryGames(inventory).transactionHistory(history);
        } else if (type.equals(fullType)) {
            return new FullUserBuilder(name).credit(credit).inventoryGames(inventory).transactionHistory(history);
        }
        return null;
    }

    private static AbstractUser makeSellUser(String name, String type, Double credit, ArrayList<String> history) {
        return new SellUserBuilder(name).credit(credit).transactionHistory(history);
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

        JsonArray transactionHistoryUser = jsonObject.get(transactionHistory).getAsJsonArray();

        if (!userVerifier(nameUser, typeUser, creditUser)) {
            return null;
        }

        ArrayList<String> transactionHistoryList = new ArrayList<String>();

        for (JsonElement i : transactionHistoryUser) {
            transactionHistoryList.add(i.getAsString());
        }

        if (jsonObject.has(inventory)) { //any other than sell
            JsonArray inventoryUser = jsonObject.get(inventory).getAsJsonArray();
            ArrayList<Game> inventoryGameObjects = gameIDsToGames(inventoryUser);
            makeBasicUser(nameUser, typeUser, creditUser, inventoryGameObjects, transactionHistoryList)
        }

        return makeSellUser(nameUser, typeUser, creditUser, transactionHistoryList);
    }
}
