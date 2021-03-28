package src.transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Logout implements Transaction{

    String username;
    String type;
    double funds;

    /**
     * Creates a new Logout transaction.
     *
     * @param u String representing the username.
     * @param t String representing the type.
     * @param f String representing the current funds.
     */
    public Logout(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Double.parseDouble(f);
    }

    /**
     * Executes a logout transaction. Returns null since no one is logged in once someone logs out.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return null, no one is logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {
        return null;
    }
}
