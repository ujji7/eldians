package main;

import java.util.ArrayList;

public class UserFactory {
    private final String admin = "AA";
    private final String buy = "BS";
    private final String sell = "SS";
    private final String full = "FS";

    public AbstractUser makeUser(String name, String type, double balance, ArrayList<Game> inventory, ArrayList<String> transactions) {

        AbstractUser user = null;

        if (type.equals(admin)) {
            user = new AdminUser(name, balance, inventory, transactions);
        } else if (type.equals(sell)) {
            user = new SellUser(name, balance, inventory, transactions);
        } else if (type.equals(buy)) {
            user = new BuyUser(name, balance, inventory, transactions);
        } else if (type.equals(full)) {
            user = new FullStandardUser(name, balance, inventory, transactions);
        }

        return user;

    }
}
