package src.transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Login implements Transaction{

    String username;
    String type;
    double funds;

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
            System.out.println("ERROR: < There is already a User who is logged in. >");
            return login;
        }

        // Find the user trying to log in
        AbstractUser log = null;
        for (AbstractUser user : users) {
            if (user.getUsername().equals(this.username)) { log = user; }
        }

        // if we found the user, return them since they are the new person who is logged in.
        if (log != null) { return log; }

        // else the user doesn't exist so this is an error and return who was previously logged in (null).
        else {
            System.out.println("ERROR: < User not found in database. >");
            return login;
        }
    }
}
