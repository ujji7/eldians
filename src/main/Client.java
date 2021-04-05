package main;

import transactions.Transaction;
import transactions.TransactionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * class checks for potential fatal errors and reads the "daily.txt" file and
 * send the transactions to be created and executed
 */
public class Client {
    // Holds all the right formatted transactions and valid transaction objects to be processed and executed
    private ArrayList<ArrayList<String>> validFormatTrans;
    private ArrayList<Transaction> validTransactions;
    // Login, Logout, Create, Add-Credit, Delete, Auction-Sale regex format
    private static String regLLCADT = "(0[0-7]|10)\\s(.{15})\\s(AA|FS|BS|SS)\\s(\\d{6}\\.\\d{2})";
    // Sell, Buy, Refund transaction regex
    private static String regSell = "(03)\\s(.{25})\\s(.{15})\\s(\\d{2}\\.\\d{2})\\s(\\d{3}\\.\\d{2})";
    private static String regBuy = "(04)\\s(.{25})\\s(.{15})\\s(.{15})";
    private static String regRefund = "(05)\\s(.{15})\\s(.{15})\\s(\\d{6}\\.\\d{2})";
    // Gift and Remove regex
    private static String regRemGif = "(0[89])\\s(.{25})\\s(.{15})\\s(.{15})";
    // HashMap of all the regex formats
    private HashMap<String, String> regMap;


    /**
     * Checks the formatting in the transaction file and creates and runs the transactions
     *
     * @param destination the location of the file containing all the generated transactions
     */
    public Client(String destination) {
        BufferedReader br = null;
        String line;
        this.validFormatTrans = new ArrayList<>();
        this.initialiseReg();
        try {
            // opening the stream and reading the file
            br = new BufferedReader(new FileReader(destination));

            // adds valid ArrayList of Strings of valid formatted transaction
            while ((line = br.readLine()) != null) {
                if (line.length() >= 31 && line.length() <= 60 ) {
                    String tranType = line.substring(0, 2);
                    // Login, Logout, Create, Add-Credit, Delete, Auction-Sale transactions
                    if (tranType.equals("00") || tranType.equals("01") || tranType.equals("02")
                            || tranType.equals("06") || tranType.equals("07") || tranType.equals("10")) {
                        this.len31Transaction(tranType,line);
                    }
                    // Sell transaction
                    else if (tranType.equals("03")){
                        this.sellTranFormat(tranType, line);
                    }
                    // Buy transaction
                    else if(tranType.equals("04")){
                        this.buyTranFormat(tranType, line);
                    }
                    // Refund transaction
                    else if(tranType.equals("05")){
                        this.refundTranFormat(tranType, line);
                    }
                    // Remove, Gift transaction
                    else if(tranType.equals("08") || tranType.equals("09")){
                        this.removeGiftFormat(tranType, line);
                    }
                    // Invalid transaction code
                    else{
                        System.out.println("<Fatal Error: for Current input of: " + line + ". The Transaction code in "+
                                "daily.txt is not valid>\nError encountered in Client.java");
                    }
                }
                else{
                    System.out.println("<Fatal Error: for Current input of :" + line + " .The Transaction format in " +
                            "daily.txt is not valid>\nError encountered in Client.java");
                }
            }
            // closing the stream
            br.close();
            // Sending all the transactions to be made in the factory and then sending all the Transaction to Application
            if(validFormatTrans.size() >=2){
                this.makeAndExecute();
            }
            else{
                System.out.println("<Fatal Error: A valid transaction sequence was not found in" +
                        " daily.txt>" +
                        " \n Error encountered in Client.java");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("<Fatal Error: "+
                    "Error in reading daily.txt>\n Error encountered in Client.java");
        }
    }


    /**
     * Sends all the properly formatted transactions to the Transaction Factory and then sends it to the application
     * for processing
     *
     *
     */
    private void makeAndExecute() {
        this.validTransactions = new ArrayList<Transaction>();
        // make an array list of Transaction Object
        for (ArrayList<String> tranSeq : validFormatTrans) {
            TransactionFactory myTranFactory = new TransactionFactory();

            this.validTransactions.add(myTranFactory.buildTransaction(tranSeq));

        }
        // If there are valid transaction objects then send them to Application upon a valid transaction Sequence
        if(validTransactions.size() >1){
            Application app = new Application();
            app.Run(validTransactions);
        }
        else{
            System.out.println("<Fatal Error: A valid transaction sequence was not found in" +
                    " daily.txt>" +
                    " \n Error encountered in Client.java");
        }
    }


    /**
     * CHECKS and strips out and returns the necessary input form the received transaction
     * dealing with REMOVE and GIFT transactions
     *
     * @param tranType the transaction code related to the transaction
     * @param transaction the transaction from the daily.txt
     */
    private void removeGiftFormat(String tranType, String transaction){
        if(this.formatChecker(tranType, transaction)){
            // Extracting the data from the transaction and adding it to the ArrayList
            String gameName = this.stripSpace(transaction.substring(3,28));
            String ownerName = this.stripSpace(transaction.substring(29,44));
            String recieverName = this.stripSpace(transaction.substring(45,60));
            if(this.containsData(gameName)){
                ArrayList<String> result = new ArrayList<>();
                // Gift Transaction
                if(tranType.equals("09")){
                    if(this.containsData(recieverName)){
                        result.add(tranType);
                        result.add(gameName);
                        result.add(ownerName);
                        result.add(recieverName);
                        this.validFormatTrans.add(result);
                    }
                    else{
                        // Transaction format is not valid
                        System.out.println("<Fatal Error: Receiver name not provided in the : " + transaction +
                                ".\nThis GIFT Transaction not valid in daily.txt>.\n" +
                                " Error encountered in Client.java");
                    }
                }
                // Remove transaction
                else{
                    result.add(tranType);
                    result.add(gameName);
                    result.add(ownerName);
                    result.add(recieverName);
                    this.validFormatTrans.add(result);
                }
            }
            else{
                // Transaction format is not valid
                System.out.println("<Fatal Error: Game name not provided in the : " + transaction +
                        ".\nThis Transaction not valid in daily.txt>.\n" + " Error encountered in Client.java");
            }
        }
    }


    /**
     * CHECKS and strips out and returns the necessary input form the received transaction
     * dealing with REFUND transactions
     *
     * @param tranType the transaction code related to the transaction
     * @param transaction the transaction from the daily.txt
     */
    private void refundTranFormat(String tranType, String transaction){
        if(this.formatChecker(tranType, transaction)){
            // Extracting the data from the transaction and adding it to the ArrayList
            String buyerName = this.stripSpace(transaction.substring(3,18));
            String sellerName = this.stripSpace(transaction.substring(19,34));
            String amount = transaction.substring(35,44);
            if(this.containsData(buyerName) && this.containsData(sellerName)){
                ArrayList<String> result = new ArrayList<>();
                result.add(tranType);
                result.add(buyerName);
                result.add(sellerName);
                result.add(amount);
                this.validFormatTrans.add(result);
            }
            else{
                // Transaction format is not valid
                System.out.println("<Fatal Error: Buyer name or Seller name not provided in the : " + transaction +
                        ".\nThis REFUND Transaction not valid in daily.txt>.\n" + " Error encountered in Client.java");
            }
        }
    }


    /**
     * CHECKS and strips out and returns the necessary input form the received transaction
     * dealing with BUY transactions
     *
     * @param tranType the transaction code related to the transaction
     * @param transaction the transaction from the daily.txt
     */
    private void buyTranFormat(String tranType, String transaction){
        if(this.formatChecker(tranType, transaction)){
            // Extracting the data from the transaction and adding it to the ArrayList
            ArrayList<String> result = new ArrayList<>();
            String gameName = this.stripSpace(transaction.substring(3,28));
            String sellerName = this.stripSpace(transaction.substring(29,44));
            String buyerName = this.stripSpace(transaction.substring(45,60));
            // checking if we received the Seller Name and Game name
            if (this.containsData(sellerName) && this.containsData(gameName)){
                result.add(tranType);
                result.add(gameName);
                result.add(sellerName);
                result.add(buyerName);
                this.validFormatTrans.add(result);
            }
            else{
                // Transaction format is not valid
                System.out.println("<Fatal Error: Game name or Seller name not provided in the : " + transaction +
                        ".\nThis BUY Transaction not valid in daily.txt>.\n" + "Error encountered in Client.java");
            }
        }
    }


    /**
     * Helper to check if the current field contains any meaningfull data to carry out the transaction
     *
     * @param field the Name of User or Game being tested
     * @return true if there is some data false otherwise
     */
    private boolean containsData(String field){
        return field.length() > 0;
    }


    /**
     * CHECKS and strips out and returns the necessary input form the received transaction
     * dealing with transactions related to: login, logout, add-Credit, create, delete & auction-sale
     *
     * @param tranType the transaction code related to the transaction
     * @param transaction the transaction from the daily.txt
     */
    private void len31Transaction(String tranType, String transaction) {

        if(this.formatChecker(tranType, transaction)){
            // Extracting the data from the transaction and adding it to the ArrayList
            String userName = this.stripSpace(transaction.substring(3, 18));
            String userType = transaction.substring(19,21);
            String amount = transaction.substring(22,31);

            // For Login, Create and Delete we require a Username input
            if(tranType.equals("00") || tranType.equals("01") || tranType.equals("02")){
                if(this.containsData(userName)){
                    ArrayList<String> result = new ArrayList<>();
                    result.add(tranType);
                    result.add(userName);
                    result.add(userType);
                    result.add(amount);
                    this.validFormatTrans.add(result);
                }
                else{
                    System.out.println("<Fatal Error: Username not provided in the : " + transaction +
                            ".\nThis Transaction not valid in daily.txt>.\n" + "Error encountered in Client.java");
                }
            }
            // Add-Credit and Logout transactions don't necessarily require a name
            else{
                ArrayList<String> result = new ArrayList<>();
                result.add(tranType);
                result.add(userName);
                result.add(userType);
                result.add(amount);
                this.validFormatTrans.add(result);
            }
        }
    }


    /**
     * CHECKS and strips out and returns the necessary input form the received transaction
     * dealing with SELL transactions
     *
     * @param tranType the transaction code related to the transaction
     * @param transaction the transaction from the daily.txt
     */
    private void sellTranFormat(String tranType ,String transaction){
        if(this.formatChecker(tranType, transaction)){
            ArrayList<String> result = new ArrayList<>();
            // Extracting the data from the transaction and adding it to the ArrayList
            String gameName = this.stripSpace(transaction.substring(3,28));
            String sellerName = this.stripSpace(transaction.substring(29,44));
            String discount = transaction.substring(45,50);
            String price = transaction.substring(51,57);
            if(this.containsData(gameName)){
                result.add(tranType);
                result.add(gameName);
                result.add(sellerName);
                result.add(discount);
                result.add(price);
                this.validFormatTrans.add(result);
            }
            else{
                // Transaction format is not valid
                System.out.println("<Fatal Error: Game name not provided in the : " + transaction +
                        ".\nThis SELL Transaction not valid in daily.txt>.\n" + "Error encountered in Client.java");

            }
        }
    }


    /**
     * Initializes all the Regex formats to check the formats of the transactions
     *
     */
    private void initialiseReg(){
        this.regMap = new HashMap<>();
        // Login, Logout, Create, Add-Credit, Delete, Auction-Sale regex format
        regMap.put("00", regLLCADT);
        regMap.put("01", regLLCADT);
        regMap.put("02", regLLCADT);
        regMap.put("06", regLLCADT);
        regMap.put("07", regLLCADT);
        regMap.put("10", regLLCADT);
        // Sell, Buy, Refund regex
        regMap.put("03", regSell);
        regMap.put("04", regBuy);
        regMap.put("05", regRefund);
        // Remove, Gift regex
        regMap.put("08", regRemGif);
        regMap.put("09", regRemGif);
    }


    /**
     * CHECKS the format of the transaction being tested
     *
     * @param tranType the transaction code of the transaction
     * @param transaction a line from daily.txt being tested
     * @return true if it is a valid transaction, false otherwise
     */
    private boolean formatChecker(String tranType, String transaction){
        boolean result = false;
        // Get the regex format for the transaction and check if it's a valid transaction
        String regFormat = this.regMap.get(tranType);
        Pattern pt = Pattern.compile(regFormat);
        Matcher mt = pt.matcher(transaction);
        result = mt.matches();
        if(!result){
            // Transaction format is not valid
            System.out.println("<Fatal Error: for Current input of : " + transaction + ". This Transaction format in " +
                    "daily.txt is not valid>\nError encountered in Client.java");
        }

        return result;
    }

    /**
     * Strips the trailing whitespaces from a transaction field
     *
     * @param field usernames or game names from the transaction code
     * @return a stripped String without the trailing white spaces
     */
    private String stripSpace(String field){
        String spaceReg = "\\s+$";
        return field.replaceAll(spaceReg, "");
    }

    
    /**
     * Starts and runs our Program
     *
     */
    public static void main(String[] args){
        Client client = new Client("daily.txt");

    }
}

