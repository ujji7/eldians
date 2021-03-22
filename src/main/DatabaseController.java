package main;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DatabaseController {
    private static final String FILENAMEGAME = "Game.json";
    private RandomAccessFile fileGame;
    private static final String FILENAMEUSER = "User.json";
    private RandomAccessFile fileUser;
    private static final String FILENAMEMARKETPLACE = "Marketplace.json";
    private RandomAccessFile fileMarketplace;
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
        //discount
        //Seller,[Game1, Game2,...,GameX]
        //"Valve",[10057,10067]

        appendData(fileMarketplace, gson.toJson(market));
//        appendData(fileMarketplace, gson.toJson(market.getAuctionSale()));
//        for (AbstractUser seller: market.getGamesOnSale().keySet()
//             ) {
//            appendData(fileMarketplace, gson.toJson(seller));
//            appendData(fileMarketplace, gson.toJson(market.getGamesOnSale().get(seller)));

//            ArrayList<String> data = collectMarketplaceData(seller, market.getGamesOnSale().get(seller));
//            for (String item: data) {
//                appendData(fileMarketplace, item);
//            }
//        }
        fileMarketplace.close();
    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        try {
            appendData(fileUser, gson.toJson(userList));
            fileUser.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGame(ArrayList<Game> gameList) throws IOException {
        try {
            appendData(fileGame, gson.toJson(gameList));
            fileGame.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> collectMarketplaceData(AbstractUser user, ArrayList<Game> games){
        ArrayList<String> data = new ArrayList<String>();
        data.add("name");
        data.add(user.getUsername());
        data.add("gamesOnSale");
        for (Game game:games) {
            data.add(String.valueOf(game.getUniqueID()));
        }
        return data;
    }

    private ArrayList<String> collectGameData(Game game){
        ArrayList<String> data = new ArrayList<String>();
        data.add("name");
        data.add(game.getName());
        data.add("price");
        data.add(String.valueOf(game.getPrice()));
        data.add("supplierID");
        data.add(game.getSupplierID());
        data.add("uniqueID");
        data.add(String.valueOf(game.getUniqueID()));
        data.add("dicount");
        data.add(String.valueOf(game.getDiscount()));
        return data;
    }

    private ArrayList<String> collectUserData(AbstractUser user){
        ArrayList<String> data = new ArrayList<String>();
        data.add("username");
        data.add(user.getUsername());
        data.add("type");
        data.add(String.valueOf(user.type));
        data.add("accountBalance");
        data.add(String.valueOf(user.getAccountBalance()));
        data.add("inventory");
        if(!user.inventory.isEmpty()) {
            for (Game game : user.inventory) {
                data.add(String.valueOf(game.getUniqueID()));
            }
        }else{
            data.add("\n");
        }
        data.add("transactionHistory");
        if(!user.transactionHistory.isEmpty()){
            data.addAll(user.transactionHistory);
        }else{
            data.add("\n");
        }
        data.add("\n");
        data.add("\n");
        return data;
    }

    private void appendData(RandomAccessFile filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
//        filename.seek(filename.length());
        filename.writeBytes(data);
    }

//    private void fileOpener(File file, String filename) {
//        if (file.getName().equals(filename)) {
//            switch (file.getName()) {
//                case FILENAMEGAME:
//                    if (!(fileGame = new File(filename)).exists()) {
//                        try {
//                            fileGame.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//                case FILENAMEUSER:
//                    if (!(fileUser = new File(filename)).exists()) {
//                        try {
//                            fileUser.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//                case FILENAMEMARKETPLACE:
//                    if (!(fileMarketplace = new File(filename)).exists()) {
//                        try {
//                            fileMarketplace.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//            }
//        } else {
//            System.out.println("INVALID FILENAME");
//        }
//    }

//    private String userPadder(AbstractUser user){
//        String name = user.username;
//        String type = String.valueOf(user.type);
//        String account = String.valueOf(user.accountBalance);
//        String inventory = "";
//        String history = "";
//        if(name.length() < 15){
//            name = name + ((25 - name.length())*' ');
//        }
//        if(account.length() < 9) //999999.00
//
//        for (Game game:user.inventory) {
//            if (inventory.isEmpty()){
//                inventory = inventory.concat("[" + game.getUniqueID());
//                }
//            else{
//                inventory = inventory.concat("," + game.getUniqueID());
//            }}
//        inventory = inventory.concat("]");
//
//        return String.format("%s\n", inventory);
//
//    }


    private String gamePadder(Game game){
//        String name = game.getName();
//        String price = String.valueOf(game.getPrice());
//        String supplierID = game.getSupplierID();
//        String uniqueID = String.valueOf(game.getUniqueID());
//        String discount = String.valueOf(game.getDiscount());
//
//        if(name.length() < 25){
//            name = name + ((25 - name.length())*' ');
//        }
//        if(price.length() < 6){
//            price = (6-price.length())*'0' + price;
//        }
//        if(supplierID.length() < 15){
//            supplierID = supplierID + ((15 - supplierID.length())*' ');
//        }
//        if(uniqueID.length() < 8){
//            uniqueID = (8-uniqueID.length())*'0' + uniqueID;
//        }
//        if(discount.length() < 5){
//            discount = (5-discount.length())*'0' + discount;
        return String.format("name\n%s\nprice\n%s\nsupplierID\n%s\n uniqueID\n%s\ndiscount\n%s\n",game.getName()
                ,game.getPrice(),game.getSupplierID(), game.getUniqueID(), game.getDiscount());
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

        ArrayList<String> transhist = new ArrayList<String>();

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

