package Transactions;

import java.util.ArrayList;

public interface Transaction {

    public String execute(ArrayList<AbstractUser> users, ArrayList<Game> games,
                                     Marketplace market, String login);

}
