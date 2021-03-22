package Transactions;

public class Buy implements Transaction {

    String game;
    String buyer;
    String seller;

    public Buy(String g, String b, String s) {
        this.game = g;
        this.buyer = b;
        this.seller = s;
    }
}
