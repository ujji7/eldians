package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import main.AdminUser;

import java.util.ArrayList;

public class AddCredit implements Transaction {

    private final String username;
    private final String type;
    private final float credit;

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

        if (login instanceof AdminUser && this.username.equals("")) {

            // When username field is empty, the admin wants to add to itself, but warn to console anyways.
            System.out.println("WARNING: \\<Username field left empty, Adding funds to self.\\>");
            login.addCredit(this.credit);

        } else if (login instanceof AdminUser) {

            Finder find = new Finder();
            // Find user we are adding credit to
            AbstractUser foundUser = find.findUser(this.username, users);

            if (foundUser == null) {
                System.out.println("ERROR: \\<Fatal: User " + this.username + " Not Found.\\>");
            } else {
                ((AdminUser) login).addCreditTo(this.credit, foundUser);
            }

        }
            else {
                if(!login.getUsername().equals(this.username)){
                    System.out.println("ERROR: \\<Fatal: User " + this.username + " does not match logged in user.\\>");
                }
            login.addCredit(this.credit);
        }
        return login;
    }

}
