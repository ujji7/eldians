import java.util.ArrayList;

/** Admin type user that extends the Abstract User class. An admin has the highest privileges.
 *
 */
public class AdminUser extends AbstractUser {
    inventory;

    public AdminUser(UserBuilder builder) {
        super(builder);
        this.type = "AA";
        this.inventory = builder.inventory;
        this.accountBalance = builder.accountBalance;
        this.transactionHistory = builder.transactionHistory
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
}