package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class AddCredit implements Transaction {

    String username;
    String type;
    float credit;

    public AddCredit(String u, String t, String c) {
        this.username = u;
        this.type = t;
        this.credit = Float.parseFloat(c);
    }

    @Override
    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                          String login) {
        return login;
    }
}
