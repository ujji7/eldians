package transactions;

import main.AbstractUser;
import main.Game;
import main.Marketplace;

import java.util.ArrayList;

public class Refund implements Transaction {

    private final String buyer;
    private final String seller;
    private final double credit;

    /**
     * Creates a new refund transaction.
     *
     * @param b String representing the buyer's username.
     * @param s String representing the seller's username.
     * @param c String representing the credit being refunded.
     */
    public Refund(String b, String s, String c) {
        this.buyer = b;
        this.seller = s;
        this.credit = Double.parseDouble(c);
    }

    /**
     * Executes a Refund transaction. Return the user currently logged in.
     *
     * @param users ArrayList of Users in the system.
     * @param games ArrayList of Games in the system.
     * @param market the current Marketplace, holding games being sold by sellers.
     * @param login the user who is currently logged in.
     * @return the user currently logged in.
     */
    @Override
    public AbstractUser execute(ArrayList<AbstractUser> users, ArrayList<Game> games, Marketplace market,
                                AbstractUser login) {

        Finder find = new Finder();

        AbstractUser buyerUser = find.findUser(this.buyer, users);

        AbstractUser sellerUser = find.findUser(this.seller, users);

        if (buyerUser == null) {
            System.out.println("ERROR: \\<Fatal: User " + this.buyer + " cannot be found in system.\\>");
        } else if (sellerUser == null) {
            System.out.println("ERROR: \\<Fatal: User " + this.seller + " cannot be found in system.\\>");
        } else {
            login.refund(buyerUser, sellerUser, this.credit);
        }

        return login;
    }
}
