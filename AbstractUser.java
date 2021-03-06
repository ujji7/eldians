import java.util.ArrayList;
import Application;
import Marketplace;






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

    public void buy(){
// test test test test test test test

    }

    public void sell(){

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
            System.out.println("ERROR: \\ < Failed COnstraint: seller.getUsername() + " could not make a refund to " +
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
                    AdminUser newUser = AdminUser(username, credit);
                    break;
                case 'FS':
                    FullStandardUser newUser = FullStandardUser(username, credit);
                    break;
                case 'BS':
                    BuyUser newUser = BuyUser(username, credit);
                    break;
                case 'SS':
                    SellUser newUser = SellUser(username, credit);
                    break;
            }
            if(!Application.userList.contains(newUser)) {
                Application.addUser(newUser)
                System.out.println("A new user was created: \n" + newUser.toString());
            }
            System.out.println("ERROR: \\< Failed Constraint: New User could not be created since" +
                    "a User already exists with given name. >//");
            }
        System.out.println("ERROR: \\< Failed Constraint: New User could not be created since "
        + string(credit) + "amount is invalid. >//");

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
}