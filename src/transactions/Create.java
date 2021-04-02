package transactions;

import main.AbstractUser;
import main.Application;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Create implements Transaction {

    private final String username;
    private final String type;
    private final double funds;

    /**
     * Creates a new Create transaction.
     *
     * @param u String representing the username.
     * @param t String representing the type.
     * @param f String representing the current funds
     */
    public Create(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Double.parseDouble(f);
    }

    /**
     * Executes a Create transaction. Returns the user who is currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return The user currently logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {
        Finder finder = new Finder();
        // Create user, AbstractUser.create handles errors.
        if(finder.findUser(this.username, users) == null) {
            AbstractUser newUser = login.create(this.username, this.type, this.funds);
            users.add(newUser);
        }
        return login;
    }
}
