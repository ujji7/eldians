import java.util.ArrayList;
import main.Game;

public class UserBuilder {

	public String username;
	public String type;
	public double accountBalance;
	public ArrayList<Game> inventory;
	public double newFunds;
	public ArrayList<String> transactionHistory;

	
	public UserBuilder(String name, String type) {
		this.username = name;
		this.type = type;
	}

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
