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
        System.out.println(gameName + " " +seller +  " " +discount + " " +salePrice);
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

        if (!login.getUsername().equals(this.seller)) {
            System.out.println("WARNING: < Logged in user does not match username: " + this.seller +
                    ", proceeding using logged in user as the seller. >");
        }

        // Create the game
        Game newGame = new Game(this.gameName, this.salePrice, login.getUsername(), uid, this.discount);

        System.out.println(newGame.getName());
        System.out.println(newGame.getDiscount());
        System.out.println(newGame.getPrice());
        System.out.println(newGame.getUniqueID());
        System.out.println(newGame.getSupplierID());

        // Sell the game
        login.sell(newGame, market);
        ArrayList<Game> sellerGames = market.getGamesOnSale().get(seller);
        for (Game g : sellerGames) {
            System.out.println("game is: " + g.getName());
        }

        // Add game to games list
        games.add(newGame);

        return login;
    }
}
