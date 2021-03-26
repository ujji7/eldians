import main.Game;

import java.util.ArrayList;

//has all possible attributes
public class UserChainBuilder {

	public String username;
	public String type;
	public double accountBalance;
	public ArrayList<main.Game> inventory;
	public double newFunds;
	public ArrayList<String> transactionHistory;

	public UserChainBuilder(String name, String type) {
		this.username = name;
		this.type = type;
	}

	public UserChainBuilder setAccountBalance(double balance){

		this.accountBalance = balance;
		this.transactionHistory.add("User: " + this.username + " added $" + balance + " to their account");
		return this;
	}

	/**
	 * Sets the inventory for our User
	 * @param inventory the inventory to-be in the user's account
	 * @return
	 */
	public UserChainBuilder setInventory(ArrayList<Game> inventory){
		for (main.Game g : inventory) {
			this.inventory.add(g);
		}
		return this;
	}


	public AbstractUser getPizza() {
		AbstractUser p = new AbstractUser(this.username, this.type);
		p.setAccountBalance(accountBalance);
		return p;
	}
}
