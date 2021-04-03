package main;
import java.util.ArrayList;
import java.util.HashMap;
//line 401
//REMOVE ANY UNNECESSARY PRINT STATEMENTS IN CREATE AND BUY

//SELL DOES NOT CHECK FOR GAMES ALREADY SOLD BY SELLER 
//SELL DOES NOT CHECK FOR IF THE GAME IS IN THEIR INVENTORY

//we need an auction sale method - i implemented it at botfitom check it out
// also look at readme for add credit - there is another implementations for admin type


//I made in game that returns the price with discount applied since we'll probs need it in many places.

//we need an auction sale method - i implemented it at bottom check it out


//Back End Error Recording:
//        All recorded errors should be of the form: ERROR: \\<msg\\>
//
//        For failed constraint errors, <msg> should contain the type and description
//        of the error and the transaction that caused it to occur.
//        For fatal errors, <msg> should contain the type and description and the file
//        that caused the error.
/**
 * Abstract class for User objects. There are 4 types of users: Admin, Buy, Sell, Full Standard. Each user has a 
 * username, a type, an account balance, a transaction history, and all users but the sell type have an inventory of 
 * games they have bought.
 *                                     
 */
public abstract class AbstractUser {

    protected String username;
    protected String type;
    protected double accountBalance = 0;
    protected ArrayList<Game> inventory;
    protected double newFunds = 0;
    public ArrayList<String> transactionHistory = new ArrayList<String>();
    protected static final double MAXFUNDS = 999999.99f;
    // can change minFunds to allow overdrafts for future improvements
    protected static final double MINFUNDS = 0f;
    protected static final double DAILYLIMIT = 1000f;
    private static final double NEWFUNDSTODAY = 0f;



    /**
     * Get the current User's unique username
     *
     * @return username String
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * get the account balance for the user
     * @return the current account balance of the user
     */
    public double getAccountBalance(){
        return this.accountBalance;
    }


    /** Get the user's type
     * 
     * @return the user's type
     */
    public String getType(){
        return this.type;
    }

    public ArrayList<Game> getInventory(){
        return this.inventory;
    }


    public ArrayList<String> getTransactionHistory(){
        
        return this.transactionHistory;
    }

    
    /**
     * Sets the account balance for our User
     *
     * @param balance the amount to balance to-be in the user's account
     */
    public void setAccountBalance(double balance){

        this.accountBalance = balance;
        this.transactionHistory.add("User: " + this.username + "'s balance is now $" + balance + ".");
    }

    
    /**
     * Helper to add the transaction history to the User's history
     *
     * @param message the message to be added to the User's history
     */
    public void addTranHis(String message){
        this.transactionHistory.add(message);
    }

    /**
     * Sets a transaction history line for our User
     *
     * @param trans the transaction to add to the user
     */
    public void setTransactionHistory(ArrayList<String> trans){
        this.transactionHistory = trans;
    }

    /**
     * Returns True if the amount of funds are avalible for the current User
     * @param amount the value of funds to check are present for our user
     * @return true if the amount is avalible false otherwise
     */
    protected boolean canTransferFunds(double amount){
        return this.accountBalance - amount >= MINFUNDS;
    }

    /**
     * Checks if the person's account will not maxout after addition of funds
     * @param amount the amount of funds to be added
     * @return true if the funds can be added false otherwise
     */
    protected boolean canAcceptFunds(double amount){
        return this.accountBalance + amount <= MAXFUNDS;
    }


    private boolean dailyLimitCheck(double amount){
        return this.NEWFUNDSTODAY + amount <= DAILYLIMIT;
    }
    
    /**
     * Transfer the requested funds to the user's account
     * Max out the funds if the addition of funds results in an overflow of funds
     *
     * @param amount The value of funds to be added
     */
    public void transferFunds(double amount){
        this.setAccountBalance(this.getAccountBalance() + amount);
        System.out.println("$" + amount + " added to " + this.username);
        System.out.println("Most updated account balance is $" + this.getAccountBalance());
    }

    /**
     * Adds the amount of funds to be added to the User's account and prints out the Status
     *
     *
     * @param amount The amount of funds to be added to the User's account
     */
    public void addCredit(double amount) {
        // check the constraints of daily limit
        if (this.dailyLimitCheck(amount)) {
            double fundsAdded;
            // check if the account will be maxed upon addition of funds
            if(this.canAcceptFunds(amount)){
                this.transferFunds(amount);
                this.newFunds += amount;
                fundsAdded = amount;
            }
            // Max out their account with the difference            @666 Piazza
            else{
                double newFunds = MAXFUNDS - this.getAccountBalance();
                this.setAccountBalance(MAXFUNDS);
                System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
                        "'s balance was Maxed out!\n$" + newFunds+ " were added to the account");
                this.newFunds += newFunds;
                fundsAdded = newFunds;
            }

            String tran  = "$"+ fundsAdded + " were added to the user's account";
            this.addTranHis(tran);
        }
        // Reject the transaction               @701 Piazza
        else {
            // get the amount that can be added
            double newFunds = DAILYLIMIT - this.newFunds;

            System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
                    "'s daily limit would be reached upon addition of funds!\n" +
                    "You can only add $" + newFunds+ " to the account for the rest of today.");

            // Add the difference to the account            For future improvements
            /*this.newFunds = DAILYLIMIT;
            // If the user's account will not be maxed out to add the funds
            if(this.canAcceptFunds(newFunds)){
                this.transferFunds(newFunds);
            }
            // Max out their account
            else{
                this.setAccountBalance(MAXFUNDS);
                System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
                        "'s balance is Maxed out!\n$" + newFunds+ " were added to the account");
            }*/
        }
    }



    /** Return true if the user is selling this game in the market.
     *
     * @param game Game to check for if user is selling in market
     * @return true if user selling game, else false
     */
    //helper for buy
    private boolean sellingGame(Game game, Marketplace market) {
        if (market.getGamesOnSale().containsKey(this.username)) { //user is selling a game in the mkt place
//            System.out.println( this.username + " in market");
            for (Game g : market.getGamesOnSale().get(this.username)) {
                if (g.getUniqueID() == game.getUniqueID()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Remove the amount from the user's account, if it does not reduce the user's balance to below the minimum amount.
     *
     * @param amount funds to be removed
     */
    //HELPER FOR BUY - I DON'T ACTUALLY USE THIS HELPER ANYMORE AND NOW THIS SEEMS A BIT REDUNDANT WE CAN
    // DELETE THIS HELPER LATER ON UNLESS SOMEONE ELSE USES IT
//    private void removeFunds(Float amount) {
//        if (this.canTransferFunds(amount)) {
//            this.accountBalance = this.accountBalance - (double) amount; //should this be a double? what happens here?
//        }
//    }

    /** Return true if the game is already in the user's inventory.
     *
     * @param game game to check for in inventory
     * @return true if game in inventory, else false
     */
    //HELPER FOR BUY
    protected boolean gameInInventory(Game game) {
        for (Game g : this.inventory) {
            if (g.getName().equals(game.getName())) {
                return true;
            }
        }
        return false;
    }

    /** Remove the amount price from account balance and add the game to inventory. Print out message with details.
     *
     * @param seller that game is bought from
     * @param price amount to be removed from account balance
     * @param game to be added to inventory
     */
    private void payAndAddGame(AbstractUser seller, double price, Game game) {
        this.accountBalance -= price;
        this.inventory.add(game);
        System.out.println(this.username + " has bought " + game.getName() + " from " + seller.getUsername() + " for "
                + price + ".");
    }

    /** Buy a game from a seller and add it to user's inventory, if the game is not already in the user's inventory.
     *
     * @param seller the supplier of the game
     * @param game the name of the game
     * @param saleToggle the status of Sale being on the market
     */


    public void buy(AbstractUser seller, Game game, boolean saleToggle, Marketplace market){
        if (!seller.sellingGame(game, market)) {
            System.out.println("ERROR: \\ < Failed Constraint: " + seller.getUsername() + " is not selling " +
                    game.getName() + " on the market.");
            return;
        }
        if (this.sellingGame(game, market)) {
            System.out.println("ERROR: \\ < Failed Constraint: " + this.getUsername() + "is selling " + game.getName()
                    + " on the market.");
            return;
        }
        if (gameInInventory(game)) { //check that game isn't already in inventory
            System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " already owns " + game.getName() +
                    ". Cannot buy it again.");
        }

        else {                                                  // Needs to be implemented in transferFunds()
            double price = game.getPriceWithDiscount(saleToggle);
            if (!this.canTransferFunds(price)) { //buyer does not have enough money
                System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have enough funds " +
                        "to buy " + game.getName() + ". ");
            }  // Needs to be implemented in transferFunds()
            else if (!seller.canAcceptFunds(price)) { //seller's account maxed out
                this.payAndAddGame(seller, price, game);
                seller.accountBalance = MAXFUNDS;
                System.out.println("Warning: "+ this.username +
                        "'s balance was maxed out upon sale of game.");
            }
            else { // make normal add and print message
                this.payAndAddGame(seller, price, game);
                seller.accountBalance += price;
                String transaction = "User: " + this.username + " bought " + game.getName() + " from "
                        + seller.getUsername();
                this.transactionHistory.add(transaction);
                System.out.println(transaction);
            }
        }
    }

    // I JUST PUT THIS TYPE SIGNATURE TO MAKE ANOTHER FUNCTION WORK YOU CAN EDIT IT LATER - bharathi

    /**
     * add game to market being sold by this AbstractUser if AbstractUser doesn't already have the game on market.
     *
     * @param game Game object being sold to the market
     * @param market Marketplace that will sell this game
     */
    public void sell(Game game, Marketplace market){

        // if game doesn't follow constraints end here
        if (!this.sellConstraints(game)) return;
        
        if (this.gameInInventory(game)) {
            System.out.println("ERROR: \\ < Failed Constraint: " + this.getUsername() + " could not sell " +
                    game.getName() + " as they have bought this exact game. >");
        }

        HashMap<String, ArrayList<Game>> map = market.getGamesOnSale(); // var for less typing
        // if user has previously put games on the market, add to list of games

        if (map.containsKey(this.username)) {
            map.get(this.username).add(game);
            this.transactionHistory.add("User: " + this.username + " is now selling " + game.getName() +
                    " for " + game.getPrice());
            System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
                    game.getPrice() + " at a " + game.getDiscount()+"% discount, will be available for purchase tomorrow.");

        } else {
            market.addNewSeller(this.username);

            // Report to console and transactionHistory
            this.transactionHistory.add("User: " + this.username + " is now selling " + game.getName() +
                    " for " + game.getPrice());
            System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
                    game.getPrice() + " at a " + game.getDiscount()+"% discount, will be available for purchase tomorrow.");
        }
    }

    /**
     * Return true if this is a valid game that can be sold. If invalid returns false and prints error to console.
     *
     * @param game, Game being sold.
     */
    private boolean sellConstraints(Game game) {

        String gameName = game.getName();
        String userName = this.getUsername();
        double gamePrice = game.getPrice();
        double gameDiscount = game.getDiscount();

        // check if game price is gt max game price
        double maxPrice = 999.99;
        if (gamePrice > maxPrice) {
            System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum sale price. > //");
            return false;
        }
        
        if (gamePrice < 0) {
            System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as the price cannot be negative. > //");
            return false;
        }
        
        // Check if game name is gt max name length
        int maxNameLength = 25;
        if (gameName.length() > maxNameLength) {
            System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum name length. > //");
            return false;
        }
        // Check if game discount is gt max discount amount
        double maxDiscount = 90;
        if (gameDiscount > maxDiscount) {
            System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
                    gameName + " with " + gameDiscount + "% discount as it exceeds the maximum discount " +
                    "amount. > //");
            return false;
        }

        // passes all checks / follows all constraints
        return true;
    }

    /**
     * Issues a refund and transfers the funds between the two user if the funds are avalible in the supplier's account
     * @param buyer the customer asking for the refund
     * @param seller the supplier of the games issueing the refund
     * @param amount the value of credits to be transfered among them
     * @return true if the refund was made false otherwise
     */
    public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to issue a refund.");
        return false;
    }

    public AbstractUser create(String username, String type, double credit){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to create " +
                "another user");
        return null;
    }

    /**
     * Helper to add the Gift being recieved, to the User's inventory
     *
     * @param gift Game to be added to the inventory
     */
    protected void addGame(Game gift){

        // get the inventory and add the game
        this.inventory.add(gift);

    }


    /**
     * Sends Games to a User if they can accept this Game
     * This method is used by Admin and Full-Standard User
     *
     * @param game a Game to be gifted
     * @param reciever person who will be recieving the Gift
     * @param market the current Market
     */
    public void gift(Game game, AbstractUser reciever, Marketplace market){
        // Reciever is a Sell user
        if (reciever instanceof SellUser){
            System.out.println("ERROR: \\< Failed Constraint: Sell User can not accept any gifts. >");
        }
        else{
            String gameName = game.getName();
            // check if the Receiver has the game in their inventory
            boolean inRecInv = !reciever.gameInInventory(game);
            // Check if the Receiver has the game up for Sale on the Market
            boolean inRecMar = !market.checkSellerSellingGame(reciever.getUsername(), gameName);

            // if the User can accept the game then check if the sender can send the game
            if(inRecInv && inRecMar){
                boolean inSenInv = this.gameInInventory(game);
                boolean inSenMar = market.checkSellerSellingGame(this.getUsername(), gameName);
                // User can send the gift, game is added to the Receiver's inventory
                if (inSenInv || inSenMar){
                    // Game needs to be removed from the sender's inventory
                    if(inSenInv){
                        this.removeFromInventory(gameName);
                    }
                    reciever.addGame(game);
                    // updating the transaction history for the users
                    String senderTran = this.getUsername() + " has gifted: " + gameName + " to " + reciever.getUsername();
                    String recTran = reciever.getUsername() + " has received " + gameName + " from " + this.getUsername();
                    this.addTranHis(senderTran);
                    reciever.addTranHis(recTran);

                }
                // Sender doesn't have the game
                else{
                    System.out.println("ERROR: \\" + this.username + " does not have the " + gameName +
                            ".\n Gift transaction failed.");
                }
            }
            // Receiver already has the game
            else{
                System.out.println("ERROR: \\" + reciever.getUsername() + " already has " +gameName+
                        ".\n Gift transaction failed.");
            }
        }
    }


    /**
     * Helper to remove the game from the User's inventory
     *
     * @param game remove the valid game title from the User's inventory
     */
    protected void removeFromInventory(String game){
        ArrayList<Game> currInv = this.inventory;
        // finding and setting the game to be removed
        Game currGame = null;
        for(Game curr : currInv){
            if(curr.getName().equals(game)){
                currGame = curr;
            }
        }
        //  removing the game
        this.inventory.remove(currGame);
    }


    /**
     * Checks and removes the game for the User
     * Method is used by Admin and Full-Standard
     *
     * @param game The game being removed
     * @param market The current market
     */
    public void removegame(Game game, Marketplace market){
        String currGame = game.getName();
        // check if the User is Selling the Game on the Market
        boolean iAmOffering = market.checkSellerSellingGame(this.getUsername(), currGame);
        boolean inMyInv = this.gameInInventory(game);
        // remove from Market
        if(iAmOffering){
            market.removeGame(this.getUsername(), currGame);
            String tran = currGame+ " was removed from the User's offering on the Market.";
            this.addTranHis(tran);
        }
        // remove from inventory
        else if (inMyInv){
            this.removeFromInventory(currGame);
            String tran = currGame+ " was removed from the User's inventory.";
            this.addTranHis(tran);
        }
        else if (!inMyInv){
            System.out.println(currGame+ " was not found in the User's inventory.");
        }
        // else printing out the error from Market for Game not being currently offered
    }

    /**
     * Given the UserID and account balance delete the user's account
     *
     *
     */
    public void delete(AbstractUser user, double amount){
        System.out.println("ERROR: \\< Failed Constraint: Current User is not allowed to delete someone's account. >//");

    }
    
    /** Prints that the user cannot implement an auction sale.
     * @param amount amount by which to reduce prices of games by.
     */
    public void auctionSale(double amount) {
        System.out.println("ERROR: \\< Failed Constraint: Current User: " + this.getUsername() +
                "is not allowed to toggle an auction sale. >//");


    }
}