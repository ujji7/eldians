package main;

import java.util.ArrayList;

import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseController {

    private static File Game, User, Marketplace;
    private static String fileNameGame = "Game.txt";
    private static String fileNameUser = "User.txt";
    private static String fileNameMarketplace = "Marketplace.txt";


    //Read the 3 files if exist, otherwise create new files to be used to store
    private static void fileOpener() {
        if (!(Game = new File(fileNameGame)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                Game.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }

        if (!(User = new File(fileNameUser)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                User.createNewFile();
            } catch (IOException o) {
                o.printStackTrace();
            }
        }

        if (!(Marketplace = new File(fileNameMarketplace)).exists()) { //if game file exists, we can assume all 3 ones exist
            try {
                Marketplace.createNewFile();
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

        return Game(gameName, gamePrice, gameSupplier, ID, gameDiscount);
//        return null;
    }


    public ArrayList<AbstractUser> readUser() {

        //User.txt
        //Name,type,fundsAvailble,inventory[],transactionHistory[],
        //"Madeo",BS,105.50,[gid1,gid2,gid3],["Bought Counter-Strike from Valve","Sold Half Life to John Doe"]

        return {};
    }

    public Marketplace readMarketplace() {

        return {};
    }

    public static void main(String[] args) {
        fileOpener();
        readGame(fileNameGame);

    }
}
