package main;
import java.util.ArrayList;

public class Application {
    public static ArrayList<AbstractUser> userList;
    public Marketplace market;

    public Application(){
        this.userList = new ArrayList<>();
    }

    public static void addUser(AbstractUser user){
        userList.add(user);
    }

}



// for Admin transaction when toggling sale call market.toggleSale()