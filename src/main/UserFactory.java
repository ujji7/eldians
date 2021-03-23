package main;

public class UserFactory {

    public AbstractUser makeFruit(String type) {

        AbstractUser user = null;

        if (type.equals("Admin")) {
            fruit = new AdminUser();
        } else if (type.equals("Sell")) {
            fruit = new Sell();
        } else if (type.equals("Buy")) {
            fruit = new Buy();
        } else if (type.equals("Full")) {
            fruit = new FullStandardUser();
        }

        return user;

    }
}
