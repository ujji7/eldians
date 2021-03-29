package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import main.AdminUser;

import java.util.ArrayList;

public class AddCredit implements Transaction {

    String username;
    String type;
    float credit;

    /**
     * Creates a new AddCredit transaction.
     *
     * @param u String representing the username.
     * @param t String representing the type.
     * @param c String representing the credit being added.
     */
    public AddCredit(String u, String t, String c) {
        this.username = u;
        this.type = t;
        this.credit = Float.parseFloat(c);
    }

    /**
     * Executes an AddCredit transaction.
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

        if (login instanceof AdminUser) {
            // Find user we are adding credit to
            AbstractUser foundUser = null;
            for (AbstractUser user : users) {
                if (user.getUsername().equals(this.username)) { foundUser = user; }
            }

            if (foundUser == null) {
                System.out.println("ERROR: < User " + this.username + "Not Found >");
            } else {
                ((AdminUser) login).addCreditTo(this.credit, foundUser);
            }
        } else {
            login.addCredit(this.credit);
        }
        return login;
    }
}
