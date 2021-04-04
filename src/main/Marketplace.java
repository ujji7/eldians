package main;
import transactions.Finder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class that keeps a record for what Games are being offered by what seller along with the functionality related
 * to the MarketPlace
 *
 */
public class Marketplace {
    // Boolean to account for Auction Sale
    public boolean auctionSale = false;
    // Unique ID associated with each game being offered by a Seller
    private int uid = 0;
    // Keeps a record for Seller against their offerings
    private HashMap<String, ArrayList<Game>> gamesOnSale;

    /** Creates a brand new Market with no active Sellers
     *
     */
    public Marketplace() {
        this.gamesOnSale = new HashMap<String, ArrayList<Game>>();
    }

    /**
     * Constructs market from previously pre-existing Market with Sellers and Games
     *
     * @param auctionSale True if Sale was going on, false otherwise
     * @param market a hashmap containing all the sellers and their offerings
     */
    public Marketplace(boolean auctionSale, HashMap<String, ArrayList<Game>> market){
        this.auctionSale = auctionSale;
        this.gamesOnSale = market;
    }

    /**
     * Gets the current auction sale Status for the Market
     *
     * @return true if Sale is offered, false otherwise
     */
    public boolean getAuctionSale(){
        return this.auctionSale;
    }

    
    
    /**
     * Toggle the Auction Sale in the market
     *
     *                  ---- WITH THE NEW UNDERSTANDING according to Piazza @692
     *                  ---- THIS will be implemented in Applicaiton/MarketPlace
     *                  ---- The setting up of Sale percentage is completed and done in Game
     */
    public void toggleSale(){

        this.auctionSale = !this.auctionSale;
    }

    /**
     * Get all the games currently being sold
     *
     * @return The Hashmap of the Market, key: UserName value: ArrayList<Game> they have up for sale
     */
    public HashMap<String, ArrayList<Game>> getGamesOnSale() {
        return this.gamesOnSale;
    }


    /** Returns all the unique IDs of the games being sold on the marketplace.
     * 
     * @return Arraylist of all unique ids of games being sold
     */
    private ArrayList<Integer> listUniqueIds() {
        ArrayList<Game> listOfGames = new ArrayList<Game>();
        for (String s : gamesOnSale.keySet()) {
            listOfGames.addAll(gamesOnSale.get(s));
        }
        
        ArrayList<Integer> listIds = new ArrayList<Integer>();
        for (Game g : listOfGames) {
            listIds.add(g.getUniqueID());
        }
        return listIds;
    }
    
    /**
     * Helper to increment the unique ID for the Game
     *
     */
    public void incrementUID(){
        
        ArrayList<Integer> listAllIds = listUniqueIds();
        this.uid ++;
        while (listAllIds.contains(this.uid)) {
            this.uid ++;
        }
    }

    /**
     * Helper to get the unique ID of the current marketplace's status
     * @return the integer value of the UniqueID
     */
    public int getUid(){
        return this.uid;
    }
    
    
    /** Helper to set the unique ID for the current marketplace's status
     * @param id the unique ID
     */
    public void setUid(Integer id){
        this.uid = id;
    }
    
    /**
     * Checks if the Seller already exists or not, if the Seller does not exist  then the
     * new Seller is added to the market
     *
     * @param seller User to be added as a Seller
     */
    public void addNewSeller(String seller){
        // Add Seller and an empty ArrayList if the Seller does not exist
        if(!this.checkSellerExist(seller)){
            ArrayList<Game> gamesOnSale = new ArrayList<Game>();
            this.gamesOnSale.put(seller, gamesOnSale);

            System.out.println("Seller: "+ seller +" added to the market");
        }
        // Seller already exists in our market
        else{
            System.out.println("Seller: "+ seller+" already exists in the market");
        }
    }

    /**
     * Checks if a Seller exists in our Market
     *
     * @param seller a User to check if they exist
     * @return true if they exist in the market false otherwise
     */
    public boolean checkSellerExist(String seller){
        return this.gamesOnSale.containsKey(seller);
    }

    /**
     * Checks if the seller is currently offering the sale of the Game title
     *
     * @param seller User checking if they are selling the game title
     * @param gameTitle The game title being checked
     * @return true if the seller is selling the Game title
     */
    public boolean checkSellerSellingGame(String seller, String gameTitle) {
        if(checkSellerExist(seller)) {
            ArrayList<Game> gameList = this.gamesOnSale.get(seller);
            for (Game g : gameList) {
                if (g.getName().equals(gameTitle)) return true;
            }
        }
        return false;
    }

    /**
     * Return true if seller is selling game and its not on hold
     *
     * @param seller User selling the game.
     * @param gameTitle game we are checking.
     * @return true if seller selling game and not on hold, false otherwise.
     */
    public boolean checkNotOnHold(String seller, String gameTitle) {
        if(checkSellerSellingGame(seller, gameTitle)) {
            Finder finder = new Finder();
            Game g = finder.findGame(gameTitle, this.gamesOnSale.get(seller));
            if (g.getHold()) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the list of games the seller is offering
     * Can be used for Future improvements and extra features
     *
     * @param seller User selling games
     * @return ArrayList of all the offerings
     */
    private ArrayList<Game> getMyOfferings(String seller){
        return this.gamesOnSale.get(seller);
    }


    /**
     * Returns the CURRENT price offering of the game the seller is selling
     * Can be used for Future improvements and extra features
     *
     * @param seller User selling games, must be a valid seller
     * @param gameTitle The game title being asked for the price, must exist with the Seller
     * @return the price of the game or -1 incase of Seller is not selling the game
     */
    private double calculatePrice(String seller, String gameTitle){
        double price = -1;
        // Get all the games offering from the Seller and check the Price for the game title requested
        ArrayList<Game> currOffering = this.getMyOfferings(seller);
        for(Game myGame : currOffering) {
            String currGameName = myGame.getName();
            if (currGameName.equals(gameTitle)) {
                price = myGame.getPriceWithDiscount(this.auctionSale);
            }
        }
    return price;
    }


    /**
     * Helper to remove the game from the User's offering from the current Market
     *
     * @param seller The person trying to remove the Game
     * @param game The Game to be removed
     */
    public void removeGame(String seller, String game){
        // get all my offerings and remove the game from my offerings if the game is not on hold
        ArrayList<Game> currOffering = this.getMyOfferings(seller);
        Game toRemove = null;
        for(Game curr : currOffering){
            if(curr.getName().equals(game)){
                toRemove = curr;
            }
        }
        // removing the game from the User's offering
        gamesOnSale.get(seller).remove(toRemove);
    }
    
}
