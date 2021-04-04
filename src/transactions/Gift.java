package transactions;

import main.AbstractUser;
import main.AdminUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Gift implements Transaction {

    private final String gameName;
    private final String ownerName;
    private final String receiverName;

    /**
     * Constructs a Gift object with the given parameters.
     *
     * @param g String representing the GameName being gifted.
     * @param o String representing the OwnerName of the game being gifted.
     * @param r String representing the RecieverName who is receiving the gift.
     */
    public Gift(String g, String o, String r) {
        this.gameName = g;
        this.ownerName = o;
        this.receiverName = r;
    }

    /**
     * Executes Gift transaction. Return the user currently logged in.
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

        // Find game
        Game game = find.findGame(this.gameName, games);

        // Find owner
        AbstractUser owner = find.findUser(ownerName, users);

        // Find receiver
        AbstractUser receiver = find.findUser(receiverName, users);

        // If game, owner, and receiver all exist, execute AbstractUser.gift, raise error otherwise
        if (game == null) {
            System.out.println("ERROR: \\<Fatal: Cannot find " + this.gameName + " in the system.\\>");
        } else if (owner == null) {
            System.out.println("ERROR: \\<Fatal: Cannot find " + this.ownerName + " in the system.\\>");
        } else if (receiver == null) {
            System.out.println("ERROR: \\<Fatal: Cannot find " + this.receiverName + " in the system.\\>");
        } else if (login instanceof AdminUser){

            // Admin user may be trying to gift someone else's game.
            ((AdminUser) login).giftTo(game, owner, receiver, market);
        } else {

            // Normal gifting functionality
            login.gift(game, receiver, market);
        }

        return login;
    }
}
