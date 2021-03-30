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
    private static final String FILENAMEGAME = "Game.json";
    private RandomAccessFile fileGame, fileUser, fileMarketplace;
    private static final String FILENAMEUSER = "User.json";
    private static final String FILENAMEMARKETPLACE = "Marketplace.json";
    private Gson gson;

    public DatabaseController(){
        try { fileGame = new RandomAccessFile(FILENAMEGAME, "rw");
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
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            appendData(fileGame, gson.toJson(gameList));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendData(RandomAccessFile filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());

        filename.writeBytes(data);
        filename.close();
    }




    public static void main(String[] args) throws IOException {
        Game g1 = new Game("Spiritfarer", 39.00, "Nintendo", 111, 20.00);
        Game g2 = new Game("Mariokart", 89.00, "Nintendo", 112, 20.00);
        Game g3 = new Game("Spyro", 59.00, "PlayStation", 113, 20.00);
        Game g4 = new Game("MarioParty", 59.00, "Nintendo", 114, 20.00);
        Game g5 = new Game("CSGO", 59.00, "Steam", 115, 20.00);
        Game g6 = new Game("Valhiem", 59.00, "Steam", 116, 20.00);
        Game g7 = new Game("COD", 59.00, "PlayStation", 117, 20.00);




        AdminUser AA = new AdminUser("Danielle", 10000.00);
        BuyUser BS = new BuyUser("Ben", 1888.00);
        SellUser SS = new SellUser("Porie", 333.00);
        FullStandardUser N = new FullStandardUser("Nintendo", 44444.00);
        SellUser S = new SellUser("Steam", 2.00);
        SellUser P = new SellUser("Playstation", 5.00);


        BS.inventory.add(g1);
        BS.inventory.add(g2);

        S.inventory.add(g6);
        S.inventory.add(g5);

        P.inventory.add(g3);
        P.inventory.add(g7);

        ArrayList<String> transhist = new TransactionHistory();

        String bs = "[buyUsername] has bought [gameName] from [sellerUsername] for [price].";
        String ac = "[Username] has added [credit] to their account balance; Balance: [Account Balance]";

        transhist.add(bs);
        transhist.add(ac);
        transhist.add(bs);
        transhist.add(ac);
        transhist.add(bs);
        transhist.add(ac);
        transhist.add(bs);
        transhist.add(ac);

        AA.transactionHistory = transhist;
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);
        AA.transactionHistory.add(ac);

        BS.transactionHistory = transhist;

        ArrayList<Game> games = new ArrayList<Game>();
        games.add(g1);
        games.add(g2);
        games.add(g3);
        games.add(g4);
        games.add(g5);
        games.add(g6);
        games.add(g7);


        ArrayList<AbstractUser> users = new ArrayList<AbstractUser>();
        users.add(AA);
        users.add(BS);
        users.add(SS);
        users.add(N);
        users.add(P);
        users.add(S);

        Marketplace m = new Marketplace();
        m.auctionSale = false;
        HashMap<String, ArrayList<Game>> gos = new HashMap<String, ArrayList<Game>>();
        gos.put(SS.username, games);
        gos.put(AA.username, games);
        gos.put(N.username, games);
        gos.put(S.username, games);
        gos.put(P.username, games);

        m.gamesOnSale = gos;

        DatabaseController DBC = new DatabaseController();

        DBC.writeGame(games);

        DBC.writeUser(users);

        DBC.writeMarket(m);
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