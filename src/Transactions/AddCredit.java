package Transactions;

public class AddCredit implements Transaction {

    String username;
    String type;
    float credit;

    public AddCredit(String u, String t, String c) {
        this.username = u;
        this.type = t;
        this.credit = Float.parseFloat(c);
    }
}
