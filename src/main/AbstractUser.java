package main;
import java.util.ArrayList;
import java.util.HashMap;
//line 401
//REMOVE ANY UNNECESSARY PRINT STATEMENTS IN CREATE AND BUY

//we need an auction sale method - i implemented it at botfitom check it out
// also look at readme for add credit - there is another implementations for admin type


//I made in game that returns the price with discount applied since we'll probs need it in many places.


//Back End Error Recording:
//        All recorded errors should be of the form: ERROR: \\<msg\\>
//
//        For failed constraint errors, <msg> should contain the type and description
//        of the error and the transaction that caused it to occur.
//        For fatal errors, <msg> should contain the type and description and the file
//        that caused the error.
/**
 * Abstract class for User objects. There are 4 types of users: Admin, Buy, Sell, Full Standard. Each user has a 
 * username, a type, an account balance, a transaction history, and an inventory of games they have bought.
 *                                     
 */
public abstract class AbstractUser {

    protected String username;
    protected String type;
    protected double accountBalance = 0;
    protected ArrayList<Game> inventory = new ArrayList<Game>();
    protected double newFunds = 0;
    public ArrayList<String> transactionHistory = new ArrayList<String>();
    protected static final double MAX_FUNDS = 999999.99d;
    // can change minFunds to allow overdrafts for future improvements
    protected static final double MIN_FUNDS = 0d;
    protected static final double DAILY_LIMIT = 1000d;
    private static final double NEW_FUNDS_TODAY = 0d;

    
    /** Get the current User's unique username
     * @return username String
     */
    public String getUsername(){
        return this.username;
    }

    /** Get the account balance for the user
     * @return the current account balance of the user
     */
    public double getAccountBalance(){
        return this.accountBalance;
    }


    /** Get the user's type
     * @return the user's type
     */
    public String getType(){
        return this.type;
    }

    /** Get the user's inventory - games they have bought
     * @return the user's inventory of games
     */
    public ArrayList<Game> getInventory(){
        return this.inventory;
    }


    /** Get the user's transaction history of all the transactions they have completed.
     * @return the user's transaction history
     */
    public ArrayList<String> getTransactionHistory(){
        
        return this.transactionHistory;
    }

    
    /** Sets the account balance for our User.
     * @param balance the amount to balance to-be in the user's account
     */
    public void setAccountBalance(double balance){

        this.accountBalance = balance;
        this.transactionHistory.add("User: " + this.username + "'s balance is now $" + balance + ".");
    }

    
    /** Helper to add the transaction history to the User's history
     * @param message the message to be added to the User's history
     */
    public void addTranHis(String message){
        this.transactionHistory.add(message);
    }

    /** Sets a transaction history line for our User
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
        return this.accountBalance - amount >= MIN_FUNDS;
    }

    /**
     * Checks if the person's account will not maxout after addition of funds
     * @param amount the amount of funds to be added
     * @return true if the funds can be added false otherwise
     */
    protected boolean canAcceptFunds(double amount){
        return this.accountBalance + amount <= MAX_FUNDS;
    }


    /** Return true if adding amount to our funds does not exceed the daily limit
     * @param amount to add to our account balance
     * @return true if amount can be added without exceeding the daily limit
     */
    private boolean dailyLimitCheck(double amount){
        return this.NEW_FUNDS_TODAY + amount <= DAILY_LIMIT;
    }
    
    /**
     * Transfer the requested funds to the user's account
     * Max out the funds if the addition of funds results in an overflow of funds
     *
     * @param amount The value of funds to be added
     */
    public void transferFunds(double amount){
        this.setAccountBalance(Math.round((this.getAccountBalance() + amount) * 100.00) / 100.00);
        System.out.println("$" + amount + " added to " + this.username);
        System.out.println("Most updated account balance is $" + this.getAccountBalance());
    }

    /**
     * Adds the amount of funds to be added to the User's account and prints out the Status
     * @param amount The amount of funds to be added to the User's account
     */
    public void addCredit(double amount) {
        if (!(amount <= MIN_FUNDS)) {
            if (this.dailyLimitCheck(amount)) { // check the constraints of daily limit
                double fundsAdded;
                // check if the account will be maxed upon addition of funds
                if (this.canAcceptFunds(amount)) {
                    this.transferFunds(amount);
                    this.newFunds += amount;
                    fundsAdded = amount;
                }
                // Max out their account with the difference            @666 Piazza
                else {
                    double newFunds = (double) Math.round((MAX_FUNDS - this.getAccountBalance()) * 100) / 100;
                    this.setAccountBalance(MAX_FUNDS);
                    System.out.println("ERROR: \\<Failed Constraint: " + this.username +
                            "'s balance was Maxed out!\n$" + newFunds + " was added to the account.\\>");
                    this.newFunds += newFunds;
                    fundsAdded = newFunds;
                }

                String tran = "$" + fundsAdded + " was added to the user's account";
                this.addTranHis(tran);
            }
            // Reject the transaction               @701 Piazza
            else {
                // get the amount that can be added
                double newFunds = (double) Math.round((DAILY_LIMIT - this.newFunds) * 100) / 100;

                System.out.println("ERROR: \\<Failed Constraint: " + this.username +
                        "'s daily limit would be reached upon addition of funds!\n" +
                        "You can only add $" + newFunds + " to the account for the rest of today.\\>");
            }
        }
        else{
        System.out.println("ERROR: \\<Failed Constraint: Amount should be greater than $0.\\>");
        }
    }

    
    /** Return true if the user is selling this game in the market.
     *
     * @param game Game to check for if user is selling in market
     * @return true if user selling game, else false
     */
    private boolean sellingGame(Game game, Marketplace market) {
        if (market.checkSellerExist(this.username)) { //user is selling a game in the mkt place
            for (Game g : market.getGamesOnSale().get(this.username)) {
                if (g.getName() == game.getName()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return true if the game is already in the user's inventory.
     *
     * @param game game to check for in inventory
     * @return true if game in inventory, else false
     */
    protected boolean gameInInventory(Game game) {
        for (Game g : this.inventory) {
            if (g.getName().equals(game.getName())) {
                return true;
            }
        }
        return false;
    }

    /** Remove the amount price from account balance, add the game to inventory, add transaction to history and return 
     * the transaction.
     *  @param seller that game is bought from
     * @param price amount to be removed from account balance
     * @param game to be added to inventory
     * @return username + " has bought " + gameName + " from " + sellerName + " for $" + price + "."
     */
    private String payAndAddGame(AbstractUser seller, double price, Game game) {
        this.accountBalance -= price;
        Game copy = gameCopy(game);
        this.inventory.add(copy);
        String trans = this.username + " has bought " + game.getName() + " from " + seller.getUsername() + " for $"
                + price + ".";
        this.addTranHis(trans);
        return trans;
    }

    /** Buy a game from a seller and add it to user's inventory, if the game is not already in the user's inventory.
     *
     * @param seller the supplier of the game
     * @param game the name of the game
     * @param saleToggle the status of Sale being on the market
     * @param market the market the user is buying the game from
     */
    public void buy(AbstractUser seller, Game game, boolean saleToggle, Marketplace market){
        if (!seller.sellingGame(game, market)) {
            System.out.println("ERROR: \\<Failed Constraint: " + seller.getUsername() + " is not selling " +
                    game.getName() + " on the market.\\>");
        } else if (game.getHold()) {
            System.out.println("ERROR: \\<Failed Constraint: " + game.getName() + "is currently on hold. Cannot be " +
                    "purchased today.\\>");
        } else if (this.sellingGame(game, market)) {
            System.out.println("ERROR: \\<Failed Constraint: " + this.getUsername() + "is selling " + game.getName()
                    + " on the market, cannot buy it as well.\\>");
        } else if (gameInInventory(game)) { //check that game isn't already in inventory
            System.out.println("ERROR: \\<Failed Constraint: "+ this.username + " already owns " + game.getName() +
                    ". Cannot buy it again.\\>");
        } else {                                                  
            double price = game.getPriceWithDiscount(saleToggle);
            if (!this.canTransferFunds(price)) { 
                System.out.println("ERROR: \\< Failed Constraint: "+ this.username + " does not have enough funds " +
                        "to buy " + game.getName() + ".\\>");
                return;
            }  
            System.out.println(this.payAndAddGame(seller, price, game));
            if (!seller.canAcceptFunds(price)) { //seller's account maxed out
                seller.accountBalance = MAX_FUNDS;
                System.out.println("Warning: "+ this.username + "'s balance was maxed out upon sale of game.");
            }
            else { 
                seller.accountBalance += price;
            }
        }
    }
    
    /**
     * add game to market being sold by this AbstractUser if AbstractUser doesn't already have the game on market.
     *
     * @param game Game object being sold to the market
     * @param market Marketplace that will sell this game
     */
    public boolean sell(Game game, Marketplace market){

        // if game doesn't follow constraints end here
        if (!this.sellConstraints(game)) return false;
        
        if (this.gameInInventory(game)) {
            System.out.println("ERROR: \\<Failed Constraint: " + this.getUsername() + " could not sell " +
                    game.getName() + " as they have bought this exact game.\\>");
            return false;
        }

        HashMap<String, ArrayList<Game>> map = market.getGamesOnSale(); // var for less typing

        // if user has previously put games on the market, add to list of games
        if (map.containsKey(this.username)) {
            map.get(this.username).add(game);
            this.transactionHistory.add("User: " + this.username + " is now selling " + game.getName() +
                    " for " + game.getPrice());
            System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
                    game.getPrice() + " at a " + game.getDiscount()+"% discount, will be available for purchase " +
                    "tomorrow.");

        } else {
            market.addNewSeller(this.username);
            map.get(this.username).add(game);
            // Report to console and transactionHistory
            this.transactionHistory.add("User: " + this.username + " is now selling " + game.getName() +
                    " for " + game.getPrice());
            System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
                    game.getPrice() + " at a " + game.getDiscount()+"% discount, will be available for purchase tomorrow.");
        }
        return true;
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
            System.out.println("ERROR: \\<Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum sale price.\\>");
            return false;
        }
        if (gamePrice < 0) {
            System.out.println("ERROR: \\<Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as the price cannot be negative.\\>");
            return false;
        }
        // Check if game name is gt max name length
        int maxNameLength = 25;
        if (gameName.length() > maxNameLength) {
            System.out.println("ERROR: \\<Failed Constraint: " + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum name length.\\>");
            return false;
        }
        // Check if game discount is gt max discount amount
        double maxDiscount = 90;
        if (gameDiscount > maxDiscount) {
            System.out.println("ERROR: \\<Failed Constraint: " + userName + " could not sell " +
                    gameName + " with " + gameDiscount + "% discount as it exceeds the maximum discount " +
                    "amount.\\>");
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
        System.out.println("ERROR: \\<Failed Constraint: "+ this.username + " does not have the ability to issue " +
                "a refund.\\>");
        return false;
    }

    /** Prints to the console that this user does not have the ability to create another user.
     * 
     * @param username name of the user to be created
     * @param type type of the user to be created
     * @param credit account balance to open 
     * @return null
     */
    public AbstractUser create(String username, String type, double credit){
        System.out.println("ERROR: \\<Failed Constraint: "+ this.username + " does not have the ability to create " +
                "another user.\\>");
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
     * @param INgame a Game to be gifted
     * @param reciever person who will be recieving the Gift
     * @param market the current Market
     */
    public void gift(Game INgame, AbstractUser reciever, Marketplace market){
        // Receiver is a Sell user
        if (reciever instanceof SellUser){
            System.out.println("ERROR: \\<Failed Constraint: Sell User can not accept any gifts.\\>");
        }
        else{
            // deep-copying the Game to work with
            Game game = this.gameCopy(INgame);
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
                    String senderTran = this.getUsername() + " has gifted: " + gameName + " to " +
                            reciever.getUsername();
                    String recTran = reciever.getUsername() + " has received " + gameName + " from " +
                            this.getUsername();
                    this.addTranHis(senderTran);
                    reciever.addTranHis(recTran);
                    System.out.println(senderTran);
                    System.out.println(recTran);
                }
                // Sender doesn't have the game
                else{
                    System.out.println("ERROR: \\<Failed Constraint: " + this.username + " does not have the " +
                            gameName + ". Gift transaction failed.\\>");
                }
            }
            // Receiver already has the game
            else{
                System.out.println("ERROR: \\<Failed Constraint" + reciever.getUsername() + " already has " +gameName+
                        ". Gift transaction failed.\\>");
            }
        }
    }


    /**
     * Helper to remove the game from the User's inventory
     *
     * @param game remove the valid game title from the User's inventory
     */
    protected boolean removeFromInventory(String game){
        ArrayList<Game> currInv = this.inventory;
        // finding and setting the game to be removed
        Game currGame = null;
        for(Game curr : currInv){
            if(curr.getName().equals(game)){
                if (!curr.getHold()) {
                    currGame = curr;
                } 
                else {
                    System.out.println(game + " cannot be removed as it is on hold.");
                }
            }
        }
        if (currGame != null) {
            this.inventory.remove(currGame); //  removing the game
            return true;
//            System.out.println(game + " has been removed from the user's inventory.");
        }
        return false;
    }


    /**
     * Checks and removes the game for the User
     * Method is used by Admin and Full-Standard
     *
     * @param INgame The game being removed
     * @param market The current market
     */

    public void removeGame(Game INgame, Marketplace market){
        // deep-copying the Game to work with
        Game game = this.gameCopy(INgame);
        String currGame = game.getName();
        // check if the User is Selling the Game on the Market
        boolean iAmOffering = market.checkSellerSellingGame(this.getUsername(), currGame);
        boolean inMyInv = this.gameInInventory(game);
        // remove from Market
        if(iAmOffering){
            market.removeGame(this.getUsername(), currGame);
            String tran = currGame+ " was removed from the User's offering on the Market.";
            this.addTranHis(tran);
            System.out.println(tran);
        }
        // remove from inventory
        else if (inMyInv){
            this.removeFromInventory(currGame);
            String tran = currGame+ " was removed from the user's inventory.";
            this.addTranHis(tran);
            System.out.println(tran);
        }
        else if (!inMyInv){
            System.out.println(currGame+ " was not found in the User's inventory.");
        }
        else {
            System.out.println("Seller: "+ this.getUsername() +" is currently not offering "+ currGame + ".");
        }
        // else printing out the error from Market for Game not being currently offered
    }

    /** Prints that this user cannot delete another user
     * Given the UserID and account balance delete the user's account
     *
     *
     */
    public void delete(AbstractUser user, double amount){
        System.out.println("ERROR: \\<Failed Constraint: Current User is not allowed to delete someone's account." +
                "\\>");

    }
    
    /** Prints that the user cannot implement an auction sale.
     */
    public void auctionSale() {
        System.out.println("ERROR: \\<Failed Constraint: Current User: " + this.getUsername() +
                "is not allowed to toggle an auction sale.\\>");
    }

    /**
     * For a valid existing game object create and return a deep copy of the Game
     *
     * @param game Game name
     * @return a deep copied Game
     */
    protected Game gameCopy(Game game) {
        Game gameCopy = new Game(game.getName(), game.getPrice(), game.getSupplierID(), game.getUniqueID(),
                game.getDiscount());
        return gameCopy;
    }
}
