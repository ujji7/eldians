package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Sell implements Transaction {

    String gameName;
    String seller;
    float discount;
    float salePrice;

    public Sell(String g, String s, String d, String p) {
        this.gameName = g;
        this.seller = s;
        this.discount = Float.parseFloat(d);
        this.salePrice = Float.parseFloat(p);
    }

    @Override
    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market, String login) {
        return null;
    }
}
