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
    private static String fileNameGame = "games.json"; //games file
    private static String fileNameUser = "users.json"; //users file
    private static String fileNameMarket = "marketjson"; //market file
    private static final String fileNotFoundError = " file not found. An empty ";
    private static final String fileFormatError =" file not in correct format. An empty ";
    private static final String fileErrorEnd = " will be created.";

    /** set the game file name to filename
     * @param filename game filename
     */
    public static void setGameFileName(String filename) {
        fileNameGame = filename;
    }
    
    /** set the user file name to filename
     * @param filename user filename
     */
    public static void setUserFileName(String filename) {
        fileNameUser = filename;
    }
    
    /** set the market file name to filename
     * @param filename market filename
     */
    public static void setMarketFileName(String filename) {
        fileNameMarket = filename;
    }
    
    /** If a file with the name does not exist, creates a file with the name
     *
     * @param file the file to create/look for
     * @param name name of file to create
     */
    private static void individualFileOpener(File file, String name) {
        if (!(file = new File(name)).exists()) { //if game file does not exist, we can assume all 3 ones exist
            try {
                file.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }
    }

    /** If the json files for games, users, and marketplace does not exist, creates them.
     */
    private static void filesOpener() {
        individualFileOpener(Game, fileNameGame);
        individualFileOpener(User, fileNameUser);
        individualFileOpener(Market, fileNameMarket);
    }


    /** Returns a list of Game objects that are created from the games file.
     *
     * @return List of game objects
     */
    public static List<Game> readGamesFile() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameGame)); // create a reader to read the games file
//            System.out.println("game file is found.");
            Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new DeserializeGame()).create(); 
            //create Gson object to build the List of games
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
            System.out.println("Games" + fileNotFoundError + "list of games" + fileErrorEnd);
        } catch (JsonSyntaxException | NullPointerException e){
            System.out.println("Games" + fileFormatError + "list of games" + fileErrorEnd);
        }
        return new ArrayList<Game>();
    }

    /** Return a hashmap of Games where the key is the unique id and value is the game object made from the gameslist
     *
     * @param gamesList list of games to be turned into a hashmap
     * @return hashmap of Games where the key is the unique id and value is the game object
     */
    private static HashMap<Integer, Game> setGamesList(List<Game> gamesList) {
        HashMap<Integer, Game> gameIDAll = new HashMap<Integer, Game>();
        if (gamesList != null) {
            for (Game g : gamesList) {
                gameIDAll.put(g.getUniqueID(), g);
            }
        }
        return gameIDAll;
    }

    /** Remove any sellers with duplicate names from the users list
     * 
     * @param users list of users to be cleaned up
     */
    private static void removeDuplicateSellers(List<AbstractUser> users) {
        ArrayList<String> uniqueSellers = new ArrayList<String>();

        for (Iterator<AbstractUser> it = users.iterator(); it.hasNext(); ) { 
            AbstractUser u = it.next();
            if (!(uniqueSellers.contains(u.getUsername()))) {
                uniqueSellers.add(u.getUsername());
            } else {
                it.remove();
            }
        }
    }

    /** Returns a list of Users that are created from the users file.
     * 
     * @param gamesList List of games in the system
     * @return list of Users
     */
    public static List<AbstractUser> readUsersFile(List<Game> gamesList) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameUser));
            GsonBuilder gsonBuilder = new GsonBuilder();
            DeserializeUser deserializer = new DeserializeUser();
            gsonBuilder.registerTypeAdapter(AbstractUser.class, deserializer);

            deserializer.setGameIDs(setGamesList(gamesList)); // create the games parameter in deserializer
            Gson gson = gsonBuilder.create();

            List<AbstractUser> users = gson.fromJson(reader, new TypeToken<List<AbstractUser>>() {}.getType());

            users.removeIf(Objects::isNull); //if any user is null (aka an error), remove it

            removeDuplicateSellers(users); // remove duplicate sellers

            reader.close();
            return users;

        } catch (IOException fileNotFoundException) {
            System.out.println("Users" + fileNotFoundError + "list of users" + fileErrorEnd);
        } catch (JsonSyntaxException | NullPointerException e){
            System.out.println("Users" + fileFormatError + "list of users" + fileErrorEnd);
        }
        return new ArrayList<AbstractUser>();
    }

    /** Return a hashmap from a List of User objects where the key-value pair is the username-User object
     * 
     * @param listUsers list of users to be turned into a hashmap
     * @return a hashmap where the key is the user name and value is user object
     */
    private static HashMap<String, AbstractUser> getUserHashmap(List<AbstractUser> listUsers) {
        HashMap<String, AbstractUser> userIDs = new HashMap<String, AbstractUser>();
        if (listUsers != null) {
            for (AbstractUser a : listUsers) {
                userIDs.put(a.getUsername(), a);
            }
        }
        return userIDs;
    }

    /** Return a Marketplace that is created from the given market file.
     * 
     * @param listGames List of games in the system
     * @param listUsers list of users in the system
     * @return a Marketplace object from the given market file.
     */
    public static Marketplace readMarketFile(List<Game> listGames, List<AbstractUser> listUsers) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameMarket));  // create a reader
            GsonBuilder gsonBuilder = new GsonBuilder();
            DeserializeMarketplace deserializer = new DeserializeMarketplace();
            gsonBuilder.registerTypeAdapter(Marketplace.class, deserializer);

            deserializer.setGameIDs(setGamesList(listGames)); // create the games parameter in deserializer
            deserializer.setUsers(getUserHashmap(listUsers));
            
            Gson gson = gsonBuilder.create();

            Marketplace market = gson.fromJson(reader, Marketplace.class);
            reader.close();
            if (market == null) {
                System.out.println("Market" + fileFormatError + "market" + fileErrorEnd);
                return new Marketplace();
            }
            return market;
        } catch (IOException fileNotFoundException) {
            System.out.println("Market" + fileNotFoundError + "market" + fileErrorEnd);
        } catch (JsonSyntaxException | NullPointerException e){
            System.out.println("Market" + fileFormatError + "market" + fileErrorEnd);
        }
        return new Marketplace();
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

