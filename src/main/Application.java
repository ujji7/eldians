package main;
import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;

public class Application {
    public static ArrayList<AbstractUser> userList;

    public Marketplace market;
    public Marketplace yesterdaysMarketplace;
    public ArrayList<Game> gamesList;
    public AbstractUser login;

    public Application(){
        this.userList = new ArrayList<>();
    }

    public static void addUser(AbstractUser user) {
        userList.add(user);
    }

    private void eodWriteeod(){
        DatabaseController databaseController = new DatabaseController();
        try {
            databaseController.writeGame(this.gamesList);
            databaseController.writeUser(this.userList);
            databaseController.writeMarket(this.market);
        }catch (IOException e){
            System.out.println("Cannot write files");
        }
    }

}




// for Admin transaction when toggling sale call market.toggleSale()