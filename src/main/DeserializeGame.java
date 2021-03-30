package main;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
//EDIT THIS WITH NEW IMPLEMENTATION OF GAME
//https://howtodoinjava.com/gson/custom-serialization-deserialization/

public class DeserializeGame implements JsonDeserializer<Game> {
    private final String name = "name";
    private final String price = "price";
    private final String seller = "seller";
    private final String gameID = "gameID";
    private final String discount = "discount";
    private final String onHold = "onHold";

    /**
     *
     * @param name name of game
     * @param price price of game
     * @param seller game seller
     * @param gameID unique game id of game
     * @param discount discount applied to game
     * @return Game object with given parameters
     */
    private static Game makeGame(String name, Double price, String seller, Integer gameID, Double discount) {

        if (name.length() > 25) { //check name
            return null;
        } else if (price > 999.99) {
            return null;
        } else if (seller.length() > 15) {
            return null;
        } else if (discount > 90.00) {
            return null;
        }
        Game game = new Game(name, price, seller, gameID, discount);
        game.changeHold();
        return game;
    }

    // https://stackoverflow.com/questions/15920212/how-to-check-the-type-of-a-value-from-a-jsonobject/15920281
    public Boolean correctTypes(JsonObject jsonObject) {
        if (!(jsonObject.has(name) && jsonObject.has(price) && jsonObject.has(seller) && jsonObject.has(gameID) &&
                jsonObject.has(discount) && jsonObject.has(onHold))) {
            return false;
        }

        try {
            jsonObject.get(name).getAsString();
            jsonObject.get(price).getAsDouble();
            jsonObject.get(seller).getAsString();
            jsonObject.get(gameID).getAsInt();
            jsonObject.get(discount).getAsDouble();
            jsonObject.get(onHold).getAsBoolean();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Game deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        if (!correctTypes(jsonObject)) { //check that all values exist and are of correct type
            return null;
        }

        String nameGame = jsonObject.get(name).getAsString();
        double priceGame = jsonObject.get(price).getAsDouble();
        String sellerGame = jsonObject.get(seller).getAsString();
        Integer gameIDGame = jsonObject.get(gameID).getAsInt();
        Double discountGame = jsonObject.get(discount).getAsDouble();

        return makeGame(nameGame, priceGame, sellerGame, gameIDGame, discountGame);
    }
}