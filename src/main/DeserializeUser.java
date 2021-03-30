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
        if (!(jsonObject.has(name) && jsonObject.has(type) && jsonObject.has(credit) && jsonObject.has(inventory) &&
                jsonObject.has(transactionHistory))) {
            return false;
        }

        try {
            jsonObject.get(name).getAsString();
            jsonObject.get(type).getAsString();
            jsonObject.get(credit).getAsDouble();
            JsonArray inventoryGame = jsonObject.get(inventory).getAsJsonArray();

            removeNonIntegers(inventoryGame);

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

    // helper for check types
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

        if (!userVerifier(nameUser, typeUser, creditUser, inventoryUser)) {
            return null;
        }
        ArrayList<Game> inventoryGameObjects = gameIDsToGames(inventoryUser);
        ArrayList<String> transactionHistoryList = new ArrayList<String>();

        for (JsonElement i : transactionHistoryUser) {
            transactionHistoryList.add(i.getAsString());
        }

        return makeUser(nameUser, typeUser, creditUser, inventoryGameObjects, transactionHistoryList);
    }
}
