package main;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

//https://howtodoinjava.com/gson/custom-serialization-deserialization/

public class DeserializeGame implements JsonDeserializer<Game> {
    private final String name = "name";
    private final String price = "price";
    private final String seller = "seller";
    private final String gameID = "gameID";
    private final String discount = "discount";

    /**
     *
     * @param name name of game
     * @param price price of game
     * @param seller game seller
     * @param gameID unique game id of game
     * @param discount discount applied to game
     * @return Game object with given parameters
     */
    private static Game makeGame(String name, Float price, String seller, Integer gameID, Float discount) {

        if (name.length() > 25) { //check name
            return null;
        } else if (price > 999.99) {
            return null;
        } else if (seller.length() > 15) {
            return null;
        } else if (discount > 90.00) {
            return null;
        }
        return new Game(name, price, seller, gameID, discount);
    }

    // https://stackoverflow.com/questions/15920212/how-to-check-the-type-of-a-value-from-a-jsonobject/15920281
    public Boolean correctTypes(JsonObject jsonObject) {
        if (!(jsonObject.has(name) && jsonObject.has(price) && jsonObject.has(seller) && jsonObject.has(gameID) &&
                jsonObject.has(discount))) {
            return false;
        }

        try {
            jsonObject.get(name).getAsString();
            jsonObject.get(price).getAsFloat();
            jsonObject.get(seller).getAsString();
            jsonObject.get(gameID).getAsInt();
            jsonObject.get(discount).getAsFloat();

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
        Float priceGame = jsonObject.get(price).getAsFloat();
        String sellerGame = jsonObject.get(seller).getAsString();
        Integer gameIDGame = jsonObject.get(gameID).getAsInt();
        Float discountGame = jsonObject.get(discount).getAsFloat();

        return makeGame(nameGame, priceGame, sellerGame, gameIDGame, discountGame);
    }
}