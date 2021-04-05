package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Login implements Transaction{

    private final String username;
    private final String type;
    private final double funds;

    /**
     * Creates a new Login Transaction.
     *
     * @param u String representing the username of the User trying to login.
     * @param t String representing the type of the User trying to login.
     * @param f String representing the funds of the User trying to login
     */
    public Login(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Double.parseDouble(f);
    }

    /**
     * Executes a Login Transaction. Returns the user who is logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return AbstractUser who is logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {

        // If someone is logged in, this is an error since only one person can be logged in at a time.
        if (login != null) {
            System.out.println("ERROR: \\<Fatal: There is already a User who is logged in.\\>");
            return login;
        }

        Finder find = new Finder();

        // Find the user trying to log in
        AbstractUser log = find.findUser(this.username, users);

        if (log == null) {
            System.out.println("ERROR: \\<Fatal: User " + this.username + " not found in database.\\>");
            return null;
        }

        // If user exists but details are wrong, proceed and warn the user
        if (log.getAccountBalance() != this.funds) {
            System.out.println("WARNING: \\<User logging in " + this.username + " does not have matching funds, " +
                    "proceeding with login.\\>");
        }
        if ((log instanceof main.SellUser && !this.type.equals("SS")) ||
                (log instanceof main.BuyUser && !this.type.equals("BS")) ||
                (log instanceof main.FullStandardUser && !this.type.equals("FS")) ||
                (log instanceof main.AdminUser && !this.type.equalsIgnoreCase("AA"))) {
            System.out.println("WARNING: \\<User logging in " + this.username + " is not of correct type, " +
                    "proceeding with login.\\>");
        }

        return log;
    }
}
