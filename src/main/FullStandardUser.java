package main;

import java.util.ArrayList;

/** Full standard type user that extends the Abstract User class. A ful standard user can both buy and sell games.And
 * it cannot create or delete users.
 *
 */
public class FullStandardUser extends AbstractUser {

    
    /** Construct a buy user using the builder attributes
     * @param builder User builder that builds the buy user
     */
    public FullStandardUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "FS";
        this.inventory = builder.inventory;
        this.transactionHistory = builder.transactionHistory;

    }


    /** User Builder class to build a Full Standard type user. Mandatory attribute is the name.
     *
     */
    public static class UserBuilder {

        private String username; // required
        public double accountBalance;
        public ArrayList<Game> inventory = new ArrayList<Game>();
        public ArrayList<String> transactionHistory;
        
        /** Initialize a user builder with the given name.
         *
         * @param name of the user
         */
        public UserBuilder(String name) {
            this.username = name;
            this.accountBalance = 0.00;
            this.transactionHistory = new ArrayList<>();
        }

        /** Set the builder's account balance to account balance
         *
         * @param accountBalance the balance to set the builder at
         * @return the user builder
         */
        public UserBuilder balance(double accountBalance){
            this.accountBalance = accountBalance;
            return this;
        }

        /** Set the builder's inventory with the given inventory
         *
         * @param inventory the inventory to set the builder at
         * @return the user builder
         */
        public UserBuilder inventoryGames(ArrayList<Game> inventory){
            this.inventory.addAll(inventory);
            return this;
        }

        /** Set the builder's transactionHistory with the given transactionHistory
         *
         * @param transactions the transactionHistory to set the builder at
         * @return the user builder
         */
        public UserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        /** Build and return the Full Standard User
         *
         * @return FullStandardUser object with the builder's attributes
         */
        public FullStandardUser build(){
            return new FullStandardUser(this);
        }
    }
}