package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public interface Transaction {

    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market, AbstractUser login);

}
