package main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.Set;


import com.google.gson.*;

//https://futurestud.io/tutorials/gson-advanced-custom-serialization-part-1
//https://attacomsian.com/blog/gson-write-json-file
//https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
//https://stackabuse.com/reading-and-writing-json-in-java/
//https://www.tutorialspoint.com/gson/gson_quick_guide.htm
//https://www.tutorialspoint.com/gson/gson_inner_classes.htm


/**
 * A Database Serializer that serializes Application's Gamelist,
 * Userlist and marketplace into JSON
 */
public class DatabaseController {
    private static final String FILENAMEGAME = "games.json";
    private FileWriter fileGame, fileUser, fileMarketplace;
    private static final String FILENAMEUSER = "users.json";
    private static final String FILENAMEMARKETPLACE = "market.json";
    private Gson gson;

    /**
     * Database Controller constructor
     * creates new files with appropriate filenames
     */
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

    /**
     * Serializes the marketplace into JSON and writes to marketplace file
     * @param market Application'c current marketplace with sellers, games, and auctionsale toggle
     * @throws IOException
     */
    public void writeMarket(Marketplace market) throws IOException {
        flush(fileMarketplace);
        GsonBuilder invBuilder = new GsonBuilder();
        invBuilder.registerTypeAdapter(Game.class, new gameSerializer()).setPrettyPrinting();
        Gson user = invBuilder.create();
        writeData(fileMarketplace, user.toJson(market));
        close(fileMarketplace);
    }

    /**
     * Serializes the Userlist into JSON then writes to the users file
     * @param userList a list of Application's users
     * @throws IOException
     */
    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            flush(fileUser);
            GsonBuilder invBuilder = new GsonBuilder();
            invBuilder.registerTypeAdapter(Game.class, new gameSerializer()).setPrettyPrinting();
            Gson userSer = invBuilder.create();
            writeData(fileUser, userSer.toJson(userList));
            close(fileUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializaes the Gamelist into JSON then writes to games file
     * @param gameList Application's list of Games
     * @throws IOException
     */
    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            flush(fileGame);
            writeData(fileGame, gson.toJson(gameList));
            close(fileGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Flushes the contents of the given file
     * @param file a file to be emptied
     * @throws IOException
     */
    private void flush(FileWriter file) throws IOException {
        file.flush();
    }

    /**
     * Closes the given file
     * @param file a file to be closed
     * @throws IOException
     */
    private void close(FileWriter file) throws IOException {
        file.close();
    }

    /**
     * Writes the given data to the given file
     * @param filename a file to be written to
     * @param data a string of JSON to be written to the file
     * @throws IOException
     */
    private void writeData(FileWriter filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());
        filename.write(data);
    }


}

/**
 * A serializer that extends the JSON Serializer to serializes game objects into their
 * Unique IDs
 */
class gameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game game, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("uniqueID", game.getUniqueID());
        return object;
    }

}