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

// CHECK IF U EVER DO THIS.BOARD = BOARD
// WHAT IF FILE LOOKS LIKE {}
public class ReadingJSON {
    //    https://www.baeldung.com/gson-deserialization-guide
    private static File Game, User, Market;
    private static final String fileNameGame = "games.json";
    private static final String fileNameUser = "users.json";
    private static final String fileNameMarket = "market.json";

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


    public List<Game> readGamesFile() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameGame)); // create a reader to read the games file

            Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new DeserializeGame()).create(); //create Gson
            // object to build the List of games


            List<Game> games = gson.fromJson(reader, new TypeToken<List<Game>>() {}.getType()); //Return list of games
            // according to game deserializer

            games.removeIf(Objects::isNull); //remove any null game Objects - they are not in proper game format

            ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();

            for (Iterator<Game> it = games.iterator(); it.hasNext(); ) { //check that there are no duplicate gameIDs
                Game g = it.next();
                if (!(uniqueIDs.contains(g.getUniqueID()))) {
                    uniqueIDs.add(g.getUniqueID());
                } else {
                    it.remove();
                }
            }

            reader.close(); // close reader
            return games;

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
        return null;

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

    public List<AbstractUser> readUsersFile(List<Game> gamesList) {
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
            fileNotFoundException.printStackTrace();
            return null;
        } catch (JsonSyntaxException e){
            return new ArrayList<>();
        }
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

    public Marketplace readMarketFile(List<Game> listGames, List<AbstractUser> listUsers) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileNameMarket));  // create a reader
            GsonBuilder gsonBuilder = new GsonBuilder();
            DeserializeMarketplace deserializer = new DeserializeMarketplace();
            gsonBuilder.registerTypeAdapter(Marketplace.class, deserializer);

            deserializer.gameIDs = setGamesList(listGames);
            deserializer.users = getUserHashmap(listUsers);

            Gson gson = gsonBuilder.create();

            Marketplace market = gson.fromJson(reader, Marketplace.class);

            reader.close(); // close reader
            return market;

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException e){
            return new Marketplace(false, new HashMap<String,ArrayList<Game>>());
        }
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

