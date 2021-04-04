package transactions;

import main.AbstractUser;
import main.AdminUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class RemoveGame implements Transaction {

    private final String gameName;
    private final String ownerName;

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

        Finder find = new Finder();

        // Find owner of game
        AbstractUser owner = find.findUser(this.ownerName, users);

        // Find game
        Game game = find.findGame(this.gameName, games);

        // remove game if owner and game exist, call correct remove game if user is admin or not.
        if (owner == null) {
            System.out.println("ERROR: \\<Fatal: User: " + this.ownerName + " does not exist in database.\\>");
        } else if (game == null) {
            System.out.println("ERROR: \\<Fatal: Game: " + this.gameName + " does not exist in database.\\>");

        } else if (login instanceof AdminUser) {
            ((AdminUser) login).removeGame(game, owner, market);
        } else {
            login.removeGame(game, market);
        }

        return login;
    }
}
