package main;

public class UserFactory {

    public AbstractUser makeUser(String type) {

        AbstractUser user = null;

        if (type.equals("Admin")) {
            user = new AdminUser();
        } else if (type.equals("Sell")) {
            user = new Sell();
        } else if (type.equals("Buy")) {
            user = new Buy();
        } else if (type.equals("Full")) {
            user = new FullStandardUser();
        }

        return user;

    }
}
