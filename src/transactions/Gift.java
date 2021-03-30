package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Gift implements Transaction {

    String gameName;
    String ownerName;
    String receiverName;

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

        // Find game
        Game game = null;
        for (Game g: games) {
            if (g.getName().equals(this.gameName)) {
                game = g;
            }
        }

        // Find owner
        AbstractUser owner = null;
        for (AbstractUser user: users) {
            if (user.getUsername().equals(this.ownerName)) {
                owner = user;
            }
        }

        // Find receiver
        AbstractUser receiver = null;
        for (AbstractUser user: users) {
            if (user.getUsername().equals(this.receiverName)) {
                receiver = user;
            }
        }

        // If game, owner, and receiver all exist, execute AbstractUser.gift, raise error otherwise
        if (game == null) {
            System.out.println("ERROR: < Cannot find " + this.gameName + " in the system. > ");
        } else if (owner == null) {
            System.out.println("ERROR: < Cannot find " + this.ownerName + " in the system. > ");
        } else if (receiver == null) {
            System.out.println("ERROR: < Cannot find " + this.receiverName + " in the system. > ");
        } else {
            // If normal user
            //FORMAT gift(GAME, RECEIVER, MARKET)

            // IF admin and valid sender name provided
            // FORMAT gift(GAME, SENDER, RECEIVER, MARKET)

            login.gift(owner, receiver, game);
        }

        return login;
    }
}
