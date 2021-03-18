package Transactions;

public class Logout implements Transaction{

    String username;
    String type;
    float funds;

    public Logout(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }
}
