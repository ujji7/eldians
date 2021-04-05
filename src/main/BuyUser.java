package main;

import java.util.ArrayList;

/** This is a buy type user that extends the Abstract User class. A Buy type user cannot sell games, only buy. And
 * it cannot create or delete users.
 *
 */
public class BuyUser extends AbstractUser {

    /** Construct a buy user using the builder attributes
     * @param builder User builder that builds the buy user
     */
    private BuyUser(UserBuilder builder) {
        this.username = builder.username;
        this.accountBalance = builder.accountBalance;
        this.type = "BS";
        this.inventory = builder.inventory;

    }

    /** Prints that the user cannot sell a game.
     *
     * @param game game to be sold
     * @param market Marketplace game will be sold on
     */
    @Override
    public boolean sell(Game game, Marketplace market){
        System.out.println(FAIL_BEGIN+ this.username + " does not have the ability to sell " +
                "games" + FAIL_END);
        return false;
    }


    /**
     * Sends Games to a User if they can accept this Game
     *
     * @param inGame a Game to be gifted
     * @param receiver person who will be recieving the Gift
     * @param market the current Market
     */
    @Override
    public void gift(Game inGame, AbstractUser receiver, Marketplace market){
        if (receiver instanceof SellUser){ // Receiver is a Sell user
            System.out.println(FAIL_BEGIN + receiver.getUsername() + "is a sell user and cannot "+ 
                    "accept gifts" + FAIL_END);
        }
        else{
            Game game = this.gameCopy(inGame); // deep-copying the Game to work with
            String gameName = game.getName();
            // if the User can accept the game then check if the sender can send the game
            if(receiver.canAccept(game, market)){
                boolean inSenInv = this.gameInInventory(game); // user can send gift, game is added to Receiver's inventory
                if (inSenInv){ // Game needs to be removed from the sender's inventory and added to the receiver's
                    if(!this.gameInventoryHold(inGame) &&this.removeFromInventory(gameName) ){
                        //this.removeFromInventory(gameName);
                        receiver.addGame(game);
                        String senderTran = this.getUsername() + " has gifted: " + gameName + " to " + receiver.getUsername();
                        String recTran = receiver.getUsername() + " has received " + gameName + " from " + this.getUsername();
                        this.addTranHis(senderTran); // updating the transaction history for the users
                        receiver.addTranHis(recTran);
                        System.out.println(senderTran);
                        System.out.println(recTran);
                    }
                    // Sender has the game on hold
                    else{
                        System.out.println(FAIL_BEGIN + gameName + " is on Hold and will be up for processing the next day."
                                + " Gift transaction failed" + FAIL_END);
                    }
                }
                else{ // Sender doesn't have the game
                    System.out.println(FAIL_BEGIN + this.username + " does not have the " + 
                            gameName + ". Gift transaction failed" + FAIL_END);
                }
            }
            else{ // Receiver already has the game
                System.out.println(FAIL_BEGIN + receiver.getUsername() + " already has " +
                        gameName+ ". Gift transaction failed" + FAIL_END);
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
        // check if the User has the game
        boolean inMyInv = this.gameInInventory(game);
        // remove from inventory
        if (inMyInv){
            boolean removed = this.removeFromInventory(currGame);
            if (removed) {
                String tran = game.getName()+ " was removed from " + this.username + "'s inventory.";
                this.addTranHis(tran);
                System.out.println(tran);
            }
        }
        else{
            System.out.println(FAIL_BEGIN + game.getName()+ " was not found in " + this.username + "'s inventory" + 
                    FAIL_END);
        }
    }

    /** User Builder class to build a Buy type user. Mandatory attribute is the name.
     *
     */
    public static class UserBuilder {

        private String username; // required
        public double accountBalance;
        public ArrayList<Game> inventory = new ArrayList<Game>();
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

        /** Set the builder's inventory with the given inventory
         *
         * @param inventory the inventory to set the builder at
         * @return the user builder
         */
        public UserBuilder inventoryGames(ArrayList<Game> inventory){
            this.inventory.addAll(inventory);
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

        /** Build and return the Buy User
         *
         * @return BuyUser object with the builder's attributes
         */
        public BuyUser build() {
            return new BuyUser(this);
        }

    }
}