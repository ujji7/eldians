package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Sell implements Transaction {

    String gameName;
    String seller;
    Double discount;
    Double salePrice;

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

        // Generate UniqueID
        int uid = 101; // TEMPORARY! REMOVE WHEN WE HAVE A WAY TO GENERATE UNIQUEID'S

        // Create the game
        Game newGame = new Game(this.gameName, this.salePrice, login.getUsername(), uid, this.discount);

        // Sell the game
        login.sell(newGame, market);

        // Add game to games list
        games.add(newGame);

        return login;
    }
}
