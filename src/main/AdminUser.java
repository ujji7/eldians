package main;

import java.util.ArrayList;

/** Admin type user that extends the Abstract User class. An admin has the highest privileges. Admin can create, delete 
 * users and issue refunds to accounts. They can also add credit to any account and gift games between users. They can 
 * also remove games from other user's inventories.
 *
 */
public class AdminUser extends AbstractUser {


    /** Construct an admin user using the builder attributes
     * @param builder User builder that builds the admin user
     */
    public AdminUser(UserBuilder builder) {
        this.username = builder.username;
        this.type = "AA";
        this.inventory = builder.inventory;
        this.accountBalance = builder.accountBalance;
        this.transactionHistory = builder.transactionHistory;
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
        if(MIN_FUNDS <= credit || credit <= MAX_FUNDS){
            AbstractUser newUser;
            switch (type) {
                case "AA":
                    newUser = new AdminUser.UserBuilder(username).balance(credit).build();
                    break;
                case "FS":
                    newUser = new FullStandardUser.UserBuilder(username).balance(credit).build();
                    break;
                case "BS":
                    newUser = new BuyUser.UserBuilder(username).balance(credit).build();
                    break;
                case "SS":
                    newUser = new SellUser.UserBuilder(username).balance(credit).build();
                    break;
                default: // if user isn't initialized we stop the create function
                    System.out.println(FAIL_BEGIN + this.username + " could not be created since " +
                            "user type does not exist" + FAIL_END);
                    return null;
            }
            if(!newUser.getUsername().equals("")) {
                this.transactionHistory.add("User: " + this.username + " has created user " + newUser.getUsername());
                System.out.println("A new user was created: \n" + username);
                return newUser;
            }
        }
        System.out.println(FAIL_BEGIN + "user " + username + " could not be created since "
                + Double.toString(credit) + " amount is invalid" + FAIL_END);
        return null;
    }

    /** Delete the user from the system and print to the console stating that
     * 
     * @param user to be deleted
     * @param credit account balance of the user to be deleted
     */
    @Override
    public void delete(AbstractUser user, double credit){
        this.transactionHistory.add(this.username + "deleted: " + user.getUsername() + "from the Eldians Application");
        System.out.println("User: " + user.getUsername() + ", deleted successfully.");
    }

    /** Implement an auction sale. If the sale was off before, turn it on and vice versa
     * 
     */
    @Override
    public void auctionSale(){
        this.transactionHistory.add(this.username + " toggled an Auction Sale in the Eldians Marketplace.");
        System.out.println(this.username + " toggled an Auction Sale in the Eldians Marketplace successfully.");
    }

    /**
     * Method to send a game gift between users
     *
     *
     * @param game A game to be sent
     * @param sender A user sending the Gift
     * @param reciever A user to recieve the Gift
     * @param market The current Market
     */
    public void giftTo(Game game, AbstractUser sender, AbstractUser reciever, Marketplace market){
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
    public void removeGame(Game game, AbstractUser owner, Marketplace market){
        // Attempting the remove transaction for the User
        owner.removeGame(game, market);
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
            transactionHistory.add(seller.getUsername() + " made a refund to "
                    + buyer.getUsername() + " for $" + amount + ".");
            System.out.println(seller.getUsername() + " made a refund to "
                    + buyer.getUsername() + " for $" + amount + ".");
        }
        // failed to issue refund
        else{
            // Seller unable to transfer the funds
            if(!canSendMoney){
                System.out.println(FAIL_BEGIN + seller.getUsername() + " could not make a " +
                        "refund to " + buyer.getUsername() + " for $" + amount + " due to insufficient funds" + FAIL_END);
            }
            else { // Buyer unable to accept the funds
                System.out.println(FAIL_BEGIN + buyer.getUsername() + " could not accept a " +
                        "refund from " + seller.getUsername() + " for $" + amount + " due to account maxing out " +
                        "upon addition of funds" + FAIL_END);
            }
        }
        return result;
    }


    /** User Builder class to build an admin type user. Mandatory attribute is the name.
     * 
     */
    public static class UserBuilder {

        public String username; //mandatory
        public double accountBalance; // optional
        public ArrayList<Game> inventory = new ArrayList<Game>(); //optional
        public ArrayList<String> transactionHistory; //optional

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

        /** Build and return the Admin User
         * 
         * @return Admin user object with the builder's attributes
         */
        public AdminUser build(){
            return new AdminUser(this);
        }
    }
}