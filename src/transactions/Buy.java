package transactions;
//lLINE 42 PRINT STATEMENT - CHANGES Alice to alice
import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Buy implements Transaction {

    private final String game;
    private final String buyer;
    private final String seller;

    /**
     * Creates a new Buy transaction.
     *
     * @param g String representing game name.
     * @param b String representing the buyer.
     * @param s String representing the seller.
     */
    public Buy(String g, String s, String b) {
        this.game = g;
        this.buyer = b;
        this.seller = s;
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

        Finder find = new Finder();

        // Find the buyer
        AbstractUser buyer = find.findUser(this.buyer, users);
        if (buyer == null) {
            System.out.println("ERROR: < Buyer not found in database. >");
        }
        // Make sure the buyer is the person who is logged in
        else if (buyer != login) {
            System.out.println("ERROR: < User making the buy transaction is not the logged in user. >");
        } else {

            // Find the seller
            AbstractUser seller = find.findUser(this.seller, users);

            if (seller == null) {
                System.out.println("ERROR: < Seller not found in database. >");
            } else {

                // Find the game
                Game gameOnSale = find.findGame(this.game, games);
                if (gameOnSale == null) {
                    System.out.println("ERROR: < Game not found in database. >");
                } else {

                    // Make the purchase
                    buyer.buy(seller, gameOnSale, market.getAuctionSale(), market);
                }
            }
        }

        return login;
    }
}
