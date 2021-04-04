package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Error implements Transaction{

    /**
     * Creates a new Error object that prints an Error to the console.
     */
    public Error() { System.out.println("ERROR: \\<Fatal: This is not a valid transaction code.\\>"); }

    /**
     * Executes an Error transaction. Returns the person who is currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {
        return login;
    }
}
