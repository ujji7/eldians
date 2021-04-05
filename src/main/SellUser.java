package main;

import java.util.ArrayList;
/** Sell type user that extends the Abstract User class. A sell type user cannot buy games, only sell. And
 * it cannot create or delete users.
 *
 */
public class SellUser extends AbstractUser {

    /** Construct a Sell user using the builder attributes
     * @param builder User builder that builds the Sell user
     */
    private SellUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "SS";
    }
    
    /** Prints that this user cannot buy a game.
     *
     * @param seller the supplier of the game
     * @param game the name of the game
     */
    @Override
    public void buy(AbstractUser seller, Game game, boolean saleToggle, Marketplace market){
        System.out.println(FAIL_BEGIN + "Sell user: "+ this.username + " cannot buy games" + FAIL_END);
    }

    /** Prints to the console that this user cannot get an inventory as theirs is empty. 
     * @return null
     */
    @Override
    public ArrayList<Game> getInventory(){
        System.out.println(FAIL_BEGIN + "A Sell user does not have inventory" + FAIL_END);
        return null;
    }


    /**
     * Sends Games to a User if they can accept this Game
     *
     * @param InGame a Game to be gifted
     * @param receiver person who will be recieving the Gift
     * @param market the current Market
     */
    @Override
    public void gift(Game InGame, AbstractUser receiver, Marketplace market){
        // Receiver is a Sell user
        if (receiver instanceof SellUser){
            System.out.println(FAIL_BEGIN + receiver.getUsername() + "is a sell user and cannot "+
                    "accept gifts" + FAIL_END);
        }
        else{
            // deep-copying the Game to work with
            Game game = this.gameCopy(InGame);
            String gameName = game.getName();
            // if the Receiver can accept the game then check if the sender can send the game
            if(receiver.canAccept(game, market)){
                boolean inSenMar = market.checkSellerSellingGame(this.getUsername(), gameName);
                // User can send the gift and it is not on hold, game is added to the Receiver's inventory
                if (inSenMar){
                    boolean isNotOnHold = market.checkNotOnHold(this.getUsername(), gameName);
                    if(isNotOnHold) {
                        // Game added to the receiver's inventory and updating the transaction history for the users
                        String senderTran = this.getUsername() + " has gifted: " + gameName + " to " +
                                receiver.getUsername() + ".";
                        String recTran = receiver.getUsername() + " has received " + gameName + " from " +
                                this.getUsername() + ".";
                        this.addTranHis(senderTran);
                        receiver.addTranHis(recTran);
                        receiver.addGame(game);
                        System.out.println(senderTran);
                        System.out.println(recTran);
                    }
                    else{
                        System.out.println(FAIL_BEGIN + gameName + " is currently on Hold on Market until the next day. " +
                                this.username + " cannot gift the game. " + "Gift transaction failed" + FAIL_END);
                    }
                }
                // Sender doesn't have the game
                else{
                    System.out.println(FAIL_BEGIN + this.username + " does not have game: " + gameName +
                            " to be gifted. Gift transaction failed" + FAIL_END);
                }
            }
            // Receiver already has the game
            else{
                System.out.println(FAIL_BEGIN + receiver.getUsername() + " already has " +gameName+
                        ". Gift transaction failed" + FAIL_END);
            }
        }
    }

    /**
     * Checks and removes the game for the User
     *
     * @param InGame The game being removed
     * @param market The current market
     */
    @Override
    public void removeGame(Game InGame, Marketplace market){
        // deep-copying the Game to work with
        Game game = this.gameCopy(InGame);

        String currGame = game.getName();
        // check if the User is Selling the Game on the Market
        boolean iAmOffering = market.checkSellerSellingGame(this.getUsername(), currGame);
        // remove from Market
        if(iAmOffering){
            boolean notHold = market.checkNotOnHold(this.username, currGame);
            if (notHold) {
                market.removeGame(this.getUsername(), currGame);
                String tran = game.getName()+ " was removed from the user's offering on the market.";
                this.addTranHis(tran);
                System.out.println(tran);
            }
            System.out.println(FAIL_BEGIN + currGame + " cannot be removed as it is on hold in the market" + FAIL_END);
        }
        else {
            System.out.println(FAIL_BEGIN + game.getName()+ " is not being sold by " + this.username +
                    ", cannot be removed" + FAIL_END);
        }
    }



    /** User Builder class to build a Sell type user. Mandatory attribute is the name.
     *
     */
    public static class UserBuilder {

        private String username; // required
        public double accountBalance;
        public ArrayList<String> transactionHistory;
        
        /** Initialize a user builder with the given name.
         *
         * @param name of the user
         */
        public UserBuilder(String name) {
            this.username = name;
            this.accountBalance = 0.00;
            this.transactionHistory = new ArrayList<>();
        }

        /** Set the builder's account balance to account balance
         *
         * @param accountBalance the balance to set the builder at
         * @return the user builder
         */
        public UserBuilder balance(double accountBalance){
            this.accountBalance = accountBalance;
            return this;
        }

        /** Set the builder's transactionHistory with the given transactionHistory
         *
         * @param transactions the transactionHistory to set the builder at
         * @return the user builder
         */
        public UserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        /** Build and return the Sell User
         *
         * @return SellUser object with the builder's attributes
         */
        public SellUser build() {
            return new SellUser(this);
        }

    }
}