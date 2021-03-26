package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Error implements Transaction{

    public Error() { System.out.println("Fatal Error: This is not a valid transaction code"); }

    @Override
    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market, String login) {
        return login;
    }
}
