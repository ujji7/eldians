//package main;
import com.google.gson.*;
//import Game;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DeserializeUser implements JsonDeserializer<AbstractUser> {
//    public static List<Game> games;
    public HashMap<Integer, Game> gameIDs;
//    public List<Game> games;

//https://howtodoinjava.com/gson/custom-serialization-deserialization/



    //        https://stackoverflow.com/questions/15920212/how-to-check-the-type-of-a-value-from-a-jsonobject/15920281
    public Boolean correctUserTypes(JsonObject jsonObject) {
        if (!(jsonObject.has("name") && jsonObject.has("type") &&
                jsonObject.has("credit") && jsonObject.has("inventory") &&
                jsonObject.has("transaction history"))) {
            return false;
        }

        try {
            String name = jsonObject.get("name").getAsString();
            String type = jsonObject.get("type").getAsString();
            Double credit = jsonObject.get("credit").getAsDouble();
            JsonArray inventory = jsonObject.get("inventory").getAsJsonArray();

            Iterator<JsonElement> iterator = inventory.iterator();
            while (iterator.hasNext()) {
                JsonElement u = iterator.next();
                try {
                    u.getAsInt();
                } catch (Exception e) {
                    iterator.remove();
                }
            }

            JsonArray transactionHistory = jsonObject.get("transaction history").getAsJsonArray();

            Iterator<JsonElement> iteratorT = transactionHistory.iterator();
            while (iteratorT.hasNext()) {
                JsonElement t = iteratorT.next();
                try {
                    t.getAsString();
                } catch (Exception e) {
                    iteratorT.remove();
                }
            }

            } catch(Exception e){
            System.out.println("wrong format or no key");
                return false;
            }
            return true;
        }

    private static boolean userVerifier(String name, String type, Double credit, JsonArray inventory) {

        if (name.length() > 15) { //check name
            System.out.println("name" + name);
            return false;
        } else if (!((type.equals("AA")) || (type.equals("BS")) || (type.equals("SS")) || (type.equals("FS")))) {
            System.out.println("unknown type" + name);
            return false;
        } else if (credit > 999999.99) {
            System.out.println("credit tooo ghih" + name + "credit: " + credit);
            return false;
        } else if (type.equals("SS") && inventory.size() != 0) {
            System.out.println("seller has inventory" + name);
            return false;
        }
        System.out.println("user verifier passed by " + name);
        return true;
    }

    public void listToHashmapGames(List<Game> gamesGiven) {
        if (gamesGiven != null) {
            for (Game g : gamesGiven) {
                gameIDs.put(g.getUniqueID(), g);
            }
        }
    }


    public ArrayList<Game> gameIDToGame(JsonArray inventory) {
        ArrayList<Game> inventoryGames = new ArrayList<Game>();
//        ArrayList<Integer> gameIDs = new ArrayList<Integer>();

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


        if (type.equals("BS")) {
            return new BuyUser(name, credit, inventory, history);
        } else if (type.equals("SS")) {
            return new SellUser(name, credit, inventory, history);
        }
//        } else if (type.equals("AA")) {
//            return new AdminUser(name, credit, inventory, history);
//        } else if (type.equals("FS")) {
//            return new FullStandardUser(name, credit, inventory, history);
//        }
        return null;
    }

    @Override
    public AbstractUser deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!correctUserTypes(jsonObject)) { //check that all values exist and are of correct type
            return null;
        }

        String name = jsonObject.get("name").getAsString();
        String type = jsonObject.get("type").getAsString();
//        double credit2 = (float) jsonObject.get("credit").getAsDouble();
//        float credit23 = jsonObject.get("credit").getAsBigDecimal().floatValue();
//        System.out.println("float credit 2 is now " + credit2);
//        System.out.println("float credit 23 is now " + credit23);
        double credit = jsonObject.get("credit").getAsDouble();
        System.out.println("double credit is now: " + credit);
        JsonArray inventory = jsonObject.get("inventory").getAsJsonArray();
        JsonArray transactionHistory = jsonObject.get("transaction history").getAsJsonArray();

        if (!userVerifier(name, type, credit, inventory)) {
            return null;
        }
        ArrayList<Game> invGameObj = gameIDToGame(inventory);
        ArrayList<String> transactionHistoryString = new ArrayList<String>();

        for (JsonElement i : transactionHistory) {
            transactionHistoryString.add(i.getAsString());
        }

        return makeUser(name, type, credit, invGameObj, transactionHistoryString);
    }
}
