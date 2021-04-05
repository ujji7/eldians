package main;
import transactions.Finder;

import java.util.ArrayList;
import java.util.HashMap;

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
    private final double newFundsToday = 0d;
    protected static final String FAIL_BEGIN = "ERROR: \\< Failed Constraint: ";
    protected static final String FAIL_END = ".\\>";

    
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
     * Returns True if the amount of funds are available for the current User
     * @param amount the value of funds to check are present for our user
     * @return true if the amount is available false otherwise
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
        return this.newFundsToday + amount <= DAILY_LIMIT;
    }
    
    /**
     * Transfer the requested funds to the user's account
     * Max out the funds if the addition of funds results in an overflow of funds
     *
     * @param amount The value of funds to be added
     */
    public void transferFunds(double amount){
        this.setAccountBalance(Math.round((this.getAccountBalance() + amount) * 100.00) / 100.00);
        System.out.println("$" + amount + " added to " + this.username + ".");
        System.out.println("Most updated account balance is $" + this.getAccountBalance() + ".");
    }

    /**
     * Adds the amount of funds to be added to the User's account and prints out the Status
     * @param amount The amount of funds to be added to the User's account
     */
    public void addCredit(double amount) {
        if (!(amount <= MIN_FUNDS)) {
            if (this.dailyLimitCheck(amount)) { // check the constraints of daily limit
                double fundsAdded;
                if (this.canAcceptFunds(amount)) { // check if the account will be maxed upon addition of funds
                    this.transferFunds(amount);
                    this.newFunds += amount;
                    fundsAdded = amount;
                }
                // Max out their account with the difference            @666 Piazza
                else {
                    double newFunds = (double) Math.round((MAX_FUNDS - this.getAccountBalance()) * 100) / 100;
                    this.setAccountBalance(MAX_FUNDS);
                    System.out.println(FAIL_BEGIN + this.username + "'s balance was Maxed out! $" + newFunds + " was " +
                            "added to their account" + FAIL_END);
                    this.newFunds += newFunds;
                    fundsAdded = newFunds;
                }
                String tran = "$" + fundsAdded + " was added to the user's account.";
                this.addTranHis(tran);
            }
            // Reject the transaction               @701 Piazza
            else { // get the amount that can be added
                double newFunds = (double) Math.round((DAILY_LIMIT - this.newFunds) * 100) / 100;
                System.out.println(FAIL_BEGIN + this.username +
                        "'s daily limit would be reached upon addition of funds!\n" +
                        "You can only add $" + newFunds + " to the account for the rest of today" + FAIL_END);
            }
        } else {
            System.out.println(FAIL_BEGIN +  "Amount should be greater than $0" + FAIL_END);
        }
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

    /** Return true if the game is on hold in the user's inventory.
     *
     * @param game game to check for in inventory
     * @return true if game in on hold in inventory, else false
     */
    protected boolean gameInventoryHold(Game game) {
        Game g = null;
        if (gameInInventory(game)) {
            Finder find = new Finder();
            g = find.findGame(game.getName(), this.inventory);
        }
        if (g != null) {
            return game.getHold();
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
        String gName = game.getName();
        String sName = seller.getUsername();
        String bName = this.username;
        if (!market.checkSellerSellingGame(sName, gName)) {
            System.out.println(FAIL_BEGIN + sName + " is not selling " + gName + " on the market. Cannot buy the " +
                    "game from them" + FAIL_END);
        } else if (!market.checkNotOnHold(sName, gName)) {
            System.out.println(FAIL_BEGIN + gName + "is currently on hold. Cannot be " + "purchased today" + FAIL_END);
        } else if (market.checkSellerSellingGame(bName, gName)) {
            System.out.println(FAIL_BEGIN + bName + "is selling " + gName + " on the market, cannot also buy it" + 
                    FAIL_END);
        } else if (gameInInventory(game)) { //check that game isn't already in inventory
            System.out.println(FAIL_BEGIN+ bName + " already owns " + gName + ". Can't buy it again" + FAIL_END);
        } else {                                                  
            double price = game.getPriceWithDiscount(saleToggle);
            if (!this.canTransferFunds(price)) { 
                System.out.println(FAIL_BEGIN + bName + " doesn't have enough funds to buy " + gName + FAIL_END);
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
            System.out.println(FAIL_BEGIN + this.getUsername() + " could not sell " +
                    game.getName() + " as they have bought this exact game" + FAIL_END);
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
                    game.getPrice() + " at a " + game.getDiscount()+"% discount, will be available for " +
                    "purchase tomorrow.");
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
        
        double maxPrice = 999.99; // check if game price is gt max game price
        if (gamePrice > maxPrice) {
            System.out.println(FAIL_BEGIN + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum sale price" + FAIL_END);
            return false;
        }
        if (gamePrice < 0) {
            System.out.println(FAIL_BEGIN + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as the price cannot be negative" + FAIL_END);
            return false;
        }
        int maxNameLength = 25; // Check if game name is gt max name length
        if (gameName.length() > maxNameLength) {
            System.out.println(FAIL_BEGIN + userName + " could not sell " +
                    gameName + " for $" + gamePrice + " as it exceeds the maximum name length" + FAIL_END);
            return false;
        }
        double maxDiscount = 90; // Check if game discount is gt max discount amount
        if (gameDiscount > maxDiscount) {
            System.out.println(FAIL_BEGIN + userName + " could not sell " +
                    gameName + " with " + gameDiscount + "% discount as it exceeds the maximum discount " +
                    "amount" + FAIL_END);
            return false;
        }
        return true; // passes all checks / follows all constraints
    }

    /**
     * Issues a refund and transfers the funds between the two user if the funds are available in the supplier's account
     * @param buyer the customer asking for the refund
     * @param seller the supplier of the games issuing the refund
     * @param amount the value of credits to be transferred among them
     * @return true if the refund was made false otherwise
     */
    public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
        System.out.println(FAIL_BEGIN+ this.username + " does not have the ability to issue " +
                "a refund" + FAIL_END);
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
        System.out.println(FAIL_BEGIN+ this.username + " does not have the ability to create " +
                "another user" + FAIL_END);
        return null;
    }

    /**
     * Helper to add the Gift being received, to the User's inventory
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
     * @param InGame a Game to be gifted
     * @param receiver person who will be receiving the Gift
     * @param market the current Market
     */
    public void gift(Game InGame, AbstractUser receiver, Marketplace market){
        if (receiver instanceof SellUser){
            System.out.println(FAIL_BEGIN + " Sell User can not accept any gifts" + FAIL_END);
            return;
        }
        // deep-copying the Game to work with
        Game game = this.gameCopy(InGame);
        // if the User can accept the game then check if the sender can send the game
        if (receiver.canAccept(game, market)) {
            // User can send the gift, game is added to the Receiver's inventory
            if (canSendGame(game, market)) {
                if(!gameInventoryHold(InGame)) {
                    // Game needs to be removed from the sender's inventory
                    if (this.gameInInventory(game)) {
                        this.removeFromInventory(game.getName());
                    }
                    sendGame(receiver, game);
                }
                else{
                    System.out.println(FAIL_BEGIN + this.username + " is currently not offering " +
                            game.getName() + " as Game is on Hold. Gift transaction failed" + FAIL_END);
                }
            }
            // Sender doesn't have the game
            else {
                System.out.println(FAIL_BEGIN + this.username + " is currently not offering " +
                        game.getName() + ". Gift transaction failed" + FAIL_END);
            }
        }
        // Receiver already has the game
        else {
            System.out.println(FAIL_BEGIN + receiver.getUsername() + " already has " +
                    game.getName() + ". Gift transaction failed" + FAIL_END);
        }
    }

    /**
     * Return true if receiver can accept this gift.
     *
     * @param gift game being gifted to this user.
     * @param market marketplace where user could be selling games.
     * @return true if game not in inventory or market, false otherwise.
     */
    protected boolean canAccept(Game gift, Marketplace market) {

        // check if the Receiver has the game in their inventory
        boolean inRecInv = this.gameInInventory(gift);

        // Check if the Receiver has the game up for Sale on the Market
        boolean inRecMar = market.checkSellerSellingGame(this.getUsername(), gift.getName());

        return !inRecInv && !inRecMar;
    }

    /**
     * Return true if user can send this gift.
     *
     * @param gift game being sent.
     * @param market marketplace where user could be selling games.
     * @return true if game in inventory or game on sale, false otherwise.
     */
    protected boolean canSendGame(Game gift, Marketplace market) {
        boolean inSenInv = this.gameInInventory(gift);
        boolean inSenMar = market.checkSellerSellingGame(this.getUsername(), gift.getName());
        boolean notOnHold = market.checkNotOnHold(this.getUsername(), gift.getName());
        if (inSenInv) {
            return true;
        } else if (inSenMar) {
            if (!notOnHold) {
                System.out.println(FAIL_BEGIN + gift.getName() + " cannot be gifted as " +
                        "it is still on hold" + FAIL_END);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper for gift, sends a game to the receiver and executes the proper prints to console and transHistory.
     *
     * @param receiver the person receiving the game.
     * @param gift the game being sent.
     */
    private void sendGame(AbstractUser receiver, Game gift) {
        receiver.addGame(gift);
        // updating the transaction history for the users
        String senderTran = this.getUsername() + " has gifted: " + gift.getName() + " to " +
                receiver.getUsername();
        String recTran = receiver.getUsername() + " has received " + gift.getName() + " from " +
                this.getUsername();
        this.addTranHis(senderTran);
        receiver.addTranHis(recTran);
        System.out.println(senderTran);
        System.out.println(recTran);
    }


    /**
     * Helper to remove the game from the User's inventory
     *
     * @param game remove the valid game title from the User's inventory
     * @return true if the game was removed, else false
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
        }
        return false;
    }


    /**
     * Checks and removes the game for the User
     * Method is used by Admin and Full-Standard
     *
     * @param inGame The game being removed
     * @param market The current market
     */

    public void removeGame(Game inGame, Marketplace market){
        Game game = this.gameCopy(inGame); // deep-copying the Game to work with
        String currGame = game.getName();
        boolean iAmOffering = market.checkSellerSellingGame(this.getUsername(), currGame); //check if user selling game
        boolean inMyInv = this.gameInInventory(game);
        if(iAmOffering){ // remove from Market
            boolean hold = market.checkNotOnHold(this.getUsername(), currGame);
            if (hold) {
                market.removeGame(this.getUsername(), currGame);
                String tran = currGame+ " was removed from " + this.username + "'s offering on the market.";
                this.addTranHis(tran);
                System.out.println(tran);
            }
            else {
                System.out.println(FAIL_BEGIN + currGame + " cannot be removed as it is on hold in the market" + FAIL_END);
            }
        }
        else if (inMyInv){ // remove from inventory
            boolean removed = this.removeFromInventory(currGame);
            if (removed) {
                String tran = currGame + " was removed from " + this.username + "'s inventory.";
                this.addTranHis(tran);
                System.out.println(tran);
            }
            else {
                System.out.println(FAIL_BEGIN + currGame + " cannot be removed as it is on hold in inventory" + FAIL_END);
            }
        }
        else {
            System.out.println(FAIL_BEGIN + currGame + " cannot be removed as it cannot be found" +  FAIL_END);
        }
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
        System.out.println(FAIL_BEGIN+ "Current User: " + this.getUsername() + 
                "is not allowed to toggle an auction sale" + FAIL_END);
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
