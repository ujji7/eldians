package main;

import java.util.ArrayList;
/** This is a buy type user that extends the Abstract User class. A Buy type user cannot sell games, only buy. And
 * it cannot create or delete users.
 *
 */
public class BuyUser extends AbstractUser {

//    public BuyUser(String username) {
//        super(username);
//    }


//    public BuyUser(String username, Double credit) {
//        super(username);
//        this.accountBalance = credit;
//        this.type = "BS";
//    }

    private BuyUser(BuyUserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "BS";
        this.inventory = builder.inventory;

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

    public TransactionHistory getTransactionHistory(){
        return this.transactionHistory;
    }


    /** Prints that the user cannot sell a game.
     *
     * @param game game to be sold
     * @param market Marketplace game will be sold on
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT
    @Override
    public void sell(Game game, Marketplace market){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to sell games.");
    }

    /** Prints that the user cannot create a user
     *
     * @param username a string with a length: 1-15
     * @param type a string representing the User type of the newly created user
     *             where AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard
     * @param credit a float representing the amount of credits to add to the newly
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    @Override
    public void create(String username, String type, double credit){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to create another user.");
    }

    /**Prints that this user cannot delete another user.
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    @Override
    public void delete(AbstractUser user, double amount){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to delete another user.");
    }

    /** Prints that this user cannot issue a refund.
     *
     * @param buyer the customer asking for the refund
     * @param seller the supplier of the games issueing the refund
     * @param amount the value of credits to be transfered among them
     * @return
     */
    @Override
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to issue a refund.");
        return false;
    }

    public static class BuyUserBuilder {

        private String username; // required
        //        public String type;
        public double accountBalance;
        public ArrayList<Game> inventory;
        public double newFunds;
        public ArrayList<String> transactionHistory;

        public BuyUserBuilder(String name) {
            this.username = name;
            this.accountBalance = 0.00;
            this.transactionHistory = new ArrayList<>();
        }

        public BuyUserBuilder balance(double accountBalance){
            this.accountBalance = accountBalance;
            return this;
        }

        public UserBuilder inventoryGames(ArrayList<main.Game> inventory){
            this.inventory.addAll(inventory);
            return this;
        }

        public BuyUserBuilder newFunds(double newFunds){
            this.newFunds = newFunds;
            return this;
        }

        public BuyUserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        public BuyUser build() {
            BuyUser user = new BuyUser(this);
            return new BuyUser(this);
        }

    }

}