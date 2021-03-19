//package main;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ReadingJSON {
//    private HashMap<Integer, Game> gameIDAll2 = new HashMap<Integer, Game>();

    //    https://www.baeldung.com/gson-deserialization-guide
    public static List<Game> readGamesFile() {
        try {

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("games.json"));

            Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new DeserializeGame()).create();

            List<Game> games = gson.fromJson(reader, new TypeToken<List<Game>>() {}.getType());

            // print users
            games.forEach(System.out::println);

            System.out.println("----");
            games.removeIf(Objects::isNull);
            System.out.println(games);

            //ALSO HAVE TO CHECK IF THE SAME GAMEID USED BEFORE
            ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();

            for (Iterator<Game> it = games.iterator(); it.hasNext(); ) {
                Game g = it.next();
                if (!(uniqueIDs.contains(g.getUniqueID()))) {
                    uniqueIDs.add(g.getUniqueID());
                } else {
                    it.remove();
                }
            }
            System.out.println(games);

            // close reader
            reader.close();
            return games;

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;

    }

    private HashMap<Integer, Game> listHashmapGames(List<Game> gamesGiven) {
        HashMap<Integer, Game> gameIDs = new HashMap<Integer, Game>();

        if (gamesGiven != null) {
            for (Game g : gamesGiven) {
//                this.gameIDAll2.put(g.getUniqueID(), g);
                gameIDs.put(g.getUniqueID(), g);
            }
        }
        return gameIDs;
//        return new HashMap<Integer, Game>();
    }

    public static List<AbstractUser> readUsersFile(List<Game> x) {
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("users.json"));

            GsonBuilder gsonBuilder = new GsonBuilder();

            DeserializeUser deserializer = new DeserializeUser();

            gsonBuilder.registerTypeAdapter(AbstractUser.class, deserializer);

            HashMap<Integer, Game> gameIDAll = new HashMap<Integer, Game>();
            if (x != null) {
                for (Game g : x) {
                    gameIDAll.put(g.getUniqueID(), g);
                }
            }

            System.out.println("5");

            deserializer.gameIDs = gameIDAll;  //EDIT THIS TO FOR LOOP SO THE THING WONT CHANGE
//            deserializer.listToHashmapGames(x);

            Gson gson = gsonBuilder.create();

//            Gson gson = new GsonBuilder().registerTypeAdapter(AbstractUser.class, new DeserializeUser()).create();

            List<AbstractUser> users = gson.fromJson(reader, new TypeToken<List<AbstractUser>>() {
            }.getType());

            // print users
            users.forEach(System.out::println);

            System.out.println("----");
            users.removeIf(Objects::isNull); //if anything is null = aka an error
            System.out.println(users);

            //ALSO HAVE TO CHECK IF THE SAME userID USED BEFORE
            ArrayList<String> uniqueSellers = new ArrayList<String>();

            for (Iterator<AbstractUser> it = users.iterator(); it.hasNext(); ) {
                AbstractUser u = it.next();
                if (!(uniqueSellers.contains(u.getUsername()))) {
                    uniqueSellers.add(u.getUsername());
                } else {
                    it.remove();
                }
            }
            System.out.println(users);

            // close reader
            reader.close();
            return users;

        } catch (IOException fileNotFoundException) {
            System.out.println("oops");
            fileNotFoundException.printStackTrace();
            return null;
        }
    }


    public static void main (String[]args){
        List<Game> xy = readGamesFile();
        System.out.println("games are: " + xy);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");

        List<AbstractUser> z = readUsersFile(xy);
        System.out.println(z);

        if (z != null) {
            for (AbstractUser u : z) {
                System.out.println(u.getUsername() + " has: " + u.inventory);
            }
        }
    }
}

