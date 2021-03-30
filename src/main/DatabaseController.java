package main;

import java.lang.reflect.Type;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseController {
    private static final String FILENAMEGAME = "games.json";
    private RandomAccessFile fileGame, fileUser, fileMarketplace;
    private static final String FILENAMEUSER = "users.json";
    private static final String FILENAMEMARKETPLACE = "market.json";
    private Gson gson;

    public DatabaseController() {
        try {
            fileGame = new RandomAccessFile(FILENAMEGAME, "rw");
            FileLock ignoredgame = fileGame.getChannel().lock();
            fileUser = new RandomAccessFile(FILENAMEUSER, "rw");
            FileLock ignoreduser = fileUser.getChannel().lock();
            fileMarketplace = new RandomAccessFile(FILENAMEMARKETPLACE, "rw");
            FileLock ignoredmarketplace = fileMarketplace.getChannel().lock();
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeMarket(Marketplace market) throws IOException {
        appendData(fileMarketplace, gson.toJson(market));
    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            Gson user = new GsonBuilder().registerTypeAdapter(AbstractUser.class, new userSerializer())
                    .create();
            appendData(fileUser, user.toJson(userList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            appendData(fileGame, gson.toJson(gameList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendData(RandomAccessFile filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());

        filename.writeBytes(data);
        filename.close();
    }


}

class userSerializer implements JsonSerializer<AbstractUser>{
    @Override
    public JsonElement serialize(AbstractUser usr, Type type, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        object.addProperty("username", usr.username);
        object.addProperty("type", usr.type);
        object.addProperty("accountBalance", usr.accountBalance);
        ArrayList<Integer> inv = new ArrayList<Integer>();
        for (Game game: usr.inventory
        ) {
            inv.add(game.getUniqueID());
        }
        object.addProperty("inventory", String.valueOf(inv));
        object.addProperty("transactionHistory", String.valueOf(usr.transactionHistory));
        return object;
    }
}