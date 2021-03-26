import java.util.ArrayList;

public class SellUser extends AbstractUser {

    /**
     * @param username
     * @param credit
     */
    public SellUser(String username, Double credit) {
        super(username);
        this.accountBalance = credit;
    }

    public SellUser(String username, Double balance, ArrayList<Game> inventory){
        super(username, balance, inventory);
//        this.inventory = inventory;
    }

    public SellUser(String username, Double balance, ArrayList<Game> inventory, ArrayList<String> transactions) {
//        this.username = username;
//        this.accountBalance = balance;
//        this.inventory = inventory;
//        this.transactionHistory = transactions;

        super(username, balance, inventory, transactions);
    }
}

