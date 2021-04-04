package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Sell implements Transaction {

    private final String gameName;
    private final String seller;
    private final Double discount;
    private final Double salePrice;

    /**
     * Creates a new Sell transaction.
     *
     * @param g String representing the name of the game being sold.
     * @param s String representing the name of the seller.
     * @param d String representing the discount associated with the game.
     * @param p String representing the sale price of the game.
     */
    public Sell(String g, String s, String d, String p) {
        this.gameName = g;
        this.seller = s;
        this.discount = Double.parseDouble(d);
        this.salePrice = Double.parseDouble(p);
//        System.out.println(g + " " +  s + " " +  d + " " + p);
//        System.out.println(gameName + " " +seller +  " " +discount + " " +salePrice);
    }

    /**
     * Executes Sell transaction. Returns the user currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return Current user who is logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {

        if (!login.getUsername().equals(this.seller)) {
            System.out.println("WARNING: \\<Logged in user does not match username: " + this.seller +
                    ", proceeding using logged in user as the seller.\\>");
        }

        // If game is already on market, do not put another on market (end here)
        if (market.checkSellerSellingGame(this.seller, this.gameName)) {
            System.out.println("ERROR: \\<Failed Constraint: " + this.seller + " could not sell " +
                    this.gameName + " as User is already selling this exact game.\\>");
            return login;
        }


        // Generate UniqueID
        market.incrementUID();
        int uid = market.getUid();

        // Create the game
        Game newGame = new Game(this.gameName, this.salePrice, login.getUsername(), uid, this.discount);

        // Sell the game
        boolean worked = login.sell(newGame, market);


        // Add game to games list
        if (worked) games.add(newGame);

        return login;
    }
}
