import java.util.ArrayList;
/**
 * Abstract class for User objects.
 */
public class AbstractUser {
    public String username;
    public double accountBalance;
    public ArrayList<Game> inventory;
    public static final double maxFunds = 999999.99;
    // can change minFunds to allow overdrafts for future improvements
    public static final float minFunds = 0;

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
            this.setAccountBalance(maxFunds);
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
            System.out.println(seller.getUsername() + " made a refund to " + buyer.getUsername() + " for $" + amount);
        }
        // buyer unable to transfer funds
        else{
            System.out.println(seller.getUsername() + " could not make a refund to " +
                    buyer.getUsername() + " for $" + amount + " due to insufficient funds.");
        }
        return result;

    }


    public void create(){

    }

    public void delete(){

    }

    /**
     * Returns True if the amount of funds are avalible for the current User
     * @param amount the value of funds to check are present for our user
     * @return true if the amount is avalible false otherwise
     */
    private boolean canTransferFunds(float amount){
        return this.accountBalance - amount >= minFunds;
    }

    /**
     * Checks if the person's account will not maxout after addition of funds
     * @param amount the amount of funds to be added
     * @return true if the funds can be added false otherwise
     */
    private boolean canAcceptFunds(float amount){
        return this.accountBalance + amount <= maxFunds;
    }
}