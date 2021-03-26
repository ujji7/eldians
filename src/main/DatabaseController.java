package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseController {
    private static final String FILENAMEGAME = "Game.json";
    private static File fileGame, fileUser, fileMarketplace;
    private static final String FILENAMEUSER = "User.json";
    private static final String FILENAMEMARKETPLACE = "Marketplace.json";
    private Gson gson;

    public DatabaseController(){
        fileGame = new File(FILENAMEGAME);
//            FileLock ignoredgame = fileGame.getChannel().lock();
        fileUser = new File(FILENAMEUSER);
//            FileLock ignoreduser = fileUser.getChannel().lock();
        fileMarketplace = new File(FILENAMEMARKETPLACE);
//            FileLock ignoredmarketplace = fileMarketplace.getChannel().lock();
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();

    }

    public void writeMarket(Marketplace market) throws IOException {
        appendData(fileMarketplace, market);
    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            appendData(fileUser, userList);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            appendData(fileGame, gameList);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendData(File filename, Object data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());
        FileWriter writer = new FileWriter(filename);
        writer.write(gson.toJson(data));
        writer.close();
    }

    //Read the 3 files if exist, otherwise create new files to be used to store
    private static void fileOpener() {
        if (!(fileGame = new File(FILENAMEGAME)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                fileGame.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }

        if (!(fileUser = new File(FILENAMEUSER)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                fileUser.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }

        if (!(fileMarketplace = new File(FILENAMEMARKETPLACE)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                fileMarketplace.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }
    }


    private static boolean isTypeFloat(String value) {
        try {
            Float.parseFloat(value);
        } catch (NumberFormatException n) {
            return false;
        }
        return true;
    }


    // verifies if the game follows game formatting and specifications
    private static boolean gameVerifier(String name, String price, String seller, String gameID, String discount) {

        if (name == null || name.length() > 25) { //check name
            System.out.println("name wrong" + name);
            return false;

        } else if (price == null || !isTypeFloat(price)) { //check price formatting
            return false;
        } else if (Float.parseFloat(price) > 999.99) {
            return false;

        } else if (seller == null || seller.length() > 15) {
            System.out.println("seller");
            return false;

        } else if (gameID == null || !isTypeFloat(gameID)) {
            System.out.println("gid");
            return false;

        } else if (discount == null || !isTypeFloat(discount)) {
            System.out.println("disc");
            return false;
        } else if (Float.parseFloat(discount) > 999.99) {
            return false;
        }
        System.out.println("all good");
        return true;
    }


    // read game - and add to games list
    private static ArrayList<Game> readGame(String fileNameGame) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileNameGame));
            ArrayList<Game> games = new ArrayList<Game>();
            String line, name, price, seller, gameID, discount;
            name = price = seller = gameID = discount = null;

            while ((line = br.readLine()) != null) { //read each line
                if (line.equalsIgnoreCase("name")) {
                    name = br.readLine();
                } else if (line.equalsIgnoreCase("price")) {
                    price = br.readLine();
                } else if (line.equalsIgnoreCase("seller")) {
                    seller = br.readLine();
                } else if (line.equalsIgnoreCase("gameID")) {
                    gameID = br.readLine();
                } else if (line.equalsIgnoreCase("discount")) {
                    discount = br.readLine();
                } else if (line.isBlank()) {
                    //make new function that adds game if verified
                    if (gameVerifier(name, price, seller, gameID, discount)) {
                        games.add(makeGame(name, price, seller, gameID, discount)); //make a new method here that converts the games into game objects and it also converts the string numbers into floats etc.
                        name = price = seller = gameID = discount = null;
                    }
                } else {
                    name = price = seller = gameID = discount = null;
                }
            }
            if (gameVerifier(name, price, seller, gameID, discount)) {
                games.add(makeGame(name, price, seller, gameID, discount));
            } return games;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Game makeGame(String name, String price, String seller, String gameID, String discount) {

//        if gameVerifier(name, price, seller, gameID, discount)

        String gameName = name.stripTrailing();
        float gamePrice = Float.parseFloat(price);
        String gameSupplier = seller.stripTrailing();
        int ID = Integer.parseInt(gameID);
        float gameDiscount = Float.parseFloat(discount);

        return new Game(gameName, gamePrice, gameSupplier, ID, gameDiscount);
//        return null;
    }


    public static void main(String[] args) throws IOException {
        Game g1 = new Game("Spiritfarer", 39.00, "Nintendo", 111, 20.00);
        Game g2 = new Game("Mariokart", 89.00, "Nintendo", 112, 20.00);
        Game g3 = new Game("Spyro", 59.00, "PlayStation", 113, 20.00);
        Game g4 = new Game("MarioParty", 59.00, "Nintendo", 114, 20.00);
        Game g5 = new Game("CSGO", 59.00, "Steam", 115, 20.00);
        Game g6 = new Game("Valhiem", 59.00, "Steam", 116, 20.00);
        Game g7 = new Game("COD", 59.00, "PlayStation", 117, 20.00);




        AdminUser AA = new AdminUser("Danielle", 10000.00f);
        BuyUser BS = new BuyUser("Ben", 1888.00f);
        SellUser SS = new SellUser("Porie", 333.00f);
        SellUser N = new SellUser("Nintendo", 44444.00f);
        SellUser S = new SellUser("Steam", 2.00f);
        SellUser P = new SellUser("Playstation", 5.00f);

        BS.password = "BUTTS";

        N.password = "Mario";

        BS.inventory.add(g1);
        BS.inventory.add(g2);

        S.inventory.add(g6);
        S.inventory.add(g5);

        P.inventory.add(g3);
        P.inventory.add(g7);

        TransactionHistory transhist = new TransactionHistory();

        String bs = "[buyUsername] has bought [gameName] from [sellerUsername] for [price].";
        String ac = "[Username] has added [credit] to their account balance; Balance: [Account Balance]";

        transhist.addTransaction(bs);
        transhist.addTransaction(ac);
        transhist.addTransaction(bs);
        transhist.addTransaction(ac);
        transhist.addTransaction(bs);
        transhist.addTransaction(ac);
        transhist.addTransaction(bs);
        transhist.addTransaction(ac);

        AA.transactionHistory = transhist;
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

        fileOpener();
        readGame(FILENAMEGAME);
    }
}

