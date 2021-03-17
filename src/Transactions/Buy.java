package Transactions;

public class Buy implements Transaction {

    String buyer;
    String seller;
    Float credit;

    public Buy(String b, String s, String c) {
        this.buyer = b;
        this.seller = s;
        this.credit = Float.parseFloat(c);
    }
}
