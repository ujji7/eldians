import java.util.ArrayList;

public class BuyUser extends AbstractUser {

    public BuyUser(String username) {
        super(username);
    }

    public BuyUser(String username, Double credit) {
        super(username);
        this.accountBalance = credit;
    }

    public BuyUser(String username, Double balance, ArrayList<Game> inventory) {
        super(username, balance, inventory);
//        this.inventory = inventory;
    }

    public BuyUser(String username, Double balance, ArrayList<Game> inventory, ArrayList<String> transactions) {
        super(username, balance, inventory, transactions);
//        this.transactionHistory = transactions;
    }
}