package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Refund implements Transaction {

    String buyer;
    String seller;
    Float credit;

    public Refund(String b, String s, String c) {
        this.buyer = b;
        this.seller = s;
        this.credit = Float.parseFloat(c);
    }

    @Override
    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market, String login) {
        return null;
    }
}
