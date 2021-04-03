package main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.Set;


import com.google.gson.*;


public class DatabaseController {
    private static final String FILENAMEGAME = "games.json";
    private FileWriter fileGame, fileUser, fileMarketplace;
    private static final String FILENAMEUSER = "users.json";
    private static final String FILENAMEMARKETPLACE = "market.json";
    private Gson gson;

    public DatabaseController() {
        try {
            fileGame = new FileWriter(FILENAMEGAME);
            fileUser = new FileWriter(FILENAMEUSER);
            fileMarketplace = new FileWriter(FILENAMEMARKETPLACE);
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            gson = builder.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMarket(Marketplace market) throws IOException {
        flush(fileMarketplace);
        GsonBuilder invBuilder = new GsonBuilder();
        invBuilder.registerTypeAdapter(Game.class, new gameSerializer()).setPrettyPrinting();
        Gson user = invBuilder.create();
        writeData(fileMarketplace, user.toJson(market));
        close(fileMarketplace);
    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            ArrayList<String> data = new ArrayList<String>();
            flush(fileUser);
            for(AbstractUser user: userList) {
                if (user instanceof SellUser) {
                    GsonBuilder sellBuilder = new GsonBuilder();
                    sellBuilder.registerTypeAdapter(SellUser.class, new sellerSerializer()).setPrettyPrinting();
                    Gson sellSer = sellBuilder.create();
                    data.add(sellSer.toJson(user));
                } else {
                    GsonBuilder invBuilder = new GsonBuilder();
                    invBuilder.registerTypeAdapter(Game.class, new gameSerializer()).setPrettyPrinting();
                    Gson userSer = invBuilder.create();
                    data.add(userSer.toJson(user));

                }
            }
            writeData(fileUser, gson.toJson(data));
            close(fileUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            flush(fileGame);
            writeData(fileGame, gson.toJson(gameList));
            close(fileGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void flush(FileWriter file) throws IOException {
        file.flush();
    }

    private void close(FileWriter file) throws IOException {
        file.close();
    }
    private void writeData(FileWriter filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());
        filename.write(data);
    }


}

class gameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game game, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("uniqueID", game.getUniqueID());
//        object.addProperty("username", usr.username);
//        object.addProperty("type", usr.type);
//        object.addProperty("accountBalance", usr.accountBalance);
//        ArrayList<Integer> inv = new ArrayList<Integer>();
//        for (Game game: usr.inventory
//        ) {
//            inv.add(game.getUniqueID());
//        }
//        object.addProperty("inventory", String.valueOf(inv));
//        object.addProperty("transactionHistory", String.valueOf(usr.transactionHistory));
        return object;
    }
}

class sellerSerializer implements JsonSerializer<SellUser> {
    @Override
    public JsonElement serialize(SellUser seller, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("username", seller.getUsername());
        object.addProperty("accountBalance", seller.getAccountBalance());
        object.addProperty("type", seller.getType());
        return object;
    }
}