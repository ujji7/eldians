package main;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
/** A ReadingJSON class that Reads the database files and creates the necessary objects to be used by the system.
 * There are 3 files to be read, one for games, one for users, and one for the marketplace.
 * This was adapted from a website post written by Eugen Paraschiv on October 24, 2019 here:
 * https://www.baeldung.com/gson-deserialization-guide
 * File opener methods were adapted from practical notes.
 *
 */
public class ReadingJSON {
    private static File Game, User, Market;
    private static final String fileNameGame = "gamec.json";
    private static final String fileNameUser = "userc.json";
    private static final String fileNameMarket = "marketc.json";

    private static void individualFileOpener(File file, String name) {
        if (!(file = new File(name)).exists()) { //if game file does not exist, we can assume all 3 ones exist
            try {
                file.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }
    }

    private static void filesOpener() {
        individualFileOpener(Game, fileNameGame);
        individualFileOpener(User, fileNameUser);
        individualFileOpener(Market, fileNameMarket);
    }


    // Code adapted from MSDN example: HOW TO CITE A FUNCTION
    // http://msdn.microsoft.com/en-us/library/ms680578(VS.85).aspx
    public static List<Game> readGamesFile() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameGame)); // create a reader to read the games file
            System.out.println("game file is found.");
            Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new DeserializeGame()).create(); //create Gson
            // object to build the List of games
            List<Game> games = gson.fromJson(reader, new TypeToken<List<Game>>() {}.getType()); //Return list of games
            // according to game deserializer
            games.removeIf(Objects::isNull);

            ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();
            for (Iterator<Game> it = games.iterator(); it.hasNext(); ) { //check that there are no duplicate gameIDs
                Game g = it.next();
                if (!(uniqueIDs.contains(g.getUniqueID()))) {
                    uniqueIDs.add(g.getUniqueID());
                } else {
                    it.remove();
                }
            }
            reader.close();
            return games;
        } catch (IOException fileNotFoundException) {
            System.out.println("Games file not found.");
        } catch (JsonSyntaxException e){
            System.out.println("Games file not in correct format.");
        }
        return new ArrayList<>();
    }

    private static HashMap<Integer, Game> setGamesList(List<Game> gamesList) {
        HashMap<Integer, Game> gameIDAll = new HashMap<Integer, Game>();
        if (gamesList != null) {
            for (Game g : gamesList) {
                gameIDAll.put(g.getUniqueID(), g);
            }
        }
        return gameIDAll;
    }

    private static void removeDuplicateSellers(List<AbstractUser> users) {
        ArrayList<String> uniqueSellers = new ArrayList<String>();

        for (Iterator<AbstractUser> it = users.iterator(); it.hasNext(); ) { //checl for duplictae seller Ids
            AbstractUser u = it.next();
            if (!(uniqueSellers.contains(u.getUsername()))) {
                uniqueSellers.add(u.getUsername());
            } else {
                it.remove();
            }
        }
    }

    public static List<AbstractUser> readUsersFile(List<Game> gamesList) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameUser));  // create a reader
            GsonBuilder gsonBuilder = new GsonBuilder();
            DeserializeUser deserializer = new DeserializeUser();
            gsonBuilder.registerTypeAdapter(AbstractUser.class, deserializer);

            deserializer.gameIDs = setGamesList(gamesList); // create the games parameter in deserializer
            Gson gson = gsonBuilder.create();

            List<AbstractUser> users = gson.fromJson(reader, new TypeToken<List<AbstractUser>>() {}.getType());

            users.removeIf(Objects::isNull); //if any user is null (aka an error), remove it

            removeDuplicateSellers(users); // remove duplicate sellers

            reader.close(); // close reader
            return users;

        } catch (IOException fileNotFoundException) {
            System.out.println("Users file not found.");
        } catch (JsonSyntaxException e){
            System.out.println("Users file not in proper format.");
        }
        return new ArrayList<>();
    }

    private static HashMap<String, AbstractUser> getUserHashmap(List<AbstractUser> listUsers) {
        HashMap<String, AbstractUser> userIDs = new HashMap<String, AbstractUser>();
        if (listUsers != null) {
            for (AbstractUser a : listUsers) {
                userIDs.put(a.getUsername(), a);
            }
        }
        return userIDs;
    }

    public static Marketplace readMarketFile(List<Game> listGames, List<AbstractUser> listUsers) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameMarket));  // create a reader
            GsonBuilder gsonBuilder = new GsonBuilder();
            DeserializeMarketplace deserializer = new DeserializeMarketplace();
            gsonBuilder.registerTypeAdapter(Marketplace.class, deserializer);

            deserializer.gameIDs = setGamesList(listGames);
            deserializer.users = getUserHashmap(listUsers);

            Gson gson = gsonBuilder.create();

            Marketplace market = gson.fromJson(reader, Marketplace.class);
            reader.close();
            return market;

        } catch (IOException fileNotFoundException) {
            System.out.println("Market file not found.");
        }
        catch (JsonSyntaxException e){
            System.out.println("Market file not in proper format.");
        }
        return new Marketplace(false, new HashMap<String,ArrayList<Game>>());
    }

//    public static void main (String[]args){
//        filesOpener();
//        List<Game> games = readGamesFile();
//        System.out.println("games are: " + games);
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//
//        List<AbstractUser> users = readUsersFile(games);
//        System.out.println("users are: " + users);
//
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//        Marketplace market = readMarketFile(games, users);
//        System.out.println("market is: " + market);
//    }
}

