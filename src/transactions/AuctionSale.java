package transactions;

import main.AbstractUser;
import main.AdminUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class AuctionSale implements Transaction {

    private final String username;
    private final String type;
    private final float funds;

    /**
     * Creates a new AuctionSale transaction.
     *
     * @param u String representing username.
     * @param t String representing type.
     * @param f String representing funds.
     */
    public AuctionSale(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }

    /**
     * Executes AuctionSale transaction. Returns user who is currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return AbstractUser who is currently logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {
        if (login instanceof AdminUser) {
            market.toggleSale();
            login.auctionSale();
        } else {
            System.out.println("ERROR: \\<Failed Constraint: User " + login.getUsername() + " does not have " +
                "the authority to toggle an auction sale.\\>");
        }
        return login;
    }
}
