package Transactions;

public class Delete implements Transaction {

    String username;
    String type;
    float funds;

    public Delete(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }
}
