import java.util.ArrayList;

// director class is like a deal that builds multiple different pizzas
// Like when u order a special combo deal from a store

public class UserDirector {

	private UserBuilder builder;
	private ArrayList<AbstractUser> abstractUsers = new ArrayList<AbstractUser>();

	public void construct() {
		
		builder = new AdminUserBuilder();
//		builder.addExtraCheese();
		
		abstractUsers.add(builder.getPizza());
		abstractUsers.add(builder.getPizza());
		
		builder = new BuyUserBuilder();
		abstractUsers.add(builder.getPizza());
		abstractUsers.add(builder.getPizza());
		abstractUsers.add(builder.getPizza());
	}
	
	public ArrayList<AbstractUser> getPizzas() {
		return this.abstractUsers;
	}
}
