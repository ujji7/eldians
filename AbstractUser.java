import java.util.ArrayList;
import Application;
import Marketplace;

//we need an auction sale method - i implemented it at bottom check it out
// also look at readme for add credit - there is another implementations for admin type

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
    public static final double MAXFUNDS = 999999.99;
    // can change minFunds to allow overdrafts for future improvements
    public static final float MINFUNDS = 0;

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
     * @param amount The amount of funds to be added to the User's account
     */
    public void addCredit(float amount){
        //boolean result = true;
        if(this.canAcceptFunds(amount)){
            this.setAccountBalance(this.getAccountBalance() + amount);
            System.out.println("$" + amount + " added to" + this.username);
        }
        else {
            // ACCORDING TO PIAZZA max out the balance and prompt user (VERIFY IT doe)
            this.setAccountBalance(MAXFUNDS);
            System.out.println(this.username + " balance was Maxed up upon addition of more funds!");
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
    //HELPER FOR BUY
    private void removeFunds(Float amount) {
        if (this.canTransferFunds(amount)) {
            this.accountBalance = this.accountBalance - (double) amount;
        }
    }

    /** Buy a game from a seller and add it to user's inventory, if the game is not already in the user's inventory.
     *
     * @param seller the supplier of the game
     * @param game the name of the game
     * @param saleToggle the status of Sale being on the market
     */

    public void buy(AbstractUser seller, Game game, boolean saleToggle){
        if (!seller.sellingGame(game)) {  //check if seller is selling this game on market
            System.out.println(seller.getUsername() + "is not selling " + game.getName() + " on the market.");
        }
                            //------ CHECK NAMES as 2 suppliers with the same game will have diff Game objects but User
                            //-- will then be able to have 2 games of the same title -Uzair
        else if (this.inventory.contains(game)) { //check that game isn't already in inventory
            System.out.println(this.username + " already owns " + game.getName() + ". Cannot buy it again.");
        }
                            //--- Discount percentage is attribute of the game and the Sale toggle is the attribute of the Market
                            //-- Check discount toggle in the market, if yes price = Gameprice * (1-disCount)
        float price = game.getPrice();
        if (saleToggle) { //if the sale isn't 0 - check if auction sale is on
            price = (float)Math.round((price * (1 - game.getDiscount()))*100) / 100; //how to ensure decimal places remain at 2?
                                                                                    //---Done but verify it -Uzair
        }
        else if (!(this.canTransferFunds(price) && seller.canAcceptFunds(price))) { //not enough money
            System.out.println("There are not enough funds to be transferred");
        }
        else {
            this.removeFunds(price);
            seller.addCredit(price);
            this.inventory.add(game);
            System.out.println(this.username + " has bought " + game.getName() + " from " + seller.getUsername() + " for "
            + price + ".");
        }
    }

    // I JUST PUT THIS TYPE SIGNATURE TO MAKE ANOTHER FUNCTION WORK YOU CAN EDIT IT LATER
    public void sell(Game game){

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

    /** Prints that the user cannot implement an auction sale.
     * @param amount amount by which to reduce prices of games by.
     */
    public void auctionSale(float amount) {
        System.out.println(this.getUsername() + "cannot implement an auction sale.");
    }
}