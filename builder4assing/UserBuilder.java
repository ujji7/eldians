import java.util.ArrayList;
import main.Game;

public class UserBuilder {

	public String username; //mandatory
	public String type; // mandatory
	public double accountBalance; // optional
	public ArrayList<Game> inventory; //optional
	public double newFunds; //optional
	public ArrayList<String> transactionHistory; //optional

	
	public UserBuilder(String name, String type) {
		this.username = name;
		this.type = type;
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


	// STEP 4: Create a build() method which creates and returns a User based on this builder


	public AbstractUser build() {
		AbstractUser user = null;
		switch (this.type){
			case "AA":
				user = new AdminUser(this); //user2 can be admin
				break;

			case"BS":
				user = new BuyUser(this);
            	break;
			case "SS":
            	user = new SellUser(this);
            	break;
			case "FS":
            	user = new FullStandardUser(this); //user2 can be admin
				break;
		return user;
	}
//	public void setAccountBalance(double balance){
//
//		this.accountBalance = balance;
//		this.transactionHistory.add("User: " + this.username + " added $" + balance + " to their account");
//	}

	/**
	 * Sets the inventory for our User
	 * @param inventory the inventory to-be in the user's account
	 */
	public void setInventory(ArrayList<Game> inventory){
		for (Game g : inventory) {
			this.inventory.add(g);
		}
	}

	public AbstractUser getPizza() {
		
		AbstractUser p = new AbstractUser(this.username, this.type);
		p.setAccountBalance(accountBalance);
//		p.setExtraSauce(extraSauce);
//		p.setPepperoni(pepperoni);
//		p.setPineapple(pineapple);
//		p.setTomatoes(tomatoes);
		return p;
	}
}
