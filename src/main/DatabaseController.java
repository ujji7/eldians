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
        GsonBuilder invBuilder = new GsonBuilder();
        invBuilder.registerTypeAdapter(Game.class, new userSerializer()).setPrettyPrinting();
        Gson user = invBuilder.create();
        writeData(fileMarketplace, user.toJson(market));

    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            GsonBuilder invBuilder = new GsonBuilder();
            invBuilder.registerTypeAdapter(Game.class, new userSerializer()).setPrettyPrinting();
            Gson user = invBuilder.create();
            writeData(fileUser, user.toJson(userList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            writeData(fileGame, gson.toJson(gameList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeData(FileWriter filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());
        filename.flush();
        filename.write(data);
        filename.close();
    }


}

class userSerializer implements JsonSerializer<Game>{
    @Override
    public JsonElement serialize(Game game, Type type, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        object.addProperty("game", game.getUniqueID());
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