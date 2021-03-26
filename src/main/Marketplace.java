package main;
import java.util.HashMap;
import java.util.ArrayList;

public class Marketplace {
    public boolean auctionSale = true;

    public HashMap<String, ArrayList<Game>> gamesOnSale, todaySale;

    public Marketplace() {
        this.gamesOnSale = new HashMap<String, ArrayList<Game>>();
        this.todaySale = new HashMap<String, ArrayList<Game>>();

    }

    public boolean getAuctionSale(){
        return this.auctionSale;
    }

    /**
     * Toggle the Auction Sale in the market
     *
     */
    private void toggleSale(){
        this.auctionSale = !this.auctionSale;
    }

    public HashMap<String, ArrayList<Game>> getGamesOnSale() {
        return this.gamesOnSale;
    }
    // this is just returning the marketplace change the method name
    // for some reason the data types cannot be accessed in abstract class - something to do with packages and main?


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
     * Adds the Game for the Seller in the market
     *
     * @param seller User adding the Game to their offerings
     * @param game The game to be added to the Seller's offering
     */
    public void addGameForSeller(String seller, Game game){
        // Check if the Seller exists and add the Game to their list of offering
        if(this.checkSellerExist(seller)){
            String addGame = game.getName();

            //ArrayList<Game> currOffering = this.getMyOfferings(seller);

            // check if the user is Selling the game or has the game up for sale if not then add the game
            if(!this.checkSellerSellingGame(seller, addGame) && !this.gameToBeUpCheck(seller, addGame)){
                this.addForTomMar(seller, game);
                System.out.println(game.getName() + " has now been added to the seller's offering");
            }
            else {
                System.out.println("Seller: "+ seller+" is already selling " + game.getName());
            }
        }
        // Seller currently does not exist in our market
        else{
            System.out.println("Seller: "+ seller+" does not exist in the market");
        }
    }


    /**
     *
     *
     * @param seller User adding the Game to their offerings
     * @param game a Unique game to be put up for Sale
     */
    private void addForTomMar(String seller, Game game){
        HashMap<String, ArrayList<Game>> todayMarket = this.todaySale;
        // check if the user already had put any games up for sale today and add the game
        if(todayMarket.containsKey(seller)){
            ArrayList<Game> futOffering = todayMarket.get(seller);
            futOffering.add(game);
        }
        else {
            ArrayList<Game> futOffering = new ArrayList<Game>();
            futOffering.add(game);
            todayMarket.put(seller, futOffering);
        }

    }


    /**
     * Checks if the game title will be up for sale tomorrow
     *
     * @param seller User offerings the game
     * @param gameTitle the game title to be checked
     * @return true if the Game title will be up for sale tomorrow
     */
    public boolean gameToBeUpCheck(String seller, String gameTitle){
        boolean result = false;
        // get the games put up for Sale by the user Today and check if the User will have the game title up for sale
        ArrayList<Game> futureOffering = this.todaySale.get(seller);
        for(Game myGame : futureOffering) {
            String currGameName = myGame.getName();
            if (currGameName.equals(gameTitle)) {
                result = true;
            }
        }
        return result;
    }


    /**
     * Checks if a Seller exists in our Market
     *
     * @param seller a User to check if they exist
     * @return true if they exist in the market false otherwise
     */
    public boolean checkSellerExist(String seller){
        return this.gamesOnSale.containsKey(seller;
    }


    /**
     * Checks if the seller is  selling the Game title
     *
     * @param seller User checking if they are selling the game title
     * @param gameTitle The game title being checked
     * @return true if the seller is selling the Game title
     */
    public boolean checkSellerSellingGame(String seller, String gameTitle){
        boolean result = false;
        // if the Seller exists and check if they are selling the Game
        if(this.checkSellerExist(seller)) {
            ArrayList<Game> currOffering = this.getMyOfferings(seller);
            boolean gameFound = false;
            // check if the seller is selling the game title
            for(Game myGame : currOffering){
                String currGameName = myGame.getName();
                if(currGameName.equals(gameTitle)){
                    result = true;
                    gameFound = true;
                }
            }
            if(!gameFound){
                System.out.println("Seller: "+ seller+" is currently not offering "+ gameTitle);
            }
        }
        // Seller currently does not exist in our market
        else{
            System.out.println("Seller: "+ seller+" does not exist in the market");
        }
        return result;
    }


    /**
     * Returns the list of games the seller is offering
     *
     * @param seller User selling games
     * @return ArrayList of all the offerings
     */
    private ArrayList<Game> getMyOfferings(String seller){
        return this.gamesOnSale.get(seller);
    }


    /**
     * Returns the CURRENT price offering of the game the seller is selling
     *
     * @param seller User selling games
     * @param gameTitle The game title being asked for the price
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
     * Sync yesterday's market with Today's market at the end of the day
     *
     */
    public void syncMarkets(){
        // get all the seller and the new games and add them to the tomorrow's market
        HashMap<String, ArrayList<Game>> todayMarket = this.todaySale;


        // for each seller sync their past offering with the new offerings
        for (String seller : todayMarket.keySet()) {
            // get the seller's current offering
            ArrayList<Game> myOffering = this.getMyOfferings(seller);
            // get all the games to put up for sale
            ArrayList<Game> newGames = todayMarket.get(seller);
            // loop through all the new games they will have up sale on tomorrow
            // add it to seller's tomorrow's offering
            myOffering.addAll(newGames);
        }
        // empty the today sale <- NEED TO IMPLEMENT the empty today sale.
        System.out.println("Market is updated for tomorrow.");
    }

}
