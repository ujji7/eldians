import java.util.ArrayList;
import main.Game;
//import main.Marketplace;
import java.util.HashMap;
//CREATE AND DELETE ARE COMMENTED OUT TO MAKE THIS RUN WITHOUT THE APPLICATION CLASS
/**
 * Abstract class for User objects.
 *                                      ------NEED TO MAKE IT INTO AN ABS CLASS
 *                                      ------Need to have multiple constructor VS Builder
 *                                      -----As we will be requiring a constructors with given inventories
 */
//PROBLEM WITH BUILDER
	// SINCE THIS WOULD BE THE PIZZA OF BUILDER 2, IT WOULD HAVE ALL CHARACTERSTICS SET TO FALS OR NULL IF THEY DONT EXIST INTHE USER TYPE
	// SO FOR EXAMPLE, INVENTORY WOULDN'T BE IN HERE - I THINK.
	// SINCE THIS IS THE ABSTRACT USER, IT WOULD HAVE METHODS LIKE BUY/SELL ETC. FOR BUY/SELL ETC, YOU NEED AN INVENTORY
	// SO IF WE TOOK OUT THE INVENTORY ATTRIBUTE IN THIS CLASS (LIKE JUST COMMENTED IT OUT) WE GET ERRORS. BC WE CANNOT
	// HAVE THOSE METHODS WITHOUT HAVING AN INVENTORY ATTRIBUTE IN THE USER. SO EITHER WE WOULD HAVE TO TAKE OUT ALL
// METHODS THAT USE INVENTORY OUT OF THIS CLASS AND ONLY IN THE RESPECTIVE BUILDER CLASS BUT THE THING IS BUILDER JUST
// BUILDS THE OBJECT WITH THE GIVEN SPECS, IT CANT RLLY STORE METHODS (LIKE BUY/SELL), SO THOSE WOULD HAVE TO BE HERE. FOR
// FOR THOSE METHODS TO BE HERE, THE INVENTORY ATTRIBUTE MUST BE HERE AND IT MUST BE INITIALIZED
public class AbstractUser {
	//like Pizza in builder2

	public String username;
	public String type;
	public double accountBalance;
	public ArrayList<Game> inventory;
	public double newFunds;
	public ArrayList<String> transactionHistory;
	public static final double MAXFUNDS = 999999.99f;
	// can change minFunds to allow overdrafts for future improvements
	public static final float MINFUNDS = 0f;
	public static final float DAILYLIMIT = 1000f;
	public static final float NEWFUNDSTODAY = 0f;

	//mandatory constructor
	public AbstractUser(String name, String type) {

		this.username = name;
		this.type = type;
		this.accountBalance = 0; // why is this 0? should we change the constructor
//		this.inventory = new ArrayList<Game>();
		this.newFunds = 0;
		this.transactionHistory = new ArrayList<String>();
	}


	//SETTER METHODS AND OTHER METHODS
	/**
	 * Sets the account balance for our User
	 * @param balance the amount to balance to-be in the user's account
	 */
	public void setAccountBalance(double balance){

		this.accountBalance = balance;
		this.transactionHistory.add("User: " + this.username + " added $" + balance + " to their account");
	}

	/**
	 * Sets the inventory for our User
	 * @param inventory the inventory to-be in the user's account
	 */
	public void setInventory(ArrayList<Game> inventory){
		for (Game g : inventory) {
			this.inventory.add(g);
		}
	}

	/**
	 * Get the current User's unique username
	 * @return username String
	 */
	public String getUsername(){
		return this.username;
	}

	/**
	 * get the account balance for the user
	 * @return the current account balance of the user
	 */
	public double getAccountBalance(){
		return this.accountBalance;
	}


	/**
	 * Transfer the requested funds to the user's account
	 * Max out the funds if the addition of funds results in an overflow of funds
	 *
	 * @param amount The value of funds to be added
	 */
	public void transferFunds(double amount){
		//boolean result = true;
		// check if we can add the funds
		if(this.canAcceptFunds(amount)){
			if(this.newFunds + amount <= DAILYLIMIT) {
				this.setAccountBalance(this.getAccountBalance() + amount); // this can be this.accountBalance += amount
				System.out.println("$" + amount + " added to" + this.username);
			}
			// Verify from piazza if this is right!
			else {
				System.out.println("Couldn't process addition of funds as " +this.getUsername()+
						" account will be maxed out upon addition of funds.");
			}
		}
		else {
			// ACCORDING TO PIAZZA @666 max out the balance and prompt user    //change it later
			this.setAccountBalance(MAXFUNDS);
			System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
					"'s balance was Maxed up upon addition of more funds!");
		}
		System.out.println("New account balance is $" + this.getAccountBalance());
		//return result;
	}


	//NEED TO MAKE A HELPER TO ISSUE REFUND AMOUNTS
	// Need a helper to round off floats for wach transaction


	/**
	 * Adds the amount of funds to be added to the User's account and prints out the Status
	 *
	 * @param amount The amount of funds to be added to the User's account
	 */
	public void addCredit(double amount) {
		// check the constraints of daily limit
		double fundsToday = this.newFunds;
		if (this.dailyLimitCheck(amount)) {
			// checking other constraints and then attempting to add the funds
			this.transferFunds(amount);
		} else {
			// According to piazza @701                         /change it later
			System.out.println("ERROR: \\ < Failed Constraint: " + this.username +
					" daily limit be reached! No funds were added");
		}
	}



	/** Return true if the user is selling this game in the market.
	 *
	 * @param game Game to check for if user is selling in market
	 * @return true if user selling game, else false
	 */
	//helper for buy
	private boolean sellingGame(Game game, Marketplace market) {
		if (market.getGamesOnSale().containsKey(this)) { //user is selling a game in the mkt place
			for (Game g : market.getGamesOnSale().get(this)) {
				if (g.getUniqueID() == game.getUniqueID()) {
					return true;
				}
			}
		}
		return false;
	}

	/** Return true if the game is already in the user's inventory.
	 *
	 * @param game game to check for in inventory
	 * @return true if game in inventory, else false
	 */
	//HELPER FOR BUY
	private boolean gameInInventory(Game game) {
		for (Game g : this.inventory) {
			if (g.getName().equals(game.getName())) {
				return true;
			}
		}
		return false;
	}

	/** Remove the amount price from account balance and add the game to inventory. Print out message with details.
	 *
	 * @param seller that game is bought from
	 * @param price amount to be removed from account balance
	 * @param game to be added to inventory
	 */
	private void payAndAddGame(AbstractUser seller, double price, Game game) {
		this.accountBalance -= price;
		this.inventory.add(game);
		System.out.println(this.username + " has bought " + game.getName() + " from " + seller.getUsername() + " for "
				+ price + ".");
	}

	/** Buy a game from a seller and add it to user's inventory, if the game is not already in the user's inventory.
	 *
	 * @param seller the supplier of the game
	 * @param game the name of the game
	 * @param saleToggle the status of Sale being on the market
	 */

	public void buy(AbstractUser seller, Game game, boolean saleToggle){

//        if (!seller.sellingGame(game)) {  //check if seller is selling this game on market - THIS CAN JUST BE IN MRKTPLC
//            //marketplace.containsKey(seller) && marketplace.get(seller).get(game)
//            System.out.println("ERROR: \\ < Failed Constraint: " + seller.getUsername() + "is not selling " + game.getName() + " on the market.");
//        }


		if (gameInInventory(game)) { //check that game isn't already in inventory
			//this.inventory.contains(game)
			System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " already owns " + game.getName() + ". Cannot buy it again.");

		}

		else {                                                  // Needs to be implemented in transferFunds()

			double price = game.getPriceWithDiscount(saleToggle);
			if (!this.canTransferFunds(price)) { //buyer does not have enough money
				System.out.println("ERROR: \\ < Failed Constraint: "+ this.username + " does not have enough funds to buy " + game.getName() + ". ");
			}
			// Needs to be implemented in transferFunds()

			else if (!seller.canAcceptFunds(price)) { //seller's account maxed out
				this.payAndAddGame(seller, price, game);
				seller.accountBalance = MAXFUNDS;
				System.out.println("ERROR: \\ < Failed Constraint: "+ this.username +
						"'s balance was Maxed out upon sale of game.");
			}
			else { // make normal add and print message
				this.payAndAddGame(seller, price, game);
				seller.accountBalance += price;
				this.transactionHistory.add("User: " + this.username + " bought " + game.getName() + " from "
						+ seller.getUsername());
			}
		}
	}

	// I JUST PUT THIS TYPE SIGNATURE TO MAKE ANOTHER FUNCTION WORK YOU CAN EDIT IT LATER - bharathi

	/**
	 * add game to market being sold by this AbstractUser if AbstractUser doesn't already have the game on market.
	 *
	 * @param game Game object being sold to the market
	 * @param market Marketplace that will sell this game
	 */
	public void sell(Game game, Marketplace market){

		// if game doesn't follow contraints end here
		if (!this.sellConstraints(game, market)) return;

		HashMap<String, ArrayList<Game>> map = market.getGamesOnSale(); // var for less typing
		// if user has previously put games on the market, add to list of games
		if (map.containsKey(this.username)) {
			map.get(this.username).add(game);
		} else {
			// Create a new ArrayList
			ArrayList<Game> gameList = new ArrayList<Game>();
			// Add game to the ArrayList
			gameList.add(game);
			// Insert the new Key-Value pairing to the market
			map.put(this.username, gameList);

			// Report to console and transactionHistory
			this.transactionHistory.add("User: " + this.username + " is now selling " + game.getName() +
					" for " + game.getPrice());
			System.out.println("Game: " + game.getName() + " is now being sold by " + this.getUsername() + " for $" +
					game.getPrice() + " at a " + game.getDiscount()+"% discount, will be availble for purchase tomorrow");
		}
	}

	/**
	 * Return true if this is a valid game that can be sold. If invalid returns false and prints error to console.
	 *
	 * @param game, Game being sold.
	 */



	private boolean sellConstraints(Game game, Marketplace market) {

		String gameName = game.getName();
		String userName = this.getUsername();
		double gamePrice = game.getPrice();
		double gameDiscount = game.getDiscount();

		// check if game price is gt max game price
		float maxPrice = 999.99f;
		if (gamePrice > maxPrice) {
			System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
					gameName + " for $" + gamePrice + " as it exceeds the maximum sale price. > //");
			return false;
		}
		// Check if game name is gt max name length
		int maxNameLength = 25;
		if (gameName.length() > maxNameLength) {
			System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
					gameName + " for $" + gamePrice + " as it exceeds the maximum name length. > //");
			return false;
		}
		// Check if game discount is gt max discount amount
		double maxDiscount = 90;
		if (gameDiscount > maxDiscount) {
			System.out.println("ERROR: \\ < Failed Constraint: " + userName + " could not sell " +
					gameName + " with " + gameDiscount + "% discount as it exceeds the maximum discount " +
					"amount. > //");
			return false;
		}
		// If game is already on market, do not put another on market (end here)

		if (this.sellingGame(game, market)) {
			System.out.println("ERROR: \\ < Failed Constraint: " + this.getUsername() + " could not sell " +
					game.getName() + " as User is already selling this exact game > //");
			return false;
		}
		// passes all checks / follows all constraints
		return true;
	}

	/**
	 * Issues a refund and transfers the funds between the two user if the funds are avalible in the supplier's account
	 * @param buyer the customer asking for the refund
	 * @param seller the supplier of the games issueing the refund
	 * @param amount the value of credits to be transfered among them
	 * @return true if the refund was made false otherwise
	 */
	public boolean refund(AbstractUser buyer, AbstractUser seller, double amount){
		boolean result = false;
		// need to check if the seller can transfer funds
		boolean canSendMoney = seller.canTransferFunds(amount);
		if(canSendMoney) {
			// remove the credits from the seller's account
			seller.transferFunds(-amount);                  //-----FAILS when seller has less funds
			//seller.issueFunds(float amount, AbstractUser user)
			// add the funds regardless of maxing out
			buyer.transferFunds(amount);        // remove this when above is done
			result = true;
			System.out.println(seller.getUsername() + " made a refund to "
					+ buyer.getUsername() + " for $" + amount);
		}
		// buyer unable to transfer funds
		else{
			System.out.println("ERROR: \\ < Failed Constraint: " + seller.getUsername() + " could not make a refund to " +
					buyer.getUsername() + " for $" + amount + " due to insufficient funds. > //");
		}
		return result;

	}

	/**
	 * creates a new user of given type and adds them to the Application userList
	 * @param username a string with a length: 1-15
	 * @param type a string representing the User type of the newly created user
	 *             where AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard
	 * @param credit a float representing the amount of credits to add to the newly
	 *               created user's account balance
	 */
//	public void create(String username, String type, double credit, Application application){
//		if(MINFUNDS <= credit || credit <= MAXFUNDS){
//			AbstractUser newUser;
//			switch (type) {
//				case "AA":
//					newUser = new AdminUser(username, credit);
//					break;
//				case "FS":
//					newUser = new FullStandardUser(username, credit);
//					break;
//				case "BS":
//					newUser = new BuyUser(username, credit);
//					break;
//				case "SS":
//					newUser = new SellUser(username, credit);
//					break;
//				default:
//					// if user isn't initialized we stop the create function
//					System.out.println("ERROR: \\< Failed Constraint: New User could not be created since user type " +
//							"does not exist. > //");
//					return;
//			}
//			if(!Application.userList.contains(newUser)) {
//				Application.addUser(newUser);
//				this.transactionHistory.addTransaction("User: " + this.username + " has created user " +
//						newUser.getUsername());
//				System.out.println("A new user was created: \n" + newUser.toString());
//			}
//			System.out.println("ERROR: \\< Failed Constraint: New User could not be created since" +
//					"a User already exists with given name. >//");
//		}
//		System.out.println("ERROR: \\< Failed Constraint: New User could not be created since "
//				+ Double.toString(credit) + "amount is invalid. >//");
//
//	}
//
//	/**
//	 * Given the UserID and account balance delete the user's account
//	 *
//	 *
//	 */
//	public void delete(AbstractUser user, double amount){
//		System.out.println("ERROR: \\< Failed Constraint: Current User is not allowed to delete someone's account. >//");
//
//	}

	/**
	 * Returns True if the amount of funds are avalible for the current User
	 * @param amount the value of funds to check are present for our user
	 * @return true if the amount is avalible false otherwise
	 */
	private boolean canTransferFunds(double amount){
		return this.accountBalance - amount >= MINFUNDS;
	}

	/**
	 * Checks if the person's account will not maxout after addition of funds
	 * @param amount the amount of funds to be added
	 * @return true if the funds can be added false otherwise
	 */
	private boolean canAcceptFunds(double amount){
		return this.accountBalance + amount <= MAXFUNDS;
	}


	private boolean dailyLimitCheck(double amount){
		return this.NEWFUNDSTODAY + amount <= DAILYLIMIT;
	}


	/** Prints that the user cannot implement an auction sale.
	 * @param amount amount by which to reduce prices of games by.
	 */
	public void auctionSale(double amount) {
		System.out.println(this.getUsername() + "cannot implement an auction sale.");
	}
	
	public String toString() {
		return "User: " + this.username +  " , " + this.type + " " + this.accountBalance;

	}
}