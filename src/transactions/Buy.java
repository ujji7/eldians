package transactions;
//lLINE 42 PRINT STATEMENT - CHANGES Alice to alice
import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Buy implements Transaction {

    private final String game;
    private final String seller;
    private final String buyer;

    /**
     * Creates a new Buy transaction.
     *
     * @param g String representing game name.
     * @param b String representing the buyer.
     * @param s String representing the seller.
     */
    public Buy(String g, String s, String b) {
        this.game = g;
        this.seller = s;
        this.buyer = b;
    }

    /**
     * Executes a Buy transaction. Returns the user currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return the user who is currently logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {
        // Find the buyer
        Finder find = new Finder();
        AbstractUser buyer = find.findUser(this.buyer, users);
        if (buyer == null) {
            System.out.println("ERROR: \\<Fatal: Buyer " + this.buyer + " not found in system.\\>");
        } else if (buyer != login) { // Make sure the buyer is the person who is logged in
            System.out.println("ERROR: \\<Fatal: User " + this.buyer + " making the buy transaction is not the " +
                    "logged in user.\\>");
        } else {
            // Find the seller
            AbstractUser seller = find.findUser(this.seller, users);
            if (seller == null) {
                System.out.println("ERROR: \\<Fatal: Seller " + this.seller + " not found in database.\\>");
            } else {

                // Find the game
                Game gameOnSale = find.findGame(this.game, games);
                if (gameOnSale == null) {
                    System.out.println("ERROR: \\<Fatal: Game " + this.game + " not found in database.\\>");
                } else {

                    // Make the purchase
                    buyer.buy(seller, gameOnSale, market.getAuctionSale(), market);
                }
            }
        }
        return login;
    }
}
