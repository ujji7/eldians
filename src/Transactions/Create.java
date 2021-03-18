package Transactions;

public class Create implements Transaction {

    String username;
    String type;
    float funds;

    public Create(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }
}
