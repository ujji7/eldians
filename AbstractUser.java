import java.util.ArrayList;
import Application;
import Marketplace;
//I made in game that returns the price with discount applied since we'll probs need it in many places.

//we need an auction sale method - i implemented it at bottom check it out
// also look at readme for add credit - there is another implementations for admin type - DONE

//Back End Error Recording:
//        All recorded errors should be of the form: ERROR: \\<msg\\>
//
//        For failed constraint errors, <msg> should contain the type and description
//        of the error and the transaction that caused it to occur.
//        For fatal errors, <msg> should contain the type and description and the file
//        that caused the error.
/**
 * Abstract class for User objects.
 */
public class AbstractUser {
    public String username;
    public double accountBalance;
    public ArrayList<Game> inventory;
    public static final double MAXFUNDS = 999999.99f;
    // can change minFunds to allow overdrafts for future improvements
    public static final float MINFUNDS = 0f;
    public static final float DAILYLIMIT = 1000f;
    public static final float NEWFUNDSTODAY = 0f;

    public AbstractUser(String username){
        this.username = username;
        this.accountBalance = 0;
        this.inventory = new ArrayList<Game>();

    }

    /**
     * Sets the account balance for our User
     * @param balance the amount to balance to-be in the user's account
     */
    public void setAccountBalance(double balance){
        this.accountBalance = balance;
    }

    /**
     * Get the current User's unique username
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


    /**
     * Adds the amount of funds to be added to the User's account and prints out the Status
     *
     *                      ---BREAK IT INTO HELPER MAKE A HELPER TO ADD FUNDS
     *
     * @param amount The amount of funds to be added to the User's account
     */
    public void addCredit(float amount){
        //boolean result = true;
        if(this.canAcceptFunds(amount)){

            this.setAccountBalance(this.getAccountBalance() + amount); // this can be this.accountBalance += amount
            System.out.println("$" + amount + " added to" + this.username);
        }
        else {
            // ACCORDING TO PIAZZA max out the balance and prompt user (VERIFY IT doe)
            this.setAccountBalance(MAXFUNDS);
            System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
                    " balance was Maxed up upon addition of more funds!");
        }
        System.out.println("New account balance is $" + this.getAccountBalance());
        //return result;
    }

    /** Return true if the user is selling this game in the market.
     *
     * @param game Game to check for if user is selling in market
     * @return true if user selling game, else false
     */
    //helper for buy
    private boolean sellingGame(Game game) {
        if (Marketplace.getGamesOnSale().containsKey(this)) { //user is selling a game in the mkt place
            for (Game g : Marketplace.getGamesOnSale().get(this)) {
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
    private void removeFunds(Float amount) {
        if (this.canTransferFunds(amount)) {
            this.accountBalance = this.accountBalance - (double) amount; //should this be a double? what happens here?
        }
    }

    /** Return true if the game is already in the user's inventory.
     *
     * @param game game to check for in inventory
     * @return true if game in inventory, else false
     */
    //HELPER FOR BUY
    private boolean gameInInventory(Game game) {
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
    private void payAndAddGame(AbstractUser seller, float price, Game game) {
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

    public void buy(AbstractUser seller, Game game, boolean saleToggle){
        if (!seller.sellingGame(game)) {  //check if seller is selling this game on market - THIS CAN JUST BE IN MRKTPLC
            //marketplace.containsKey(seller) && marketplace.get(seller).get(game)
            System.out.println(seller.getUsername() + "is not selling " + game.getName() + " on the market.");
        }

        else if (gameInInventory(game)) { //check that game isn't already in inventory
            //this.inventory.contains(game)
            System.out.println(this.username + " already owns " + game.getName() + ". Cannot buy it again.");
        }

        else {
            float price = game.getPriceWithDiscount(saleToggle);
            if (!this.canTransferFunds(price)) { //buyer does not have enough money
                System.out.println(this.username + " does not have enough funds to buy " + game.getName() + ". ");
            }
            else if (!seller.canAcceptFunds(price)) { //seller's account maxed out
                this.payAndAddGame(seller, price, game);
                seller.accountBalance = MAXFUNDS;
            }
            else { // make normal add
                this.payAndAddGame(seller, price, game);
                seller.accountBalance += price;
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

        // if game doesn't follow contraints end here
        if (!this.sellConstraints(game)) return;

        map = market.gamesOnSale; // var for less typing
        // if user has previously put games on the market, add to list of games
        if (map.containsKey(this)) {
            map.get(this).add(game);
        } else {
            // Create a new ArrayList
            ArrayList<Game> gameList = new ArrayList<Game>();
            // Add game to the ArrayList
            gameList.add(game);
            // Insert the new Key-Value pairing to the market
            map.put(this, gameList);

            // Report to console (or Observer if implemented later)
            System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
                    game.getPrice() + " at a " + game.getDiscount+"% discount, will be availble for purchase tomorrow");
        }
    }

    /**
     * Return true if this is a valid game that can be sold. If invalid returns false and prints error to console.
     *
     * @param game, Game being sold.
     */
    private void sellConstraints(game) {
        // check if game price is gt max game price
        float maxPrice = 999.99f;
        if (game.getPrice() > maxPrice) {
            System.out.println("ERROR: \\ < Failed Constraint: " seller.getUsername() + " could not sell " +
                    game.getName() + " for $" + game.getPrice() + " as it exceeds the maximum sale price. > //");
            return false;
        }
        // Check if game name is gt max name length
        int maxNameLength = 25;
        if (game.getName().length > maxNameLength) {
            System.out.println("ERROR: \\ < Failed Constraint: " seller.getUsername() + " could not sell " +
                    game.getName() + " for $" + game.getPrice() + " as it exceeds the maximum name length. > //");
            return false;
        }
        // Check if game discount is gt max discount amount
        double maxDiscount = 90;
        if (game.getDiscount() > maxDiscount) {
            System.out.println("ERROR: \\ < Failed Constraint: " seller.getUsername() + " could not sell " +
                    game.getName() + " with " + game.getDiscount() + "% discount as it exceeds the maximum discount " +
                    "amount. > //");
            return false;
        }
        // If game is already on market, do not put another on market (end here)
        if (this.sellingGame(game)) {
            System.out.println("ERROR: \\ < Failed Constraint: " seller.getUsername() + " could not sell " +
                    game.getName() + " as User is already selling this exact game > //");
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
    public boolean refund(AbstractUser buyer, AbstractUser seller, float amount){
        boolean result = false;
        // need to check if the seller can transfer funds
        boolean canSendMoney = seller.canTransferFunds(amount);
        if(canSendMoney) {
            // remove the credits from the seller's account
            seller.addCredit(-amount);
            // add the funds regardless of maxing out
            buyer.addCredit(amount);
            result = true;
            System.out.println(seller.getUsername() + " made a refund to "
                    + buyer.getUsername() + " for $" + amount);
        }
        // buyer unable to transfer funds
        else{
            System.out.println("ERROR: \\ < Failed Constraint: " + seller.getUsername() + " could not make a refund to " +
                    buyer.getUsername() + " for $" + amount + " due to insufficient funds. > //");
        }
        return result;

    }

    /**
     * creates a new user of given type and adds them to the Application userList
     * @param username a string with a length: 1-15
     * @param type a string representing the User type of the newly created user
     *             where AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard
     * @param credit a float representing the amount of credits to add to the newly
     *               created user's account balance
     */
    public void create(String username, String type, Float credit){
        if(MINFUNDS <= credit || credit <= MAXFUNDS){
            switch (type) {
                case 'AA':
                    AdminUser newUser = new AdminUser(username, credit);
                    break;
                case 'FS':
                    FullStandardUser newUser = new FullStandardUser(username, credit);
                    break;
                case 'BS':
                    BuyUser newUser = new BuyUser(username, credit);
                    break;
                case 'SS':
                    SellUser newUser = new SellUser(username, credit);
                    break;
            }
            if(!Application.userList.contains(newUser)) {
                Application.addUser(newUser);
                System.out.println("A new user was created: \n" + newUser.toString());
            }
            System.out.println("ERROR: \\< Failed Constraint: New User could not be created since" +
                    "a User already exists with given name. >//");
            }
        System.out.println("ERROR: \\< Failed Constraint: New User could not be created since "
        + Float.toString(credit) + "amount is invalid. >//");

    }

    public void delete(){

    }

    /**
     * Returns True if the amount of funds are avalible for the current User
     * @param amount the value of funds to check are present for our user
     * @return true if the amount is avalible false otherwise
     */
    private boolean canTransferFunds(float amount){
        return this.accountBalance - amount >= MINFUNDS;
    }

    /**
     * Checks if the person's account will not maxout after addition of funds
     * @param amount the amount of funds to be added
     * @return true if the funds can be added false otherwise
     */
    private boolean canAcceptFunds(float amount){
        return this.accountBalance + amount <= MAXFUNDS;
    }


    private boolean dailyLimitCheck(float amount){
        return this.NEWFUNDSTODAY + amount <= DAILYLIMIT;
    }


    /** Prints that the user cannot implement an auction sale.
     * @param amount amount by which to reduce prices of games by.
     */
    public void auctionSale(float amount) {
        System.out.println(this.getUsername() + "cannot implement an auction sale.");
    }
}