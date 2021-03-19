package main;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DeserializeGame implements JsonDeserializer<Game> {

//https://howtodoinjava.com/gson/custom-serialization-deserialization/

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

    //        https://stackoverflow.com/questions/15920212/how-to-check-the-type-of-a-value-from-a-jsonobject/15920281
    public Boolean correctTypes(JsonObject jsonObject) {
        if (!(jsonObject.has("name") && jsonObject.has("price") &&
                jsonObject.has("seller") && jsonObject.has("gameID") &&
                jsonObject.has("discount"))) {
            return false;
        }

        JsonElement nameObj = jsonObject.get("name");

        try {
            String name = nameObj.getAsString();
            Float price = jsonObject.get("price").getAsFloat();
            String seller = jsonObject.get("seller").getAsString();
            Integer gameId = jsonObject.get("gameID").getAsInt();
            Float discount = jsonObject.get("discount").getAsFloat();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Game deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        //put all values.opt into verifier: Returns the value mapped by name, or null if no such mapping exists.


        if (!correctTypes(jsonObject)) { //check that all values exist and are of correct type
            return null;
        }

        String name = jsonObject.get("name").getAsString();
        Float price = jsonObject.get("price").getAsFloat();
        String seller = jsonObject.get("seller").getAsString();
        Integer gameID = jsonObject.get("gameID").getAsInt();
        Float discount = jsonObject.get("discount").getAsFloat();

        return makeGame(name, price, seller, gameID, discount);
    }
}