package Transactions;

public class Login implements Transaction{

    String username;
    String type;
    float funds;

    public Login(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }
}
