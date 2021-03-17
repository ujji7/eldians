package Transactions;

public class Refund implements Transaction {

    String buyer;
    String seller;
    Float credit;

    public Refund(String b, String s, String c) {
        this.buyer = b;
        this.seller = s;
        this.credit = Float.parseFloat(c);
    }
}
