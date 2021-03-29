package main;

import java.util.ArrayList;

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

    public TransactionHistory getTransactionHistory(){
        return this.transactionHistory;
    }

    /** Add credit to an account.
     *
     * @param amount The amount of funds to be added to the User's account
     */

    public void addCreditTo(double amount, AbstractUser user) {
        user.addCredit(amount);
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

    public static class UserBuilder {

        public String username; //mandatory
        public double accountBalance; // optional
        public ArrayList<main.Game> inventory; //optional
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

        public UserBuilder inventoryGames(ArrayList<main.Game> inventory){
            this.inventory.addAll(inventory);
            return this;
        }

        public UserBuilder newFunds(double newFunds){
            this.newFunds = newFunds;
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