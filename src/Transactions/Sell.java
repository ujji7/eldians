package Transactions;

public class Sell implements Transaction {

    String gameName;
    String seller;
    float discount;
    float salePrice;

    public Sell(String g, String s, String d, String p) {
        this.gameName = g;
        this.seller = s;
        this.discount = Float.parseFloat(d);
        this.salePrice = Float.parseFloat(p);
    }
}
