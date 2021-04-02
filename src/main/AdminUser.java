package main;

import java.util.ArrayList;

//CREATE METHOD DOES NOT PROPERYL CHECK FOR ALREADY USER WITH THE GIVEN USERNAME EXISTS
/** Admin type user that extends the Abstract User class. An admin has the highest privileges.
 *
 */
public class AdminUser extends AbstractUser {


//    public AdminUser(String username, double credit) {
//        super(username);
//        this.accountBalance = credit;
//        this.type = "AA";
//    }

    public AdminUser(UserBuilder builder) {
        this.username = builder.username;
        this.type = "AA";
        this.inventory = builder.inventory;
        this.accountBalance = builder.accountBalance;
        this.transactionHistory = builder.transactionHistory;
    }

    public String getUsername(){
        return this.username;
    }

    public String getType(){
        return this.type;
    }

    public ArrayList<Game> getInventory(){
        return this.inventory;
    }

    public double getAccountBalance(){
        return this.accountBalance;
    }

    public ArrayList<String> getTransactionHistory(){
        return this.transactionHistory;
    }

    /** Add credit to an account.
     *
     * @param amount The amount of funds to be added to the User's account
     */

    public void addCreditTo(double amount, AbstractUser user) {
        user.addCredit(amount);
    }



    /**
     * creates a new user of given type and adds them to the Application userList
     * @param username a string with a length: 1-15
     * @param type a string representing the User type of the newly created user
     *             where AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard
     * @param credit a float representing the amount of credits to add to the newly
     *               created user's account balance
     */
    @Override
    public AbstractUser create(String username, String type, double credit){
        if(MINFUNDS <= credit || credit <= MAXFUNDS){
            AbstractUser newUser;
            switch (type) {
                case "AA":
                    AdminUser.UserBuilder AAbuilder = new AdminUser.UserBuilder(username);
                    AAbuilder.balance(credit);
                    newUser = AAbuilder.build();
                    break;
                case "FS":
                    FullStandardUser.UserBuilder FSbuilder = new FullStandardUser.UserBuilder(username);
                    FSbuilder.balance(credit);
                    newUser = FSbuilder.build();
                    break;
                case "BS":
                    BuyUser.UserBuilder BSbuilder = new BuyUser.UserBuilder(username);
                    BSbuilder.balance(credit);
                    newUser = BSbuilder.build();
                    break;
                case "SS":
                    SellUser.UserBuilder SSbuilder = new SellUser.UserBuilder(username);
                    SSbuilder.balance(credit);
                    newUser = SSbuilder.build();
                    break;
                default:
                    // if user isn't initialized we stop the create function
                    System.out.println("ERROR: \\< Failed Constraint: New User could not be created since user type " +
                            "does not exist. > //");
                    return null;
            }
            if(!newUser.getUsername().equals("")) {
                this.transactionHistory.add("User: " + this.username + " has created user " +
                        newUser.getUsername());
                System.out.println("A new user was created: \n" + username); //+ newUser.toString()
//                System.out.println("new user name is: " + username);
                return newUser;
            }
            System.out.println("ERROR: \\< Failed Constraint: New User could not be created since" +
                    " a User already exists with given name. >//");
            System.out.println("baby is alive: " + username);
        }
        System.out.println("ERROR: \\< Failed Constraint: New User could not be created since "
                + Double.toString(credit) + " amount is invalid. >//");

        return null;
    }

    /** If there is currently no reduced price on games, turn on a sale for this amount. Else, turn off the sale.
     *
     *
     *                  ---- WITH THE NEW UNDERSTANDING according to Piazza @692
     *                  ---- THIS will be implemented in Applicaiton/MarketPlace
     *                  ---- The setting up of Sale percentage is completed and done in Game
     *
     * @param amount amount by which to reduce prices of games by.
     */
//    @Override
//    public void auctionSale(float amount) {
//        if (Marketplace.getAuctionSale() = 0.00f) {//this should be .equals(00.0) not setting
//            Marketplace.AuctionSale() = amount;
//        } else {
//            Marketplace.getAuctionSale() = 0.00f;
//        }
//    }

    /**
     * Method to send a game gift between users
     *
     *
     * @param game A game to be sent
     * @param sender A user sending the Gift
     * @param reciever A user to recieve the Gift
     * @param market The current Market
     */
    public void gift(Game game, AbstractUser sender, AbstractUser reciever, Marketplace market){
        // Attempting the gift transaction
        sender.gift(game, reciever, market);
    }


    /**
     * Checks and removes the game for the requested User
     * Method is used by Admin when a valid Username is provided
     *
     * @param game Game to be removed
     * @param owner Owner of the Game
     * @param market The current Market
     */
    public void removegame(Game game, AbstractUser owner, Marketplace market){
        // Attempting the remove transaction for the User
        owner.removegame(game, market);
    }



    /**
     * Issues a refund and transfers the funds between the two user if the funds are avalible in the supplier's account
     * @param buyer the customer asking for the refund
     * @param seller the supplier of the games issueing the refund
     * @param amount the value of credits to be transfered among them
     * @return true if the refund was made false otherwise
     */
    public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
        boolean result = false;
        // need to check if the seller can transfer funds an buyer can accept the funds
        boolean canSendMoney = seller.canTransferFunds(amount);
        boolean canRecieveMoney = buyer.canAcceptFunds(amount);
        if(canSendMoney && canRecieveMoney){
            // remove the credits from the seller's account and add it to the buyer's
            seller.transferFunds(-amount);
            buyer.transferFunds(amount);
            result = true;
            System.out.println(seller.getUsername() + " made a refund to "
                    + buyer.getUsername() + " for $" + amount);
        }
        // failed to issue refund
        else{
            // Seller unable to transfer the funds
            if(!canSendMoney){
                System.out.println("ERROR: \\ < Failed Constraint: " + seller.getUsername() + " could not make a refund to " +
                        buyer.getUsername() + " for $" + amount + " due to insufficient funds. > //");
            }
            // Buyer unable to accept the funds
            else {
                System.out.println("ERROR: \\ < Failed Constraint: " + buyer.getUsername() + " could not accept a refund from " +
                        seller.getUsername() + " for $" + amount + " due to account maxing out upon addition of funds. > //");
            }
        }
        return result;

    }




    public static class UserBuilder {

        public String username; //mandatory
        public double accountBalance; // optional
        public ArrayList<Game> inventory = new ArrayList<Game>(); //optional
        public double newFunds; //optional
        public ArrayList<String> transactionHistory; //optional


        public UserBuilder(String name) {
            this.username = name;
            this.accountBalance = 0.00;
            this.transactionHistory = new ArrayList<>();
        }

        public UserBuilder balance(double accountBalance){
            this.accountBalance = accountBalance;
            return this;
        }

        public UserBuilder inventoryGames(ArrayList<Game> inventory){
            this.inventory.addAll(inventory);
            return this;
        }


        public UserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        public AdminUser build(){
            return new AdminUser(this);
        }
    }
}