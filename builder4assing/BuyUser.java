/** This is a buy type user that extends the Abstract User class. A Buy type user cannot sell games, only buy. And
 * it cannot create or delete users.
 *
 */
public class BuyUser extends AbstractUser {

    public BuyUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "BS";
    }

//    public BuyUser(String username, Double credit) {
//        super(username);
//        this.accountBalance = credit;
//        this.type = "BS";
//    }
//
//    public BuyUser(String username, Double balance, ArrayList<Game> inventory, ArrayList<String> transactions) {
//        super(username, balance, inventory, transactions);
////        this.transactionHistory = transactions;
//    }

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
    public void create(String username, String type, double credit, Application application){
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

}