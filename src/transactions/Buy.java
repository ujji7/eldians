package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Buy implements Transaction {

    String game;
    String buyer;
    String seller;

    public Buy(String g, String b, String s) {
        this.game = g;
        this.buyer = b;
        this.seller = s;
    }

    @Override
    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market, String login) {
        return login;
    }
}
