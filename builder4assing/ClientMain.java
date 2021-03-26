import java.util.ArrayList;

public class ClientMain {

	public static void main(String[] args) {

		// TODO: Use PizzaBuilder to build a pizza with pepperoni, pineapple and extra cheese
		// Store this Pizza that you built in variable "p0"
		UserBuilder builder = new UserBuilder("Pineroni");

		builder.addPepperoni();
		builder.addPineapple();
		builder.addExtraCheese();
		AbstractUser p0 = builder.getPizza();
		
		System.out.println(p0); // Should print "Pineroni: extraCheese=true extraSauce=false pepperoni=true tomatoes=false pineapple=true"
		
		// TODO: Create a new HawaiianPizza using the appropriate PizzaBuilder
		// Store this Pizza in variable "h0"
		UserBuilder hawaiianBuilder = new AdminUserBuilder();
		AbstractUser h0 = hawaiianBuilder.getPizza();
		
		
		System.out.println(h0); // Should print "Hawaiian: extraCheese=false extraSauce=false pepperoni=true tomatoes=false pineapple=true"
		
	
		
		// Example of using the director to construct a bunch of pizzas
		UserDirector director = new UserDirector();
		director.construct();
		ArrayList<AbstractUser> abstractUsers = director.getPizzas();

		for (AbstractUser p : abstractUsers) {
			System.out.println(p);
		}

		// Example of free-style using the chain builder
		AbstractUser p1 = new UserChainBuilder("Original")
				.getPizza();
		System.out.println(p1);
		
		AbstractUser p2 = new UserChainBuilder("TheLarry")
				.addExtraCheese()
				.addExtraSauce()
				.addPepperoni()
				.getPizza();
		System.out.println(p2);

		AbstractUser p3 = new UserChainBuilder("TheSadia")
				.addTomato()
				.addExtraCheese()
				.getPizza();
		System.out.println(p3);

	}
}