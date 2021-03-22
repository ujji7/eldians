package main;
import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;

public class Application {
    public Marketplace market;
    public Marketplace yesterdaysMarketplace;
    public ArrayList<AbstractUser> users;
    public ArrayList<Game> games;
    public AbstractUser login;

    public Application(){
        this.users = new ArrayList<>();
    }

    public void addUser(AbstractUser user){
        users.add(user);
    }

    private void eodWrite() {
        DatabaseController databaseController = new DatabaseController();
        try {
            databaseController.writeGame(this.games);
            databaseController.writeUser(this.users);
            databaseController.writeMarket(this.market);
        }catch (IOException e){
            System.out.println("Cannot write files");
        }
    }
}




// for Admin transaction when toggling sale call market.toggleSale()