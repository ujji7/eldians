package main;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.io.*;
import java.util.Objects;

public class DatabaseController {
    private static final String FILENAMEGAME = "Game.txt";
    private RandomAccessFile fileGame;
    private static final String FILENAMEUSER = "User.txt";
    private RandomAccessFile fileUser;
    private static final String FILENAMEMARKETPLACE = "Marketplace.txt";
    private RandomAccessFile fileMarketplace;

    public DatabaseController(){
        try { fileGame = new RandomAccessFile(FILENAMEGAME, "rw");
            FileLock ignoredgame = fileGame.getChannel().lock();
            fileUser = new RandomAccessFile(FILENAMEUSER, "rw");
            FileLock ignoreduser = fileUser.getChannel().lock();
            fileMarketplace = new RandomAccessFile(FILENAMEMARKETPLACE, "rw");
            FileLock ignoredmarketplace = fileMarketplace.getChannel().lock();
        } catch (IOException e) {
                e.printStackTrace();
            }

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


    public void writeMarket(Marketplace market) throws IOException {
        //discount
        //Seller,[Game1, Game2,...,GameX]
        //"Valve",[10057,10067]
        appendData(fileMarketplace, String.valueOf(market.getAuctionSale()));
        appendData(fileMarketplace, String.valueOf(market.getDiscount()));
        for (AbstractUser seller: market.getGamesOnSale().keySet()
             ) {
            ArrayList<String> data = collectMarketplaceData(seller, market.getGamesOnSale().get(seller));
            for (String item: data) {
                appendData(fileMarketplace, item);
            }
        }
        fileMarketplace.close();
    }

    public void writeUser(ArrayList<AbstractUser> userList) throws IOException {
        //Name,type,fundsAvailble,inventory[],transactionHistory[],
        //Madeo,BS,105.50,[gid1,gid2,gid3],["Bought Counter-Strike from Valve","Sold Half Life to John Doe"]

//        try (RandomAccessFile reader = new RandomAccessFile(FILENAMEGAME, "rw");
//             FileLock ignored = reader.getChannel().lock()) {
        for (AbstractUser user : userList) {
            if (!user.username.isEmpty()) {
                ArrayList<String> data = collectUserData(user);
                for (String item:
                     data) {
                    appendData(fileUser, item);
                }
//
//                // Step 3.1: Format our string to write to the file with yyyy-mm-dd text
//                String line = String.format("%s,%c,%a,[%s],[%s]\n", user.username, user.type,
//                        user.accountBalance, user.inventory, user.transactionHistory);
//                // Step 3.2: Seek to the end of the file BEFORE writing else you will be overwriting it
//                fileUser.seek(fileUser.length());
//                // Step 3.3 Write your data to the file
//                fileUser.writeChars(line);
            }
        }
        fileUser.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void writeGame(ArrayList<Game> gameList) throws IOException {
        //Name,Seller,Price,discount,gid
        //"Counter-Strike","Valve",20.99,10.06,10057
        //Game(String name, float price, String supplierID, int uniqueID, double discount

//        try (RandomAccessFile reader = new RandomAccessFile(FILENAMEGAME, "rw");
//             FileLock ignored = reader.getChannel().lock()) {
        for (Game game : gameList) {
            if (!game.getName().isEmpty()) {
                ArrayList<String> data = collectGameData(game);
                for (String item : data
                ) {
                    appendData(fileGame, item);
                }
            }
        }
        fileGame.close();
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
        }
        data.add("transactionHistory");
        if(!user.transactionHistory.isEmpty()){
            data.addAll(user.transactionHistory);
        }
        return data;
    }

    private void appendData(RandomAccessFile filename, String data) throws IOException {
        //https://www.journaldev.com/921/java-randomaccessfile-example
        filename.seek(filename.length());
        filename.writeChars(data);
    }
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
}