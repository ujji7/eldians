public class AdminUserBuilder extends UserBuilder {

	// HOW DO WE ADD THE NAME AND STUFF HERE
	// I THINK I HAVE TO CREATE ANOTHER ADMIN USER OBJECT, LIKE IN BUILDER1 EXAMPLE'S USER2 OBJECT.
	public AdminUserBuilder() {
		super("hawaiian");
//		this.UserBuilder("Hawaiian"); // goes to pizzabuilder, and makes name = hawaiian
		this.setAccountBalance(); // sin
		this.addPepperoni();
	}
}
