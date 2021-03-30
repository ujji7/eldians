package main;

import java.util.ArrayList;

/** Admin type user that extends the Abstract USer class. A sell type user cannot buy games, only sell. And
 * it cannot create or delete users.
 *
 */
public class SellUser extends AbstractUser {

    private SellUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "SS";
    }

    public String getUsername(){
        return this.username;
    }

    public String getType(){
        return this.type;
    }

    public double getAccountBalance(){
        return this.accountBalance;
    }

    public ArrayList<String> getTransactionHistory(){
        return this.transactionHistory;
    }

    /** Prints that this user cannot buy a game.
     *
     * @param seller the supplier of the game
     * @param game the name of the game
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT

    @Override
    public void buy(AbstractUser seller, Game game, boolean saleToggle, Marketplace market){
        System.out.println(this.username + " cannot buy games.");
    }


    /**
     * Sends Games to a User if they can accept this Game
     *
     * @param game a Game to be gifted
     * @param reciever person who will be recieving the Gift
     * @param market the current Market
     */
    @Override
    public void gift(Game game, AbstractUser reciever, Marketplace market){
        // Reciever is a Sell user
        if (reciever instanceof SellUser){
            System.out.println("ERROR: \\< Failed Constraint: Sell User can not accept any gifts. >//");
        }
        else{
            String gameName = game.getName();
            // Check if the Receiver has the game up for Sale on the Market
            boolean inRecMar = !market.checkSellerSellingGame(reciever.getUsername(), gameName);

            // if the User can accept the game then check if the sender can send the game
            if(inRecMar){
                boolean inSenMar = market.checkSellerSellingGame(this.getUsername(), gameName);
                // User can send the gift, game is added to the Receiver's inventory
                if (inSenMar){
                   // Game added to the receiver's inventory
                    this.inventory.add(game);
                }
                // Sender doesn't have the game
                else{
                    System.out.println("ERROR: \\" + this.username + " does not have the " + gameName +
                            ".\n Gift transaction failed.");
                }
            }
            // Reciever already has the game
            else{
                System.out.println("ERROR: \\" + reciever.getUsername() + " already has " +gameName+
                        ".\n Gift transaction failed.");
            }
        }
    }

    /**
     * Checks and removes the game for the User
     *
     * @param game The game being removed
     * @param market The current market
     */
    @Override
    public void removegame(Game game, Marketplace market){
        String currGame = game.getName();
        // check if the User is Selling the Game on the Market
        boolean iAmOffering = market.checkSellerSellingGame(this.getUsername(), currGame);
        // remove from Market
        if(iAmOffering){
            market.removeGame(this.getUsername(), currGame);
            System.out.println(game.getName()+ " was removed from the User's offering on the Market.");
        }
        // else printing out the error from Market
    }



    public static class UserBuilder {

        private String username; // required
        //        public String type;
        public double accountBalance;
        public double newFunds;
        public ArrayList<String> transactionHistory;

        public UserBuilder(String name) {
            this.username = name;
            this.accountBalance = 0.00;
            this.transactionHistory = new ArrayList<>();
        }

        public UserBuilder balance(double accountBalance){
            this.accountBalance = accountBalance;
            return this;
        }

        public UserBuilder newFunds(double newFunds){
            this.newFunds = newFunds;
            return this;
        }

        public UserBuilder transactionHistory(ArrayList<String> transactions){
            this.transactionHistory.addAll(transactions);
            return this;
        }

        public SellUser build() {
            SellUser user = new SellUser(this);
            return new SellUser(this);
        }

    }
}