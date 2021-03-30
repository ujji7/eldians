package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class RemoveGame implements Transaction {

    String gameName;
    String ownerName;

    /**
     * Constructs a RemoveGame object with the given parameters.
     *
     * @param g String representing the GameName of the game being removed.
     * @param o String representing the OwnerName of the game being removed.
     */
    public RemoveGame(String g, String o) {
        this.gameName = g;
        this.ownerName = o;
    }

    /**
     * Executes a RemoveGame transaction. Return the user currently logged in.
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

        // Find owner of game
        AbstractUser owner = null;
        for (AbstractUser user: users) {
            if (user.getUsername().equals(this.ownerName)) {
                owner = user;
            }
        }

        // Find game
        Game game = null;
        for (Game g: games) {
            if (g.getName().equals(this.gameName)) {
                game = g;
            }
        }

        // remove game if owner and game exist
        if (owner == null) {
            System.out.println();
        } else if (game == null) {
            System.out.println();
        } else {
            login.removegame(game, market);
        }

        return login;
    }
}
