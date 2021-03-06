/** Admin type user that extends the Abstract User class. An admin has the highest privileges.
 *
 */
public class AdminUser extends AbstractUser {


    public AdminUser(String username, Float credit) {
        super(username);
        this.accountBalance = credit;
    }

    /** Add credit to account.
     *
     * @param amount The amount of funds to be added to the User's account
     */
    @Override
    public void addCredit(float amount) {
        //another implementation
    }

    /** If there is currently no reduced price on games, turn on a sale for this amount. Else, turn off the sale.
     *
     * @param amount amount by which to reduce prices of games by.
     */
    @Override
    public void auctionSale(float amount) {
        if (Marketplace.getAuctionSale = 0.00f) {
            Marketplace.AuctionSale = amount;
        } else {
            Marketplace.getAuctionSale = 0.00f;
        }
    }
}