import java.util.HashMap;
public class Marketplace {
    public float auctionSale;
    public HashMap<AbstractUser, Game[]> gamesOnSale;

    public Marketplace() {
        this.auctionSale = 0.00f;
        this.gamesOnSale = new HashMap<AbstractUser, Game[]>();
    }

    public float getAuctionSale(){
        return this.auctionSale;
    }

    public HashMap<AbstractUser, Game[]> getGamesOnSale() {
        return this.gamesOnSale;
    }
    // for some reason the data types cannot be accessed in abstract class - something to do with packages and main?
}