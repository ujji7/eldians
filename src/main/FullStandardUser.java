package main;

import java.util.ArrayList;

/** Full standard type user that extends the Abstract User class. A ful standard user can both buy and sell games.And
 * it cannot create or delete users.
 *
 */
public class FullStandardUser extends AbstractUser {


//    public FullStandardUser(String username, double credit) {
//        super(username);
//        this.accountBalance = credit;
//        this.type = "FS";
//    }

    public FullStandardUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "FS";
        this.inventory = builder.inventory;
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


    public static class UserBuilder {

        public String username; //mandatory
        public String type; // mandatory
        public double accountBalance; // optional
        public ArrayList<Game> inventory = new ArrayList<Game>(); //optional
        public double newFunds; //optional
        public ArrayList<String> transactionHistory; //optional


        public UserBuilder(String name) {
            this.username = name;
            this.type = type;
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

        public UserBuilder newFunds(double newFunds){
            this.newFunds = newFunds;
            return this;
        }

        public UserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        public FullStandardUser build(){
            FullStandardUser user = new FullStandardUser(this);
            return user;
        }
    }
}