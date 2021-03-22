package main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * class reads the "daily.txt" file and send the transactions to be created to the TransactionFactory
 * and send an ArrayList of transactions to the Application to process these transactions
 *
 */
public class Client {
    private List<String> allLines;
    private ArrayList<ArrayList<String>> validFormatTrans;
    private ArrayList<Transaction> validTranasctions;
    private static String dailyTxt;

    public Client(String destination){
        // add the destination of the file
        this.dailyTxt = destination;
        // convert the entire file into a list
        boolean readCheck = this.fileToList();
        // checks if the file was successfully converted
        if (readCheck){
            // initializing an ArrayList of transactions
            this.listToValidTran(allLines);
            // if there are some formatted transactions then get all the
            if(validFormatTrans.size() > 0){
                TransactionFactory transactionFactory = new TransactionFactory();
                for(ArrayList<String> tran: validFormatTrans ){
                    // build the transaction Object and add it to the ArrayList of transaction objects
                    Transaction transaction = transactionFactory.buildTransaction(tran);
                    validTranasctions.add(transaction);
                }

                            /// SEND THE SEQUENCES TO APPLICATION TO PROCESS
                /// SEND THE SEQUENCES TO APPLICATION TO PROCESS
                            /// SEND THE SEQUENCES TO APPLICATION TO PROCESS

                /// have a main where you load in the daily.txt


            }
            else {
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " No valid transactions were found.");
            }
        }
    }


    /**
     * Read the file and turn it into a list for each line of the file
     *
     */
    private boolean fileToList(){
        boolean result = false;
        try {
            // check if to implement with buffer source for buffer: https://www.codingame.com/playgrounds/2913/how-to-read-a-text-file-in-java
            // OR https://www.programcreek.com/java-api-examples/?class=java.nio.file.Files&method=readAllLines
            this.allLines = Files.readAllLines(Paths.get(dailyTxt));
            result = true;
        }
        catch (IOException e) {
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " When loading daily.txt");
                // check if this is required or not
                e.printStackTrace();
        }
        return result;
    }


    /**
     * Splitting and adding the correct formatted transaction sequences to an ArrayList
     *
     * @param allLines list of the
     */
    private void listToValidTran(List<String> allLines){
        // going over all the transactions
        for (String inTran : allLines){
            try{
                String tranType;
                // check the first code
                tranType = inTran.substring(0,2);

                // check for the transactions with the same format of 31 characters[00/01/02/06/07/10]
                if (tranType.equals("00") || tranType.equals("01") || tranType.equals("02")
                        || tranType.equals("06") || tranType.equals("07") || tranType.equals("10")){
                    // will contain only 1 element if any of the input is invalid
                    ArrayList<String> preTran = this.len31Transaction(inTran);
                    // append to validTranasctions if transaction is of right format
                    this.addPossSeq(preTran, inTran);
                }
                // Extracting fields for the SELL transaction
                else if(tranType.equals("03")) {
                    // will contain only 1 element if any of the input is invalid
                    ArrayList<String> preTran = this.extractSellInfo(inTran);
                    // append to validTranasctions if transaction is of right format
                    this.addPossSeq(preTran, inTran);
                }
                // Extracting fields for BUY transaction
                else if(tranType.equals("04")){
                    // will contain only 1 element if any of the input is invalid
                    ArrayList<String> preTran = this.extractBuyInfo(inTran);
                    // append to validTranasctions if transaction is of right format
                    this.addPossSeq(preTran, inTran);
                }
                // Extracting fields for REFUND transaction
                else if(tranType.equals("05")){
                    // will contain only 1 element if any of the input is invalid
                    ArrayList<String> preTran = this.extractRefundInfo(inTran);
                    // append to validTranasctions if transaction is of right format
                    this.addPossSeq(preTran, inTran);
                }
                // invalid Transaction code
                else{
                    System.out.println("ERROR: \\ < Failed Constraint: Invalid transaction Code >");
                    System.out.println(" in the transaction: " + inTran);
                }

                }

            catch(Exception e){
                // what kind of error is it?!
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " Transaction length is not valid in daily.txt");
            }

            }
    }


    /**
     * helper to add properly formatted transactions
     *
     * @param trans an ArrayList of the transaction containing Strings split into useful fields
     * @param inTran The input of the transaction
     */
    private void addPossSeq(ArrayList<String> trans, String inTran){
        if(trans.size() > 1){
            this.validFormatTrans.add(trans);
        }
        else{
            System.out.println("ERROR: \\ < Failed Constraint: Invalid Transaction input format >");
            System.out.println(" in the transaction: " + inTran);
        }
    }



    /**
     * Checks and strips out and returns the necessary input form the received BUY transaction
     *
     * @param transaction the transaction from the daily.txt
     * @return an ArrayList of String, with a transaction or empty string otherwise
     */
    private ArrayList<String> extractRefundInfo(String transaction){
        ArrayList<String> result = new ArrayList<>();
        if(transaction.length() == 44){
            // Extracting only the required info for constructing the objects
            String transCode = transaction.substring(0,2);
            String buyerName = this.stringInputStrip(transaction.substring(2,18));
            String sellerName = this.stringInputStrip(transaction.substring(18,34));
            String refundAmount = this.numericInputStrip(transaction.substring(34,44));
            // Checking all the field actually contain valuable information and adding them if they do
            boolean validFieldChecks = this.tran3EntriesCheck(buyerName,sellerName, refundAmount);
            if (validFieldChecks){
                result.add(transCode);
                result.add(buyerName);
                result.add(sellerName);
                result.add(refundAmount);
            }
            else {
                // error check addition
                result.add("");
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " Transaction contains fields not provided daily.txt");
            }
        }
        else{
            // error check addition
            result.add("");
            System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                    " Transaction is of invalid length for the REFUND transaction in daily.txt");
        }
        return result;
    }


    /**
     * Checks and strips out and returns the necessary input form the received BUY transaction
     *
     * @param transaction the transaction from the daily.txt
     * @return an ArrayList of String, with a transaction or empty string otherwise
     */
    private ArrayList<String> extractBuyInfo(String transaction){
        ArrayList<String> result = new ArrayList<>();
        if(transaction.length() == 60){
            // Extracting only the required info for constructing the objects
            String transCode = transaction.substring(0,2);
            String preGame = this.stringInputStrip(transaction.substring(2,28));
            String sellerName = this.stringInputStrip(transaction.substring(28,44));
            String buyerName = this.stringInputStrip(transaction.substring(44,60));
            // Checking all the field actually contain valuable information and adding them if they do
            boolean validFieldChecks = this.tran3EntriesCheck(preGame,sellerName, buyerName);
            if (validFieldChecks){
                result.add(transCode);
                result.add(preGame);
                result.add(sellerName);
                result.add(buyerName);
            }
            else {
                // error check addition
                result.add("");
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " Transaction contains fields not provided daily.txt");
            }
        }
        else{
            // error check addition
            result.add("");
            System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                    " Transaction is of invalid length for the BUY transaction in daily.txt");
        }
        return result;
    }


    /**
     * Checks and strips out and returns the necessary input form the received SELL transaction
     *
     * @param transaction the transaction from the daily.txt
     * @return an ArrayList of String, with a transaction or empty string otherwise
     */
    private ArrayList<String> extractSellInfo(String transaction){
        ArrayList<String> result = new ArrayList<>();
        if(transaction.length() == 57){
            // Extracting only the required info for constructing the objects
            String transCode = transaction.substring(0,2);
            String preGame = this.stringInputStrip(transaction.substring(2,28));
            String preUsername = this.stringInputStrip(transaction.substring(28,44));
            String disAmount = this.numericInputStrip(transaction.substring(44,50));
            String costAmount = this.numericInputStrip(transaction.substring(50,57));
            // Checking all the field actually contain valuable information and adding them if they do
            boolean validFieldChecks = this.tran4EntriesCheck(preGame, preUsername, disAmount, costAmount);
            if (validFieldChecks){
                result.add(transCode);
                result.add(preGame);
                result.add(preUsername);
                result.add(disAmount);
                result.add(costAmount);
            }
            else {
                // error check addition
                result.add("");
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " Transaction contains fields not provided daily.txt");
            }
        }
        else{
            // error check addition
            result.add("");
            System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                    " Transaction is of invalid length for SELL transaction in daily.txt");
        }
        return result;
    }


    /**
     * Checks and strips out and returns the necessary input form the received transaction
     *
     * @param transaction the transaction from the daily.txt
     * @return an ArrayList of String, with a transaction or empty string otherwise
     */
    private ArrayList<String> len31Transaction(String transaction){
        ArrayList<String> result = new ArrayList<>();
        if(transaction.length() == 31){
            // Extracting only the required info for constructing the objects
            String transCode = transaction.substring(0,2);
            String preUserName = this.stringInputStrip(transaction.substring(2,18));
            String preUserType = this.stringInputStrip(transaction.substring(18,21));
            String preAmount = this.numericInputStrip(transaction.substring(21,31));
            // Checking all the field actually contain valuable information and adding them if they do
            boolean validFieldChecks = this.tran3EntriesCheck(preUserName,preUserType, preAmount);
            if (validFieldChecks){
                result.add(transCode);
                result.add(preUserName);
                result.add(preUserType);
                result.add(preAmount);
            }
            else {
                // error check addition
                result.add("");
                System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                        " Transaction contains fields not provided daily.txt");
            }
        }
        else{
            // error check addition
            result.add("");
            System.out.println("<FATAL ERROR>: Encountered in Client.java." +
                    " Transaction is of invalid length in daily.txt");
        }

        return result;
    }

    /**
     * Checking if the entries actually contain information
     *
     * @param entry1 field possibly containing information
     * @param entry2 field possibly containing information
     * @param entry3 field possibly containing information
     * @param entry4 field possibly containing information
     * @return true if the all the entries contain fields
     */
    private boolean tran4EntriesCheck(String entry1, String entry2, String entry3, String entry4){
        return this.tran3EntriesCheck(entry1, entry2, entry3) && entry4.length() > 0;
    }


    /**
     * Checking if the entries actually contain information
     *
     * @param entry1 field possibly containing information
     * @param entry2 field possibly containing information
     * @param entry3 field possibly containing information
     * @return true if the all the entries contain fields
     */
    private boolean tran3EntriesCheck(String entry1, String entry2, String entry3){

        return entry1.length() > 0 && entry2.length() > 0 && entry3.length() > 0;
    }


    /**
     * Checks if the first field is empty this is to make sure there are valid space between each piece of
     * transaction information being recieved
     *
     * @param field piece of information from the transaction code to be checked and processed
     * @return the trimmed version of the valid string or returns a "" otherwise
     */
    private String stringInputStrip(String field){
        String result = "";
        // checking if the 1st chr
        if(field.charAt(0) == ' ' && field.charAt(1) != ' '){
            // remove the padding around the value
            result = field.trim();
        }
        return result;
    }

    /**
     * checks if the first field is empty and making sure the format for the float input is right.
     * Checking the correct decimal point format
     *
     * @param field piece of transaction code that deals with numeric input
     * @return the trimmed version of the valid string or returns a "" otherwise
     */
    private String numericInputStrip(String field){
        String result = "";
        // checking the 1st chracter and the format of digits input
        if(field.charAt(0) == ' ' && field.charAt(1) != ' ' && field.charAt(field.length() - 3) == '.' ){
            result = field.trim();
        }
        return result;
    }

}
