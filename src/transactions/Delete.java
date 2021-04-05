package transactions;

import main.*;

import java.util.ArrayList;

public class Delete implements Transaction {

    private final String username;
    private final String type;
    private final double funds;

    /**
     * Creates a new Delete transaction.
     *
     * @param u String representing the username.
     * @param t String representing the user type.
     * @param f String representing the funds.
     */
    public Delete(String u, String t, String f) {
        this.username = u;
        this.type = t;
        this.funds = Double.parseDouble(f);
    }

    /**
     * Executes a Delete transaction. Returns the user currently logged in.
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
        // Find the user being deleted
        AbstractUser delete = find.findUser(this.username, users);
        // Delete if they exist, raise error otherwise, raise warnings if they exits but attributes are not corrects
        if (delete == null) {
            System.out.println("ERROR: \\<Failed Constraint: Cannot delete " + this.username + " as the user does " +
                    "not exist in the system.\\>");
            return login;
        }
        // If deleted user exists but has wrong details, proceed but raise errors.
        if (delete.getAccountBalance() != this.funds) {
            System.out.println("WARNING: \\<User " + this.username + " being deleted does not have matching funds, " +
                    "proceeding with deletion.\\>");
        }
        if((delete instanceof main.SellUser && !this.type.equals("SS")) ||
                (delete instanceof main.BuyUser && !this.type.equals("BS")) ||
                (delete instanceof main.FullStandardUser && !this.type.equals("FS"))) {
            System.out.println("WARNING: \\<User " + this.username + " being deleted is not of correct type, " +
                    "proceeding with deletion.\\>");
        }
        if(!(login instanceof AdminUser)){
            System.out.println("ERROR: \\<Failed Constraint: Cannot delete " + this.username + " as the user " +
                    "logged in does not have permissions.\\>");
            return login;
        }
        if(!(delete instanceof BuyUser)) {
            market.getGamesOnSale().remove(this.username);
        }
        users.remove(delete);
        login.delete(delete, this.funds);
        return login;
    }
}
