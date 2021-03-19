package main;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ReadingJSON {
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

    public static void main(String args[]) {
        List<Game> x = readGamesFile();
        System.out.println("games are: " + x);

//

//        try {
//
//            // create a reader
//            Reader reader = Files.newBufferedReader(Paths.get("users.json"));
//
//            Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new DeserializeGame()).create();
//
//            List<Game> games = gson.fromJson(reader, new TypeToken<List<Game>>() {}.getType());
//
//            // print users
//            games.forEach(System.out::println);
//
//            System.out.println("----");
//            games.removeIf(Objects::isNull);
//            System.out.println(games);
//
//            //ALSO HAVE TO CHECK IF THE SAME GAMEID USED BEFORE
//            ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();
//
//            for (Game game : games) {
//                if (!(uniqueIDs.contains(game.getUniqueID()))) {
//                    uniqueIDs.add(game.getUniqueID());
//                } else {
//                    games.remove(game);
//                }
//            }
//
//            for (Iterator<Game> it = games.iterator(); it.hasNext(); ) {
//                Game g = it.next();
//                if (!(uniqueIDs.contains(g.getUniqueID()))) {
//                    uniqueIDs.add(g.getUniqueID());
//                } else {
//                    it.remove();
//                }
//            }
//            System.out.println(games);
//
//            // close reader
//            reader.close();
//
//
//
//            } catch (IOException fileNotFoundException) {
//            fileNotFoundException.printStackTrace();
//        }
    }
}

