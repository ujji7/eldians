import java.util.ArrayList;

/** Full standard type user that extends the Abstract User class. A ful standard user can both buy and sell games.And
 * it cannot create or delete users.
 *
 */
public class FullStandardUser extends AbstractUser {


    public FullStandardUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "FS";
        this.inventory = builder.inventory;
        this.transactionHistory = builder.transactionHistory;

    }

    public class UserBuilder {

        public String username; //mandatory
        public String type; // mandatory
        public double accountBalance; // optional
        public ArrayList<main.Game> inventory; //optional
        public double newFunds; //optional
        public ArrayList<String> transactionHistory; //optional


        public UserBuilder(String name) {
            this.username = name;
            this.type = type;
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

        public FullStandardUser build(){
            FullStandardUser user = new FullStandardUser(this);
            return user;
        }
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
    public void create(String username, String type, double credit, Application application){
        System.out.println(this.username + " cannot create another user.");
    }

    /**Prints that this user cannot delete another user.
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    @Override
    public void delete(AbstractUser user, double amount){
        System.out.println(this.username + " cannot delete another user.");
    }


    /** Prints that this user cannot issue a refund.
     *
     * @param buyer the customer asking for the refund
     * @param seller the supplier of the games issueing the refund
     * @param amount the value of credits to be transfered among them
     * @return
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    @Override
    public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
        System.out.println(this.username + " cannot issue a refund.");
        return false;
    }
}