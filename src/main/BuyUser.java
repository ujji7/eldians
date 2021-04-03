package main;

import java.util.ArrayList;
/** This is a buy type user that extends the Abstract User class. A Buy type user cannot sell games, only buy. And
 * it cannot create or delete users.
 *
 */
public class BuyUser extends AbstractUser {
    

    private BuyUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "BS";
        this.inventory = builder.inventory;

    }
    

    public ArrayList<Game> getInventory(){
        return this.inventory;
    }


    /** Prints that the user cannot sell a game.
     *
     * @param game game to be sold
     * @param market Marketplace game will be sold on
     */
    //THIS DOES NOT FOLLOW THE RIGHT FORMAT
    @Override
    public void sell(Game game, Marketplace market){
        System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have the ability to sell games.");
    }


    /**
     * Sends Games to a User if they can accept this Game
     *
     * @param INgame a Game to be gifted
     * @param reciever person who will be recieving the Gift
     * @param market the current Market
     */
    @Override
    public void gift(Game INgame, AbstractUser reciever, Marketplace market){

        // Reciever is a Sell user
        if (reciever instanceof SellUser){
            System.out.println("ERROR: \\< Failed Constraint: Sell User can not accept any gifts. >//");
        }
        else{
            // deep-copying the Game to work with
            Game game = this.gameCopy(INgame);
            String gameName = game.getName();
            // check if the Receiver has the game in their inventory
            boolean inRecInv = !reciever.gameInInventory(game);
            // Check if the Receiver has the game up for Sale on the Market
            boolean inRecMar = !market.checkSellerSellingGame(reciever.getUsername(), gameName);
            // if the User can accept the game then check if the sender can send the game
            if(inRecInv && inRecMar){
                boolean inSenInv = this.gameInInventory(game);
                // User can send the gift, game is added to the Receiver's inventory
                if (inSenInv){
                    // Game needs to be removed from the sender's inventory and added to the reciever's
                    this.removeFromInventory(gameName);
                    reciever.addGame(game);
                    // updating the transaction history for the users
                    String senderTran = this.getUsername() + " has gifted: " + gameName + " to " + reciever.getUsername();
                    String recTran = reciever.getUsername() + " has received " + gameName + " from " + this.getUsername();
                    this.addTranHis(senderTran);
                    reciever.addTranHis(recTran);
                    System.out.println(senderTran);
                    System.out.println(recTran);
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
     * @param INgame The game being removed
     * @param market The current market
     */
    @Override
    public void removegame(Game INgame, Marketplace market){
        // deep-copying the Game to work with
        Game game = this.gameCopy(INgame);
        String currGame = game.getName();
        // check if the User has the game
        boolean inMyInv = this.gameInInventory(game);
        // remove from inventory
        if (inMyInv){
            this.removeFromInventory(currGame);
            String tran = game.getName()+ " was removed from the User's inventory.";
            this.addTranHis(tran);
            System.out.println(tran);
        }
        else{
            System.out.println(game.getName()+ " was not found in the User's inventory.");
        }
    }

    public static class UserBuilder {

        private String username; // required
        //        public String type;
        public double accountBalance;
        public ArrayList<Game> inventory = new ArrayList<Game>();
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

        public UserBuilder inventoryGames(ArrayList<Game> inventory){
            this.inventory.addAll(inventory);

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

        public BuyUser build() {
            BuyUser user = new BuyUser(this);
            return new BuyUser(this);
        }

    }

}