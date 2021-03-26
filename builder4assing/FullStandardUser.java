/** Full standard type user that extends the Abstract User class. A ful standard user can both buy and sell games.And
 * it cannot create or delete users.
 *
 */
public class FullStandardUser extends AbstractUser {


    public FullStandardUser(String username, double credit) {
        super(username);
        this.accountBalance = credit;
        this.type = "FS";
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