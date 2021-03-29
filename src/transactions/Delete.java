package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Delete implements Transaction {

    String username;
    String type;
    double funds;

    /**
     * Creates a new Delete transaction.
     *
     * @param u String representing the username.
     * @param t String representing the user type.
     * @param f String representing the funds.
     */
    public Delete(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Double.parseDouble(f);
    }

    /**
     * Executes a Delete transaction. Returns the user currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return the user currently logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {

        // Find the user being deleted
        AbstractUser delete = null;
        for (AbstractUser user : users) {
            if (user.getUsername().equals(this.username)) {
                delete = user;
            }
        }

        // Delete if they exist, raise error otherwise
        if (delete == null) {
            System.out.println("ERROR: < Cannot delete " + this.username + " as the user does not exist in the " +
                    "system > ");
        } else {
            login.delete(delete, this.funds);
        }

        return login;
    }
}
