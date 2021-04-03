package transactions;

import main.AbstractUser;
import main.Game;

import java.util.ArrayList;

public class Finder {

    /**
     * Returns the user located in userList according to username, null otherwise.
     *
     * @param username String representing username
     * @param userList ArrayList<AbstractUsers> holding all the systems users.
     * @return AbstractUser if found, null otherwise
     */
    public AbstractUser findUser(String username, ArrayList<AbstractUser> userList) {
        for (AbstractUser user : userList) {
            if (user.getUsername().equals(username)) { return user; }
        }
        return null;
    }

    /**
     * Returns the game located in gameList according to username, null otherwise.
     *
     * @param title String representing the game title.
     * @param gameList ArrayList<Game> holding all the system's games.
     * @return Game if found, null otherwise
     */
    public Game findGame(String title, ArrayList<Game> gameList) {
        for (Game game : gameList) {
            if (game.getName().equals(title)) { return game; }
        }
        return null;
    }


}
