package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Logout implements Transaction{

    private final String username;
    private final String type;
    private final double funds;

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

        if (login == null) {
            System.out.println("ERROR: \\<Fatal: No one is currently logged in, maintaining previous state.\\>");
            return null;
        }

        if (!login.getUsername().equals(this.username)) {
            System.out.println("WARNING: \\<User logging out " + this.username + " does not match username of " +
                    "user currently logged in, proceeding by logging out user currently logged in.\\>");
        }

        if (login.getAccountBalance() != this.funds) {
            System.out.println("WARNING: \\<User logging out " + this.username + " does not have matching funds, " +
                    "proceeding with logout.\\>");
        }

        if ((login instanceof main.SellUser && !this.type.equals("SS")) ||
                (login instanceof main.BuyUser && !this.type.equals("BS")) ||
                (login instanceof main.FullStandardUser && !this.type.equals("FS")) ||
                (login instanceof  main.AdminUser && !this.type.equals("AA"))) {
            System.out.println("WARNING: \\<User logging out " + this.username + " is not of correct type, " +
                    "proceeding with loggout.\\>");
        }

        return null;
    }
}
