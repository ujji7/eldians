package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Buy implements Transaction {

    String game;
    String buyer;
    String seller;

    /**
     * Creates a new Buy transaction.
     *
     * @param g String representing game name.
     * @param b String representing the buyer.
     * @param s String representing the seller.
     */
    public Buy(String g, String b, String s) {
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

        // Find the buyer
        AbstractUser buyer = null;
        for(AbstractUser user : users) {
            if (user.getUsername().equals(this.buyer)) {
                buyer = user;
            }
        }
        if (buyer == null) {
            System.out.println("ERROR: < Buyer not found in database. >");
        }
        // Make sure the buyer is the person who is logged in
        else if (buyer != login) {
            System.out.println("ERROR: < User making the buy transaction is not the logged in user. >");
        } else {

            // Find the seller
            AbstractUser seller = null;
            for(AbstractUser user : users) {
                if (user.getUsername().equals(this.seller)) {
                    seller = user;
                }
            }

            if (seller == null) {
                System.out.println("ERROR: < Seller not found in database. >");
            } else {

                // Find the game
                Game gameOnSale = null;
                for(Game game : games) {
                    if (game.getName().equals(this.game) && game.getSupplierID().equals(this.seller)) {
                        gameOnSale = game;
                    }
                }
                if (gameOnSale == null) {
                    System.out.println("ERROR: < Game not found in database. >");
                } else {

                    // Make the purchase
                    buyer.buy(seller, gameOnSale, market.getAuctionSale());
                }
            }
        }

        return login;
    }
}
