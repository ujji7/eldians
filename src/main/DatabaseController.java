package main;

import java.util.ArrayList;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
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

    //what does this have to do
    //read the game file and create the appropriate stuff, as long as they match the format - check with line parsing etc.


    public ArrayList<Game> readGame() {

        //Game.txt
        //Name,Seller,Price,discount,gid
        //"Counter-Strike","Valve",20.99,10.06,10057
        //"Half-Life","Valve",0.00,10067
        return {};
    }

    public ArrayList<AbstractUser> readUser() {

        //User.txt
        //Name,type,fundsAvailble,inventory[],transactionHistory[],
        //Madeo,BS,105.50,[gid1,gid2,gid3],["Bought Counter-Strike from Valve","Sold Half Life to John Doe"]

        return {};
    }

    public Marketplace readMarketplace() {

        //Marketplace.txt
        //discount
        //Seller,[Game1, Game2,...,GameX]
        //Seller,[Game1,...,GameX]

        //true
        //"Valve",[10057,10067]
        //"Riot",[12653,12689]
        return {};
    }

    public static void main(String[] args) {
        readGame();

    }
}
