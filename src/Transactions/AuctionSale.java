package Transactions;

public class AuctionSale implements Transaction {

    String username;
    String type;
    float funds;

    public AuctionSale(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Float.parseFloat(f);
    }
}
