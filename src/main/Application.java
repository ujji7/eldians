package main;

        import transactions.Transaction;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

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

    private void bodReadBOD() {
        ReadingJSON readFiles = new ReadingJSON();
        try {
            readFiles.filesOpener();
            List<Game> games = readFiles.readGamesFile();
            List<AbstractUser> users = readFiles.readUsersFile(games);
            Marketplace market = readFiles.readMarketFile(games, users);

        } catch (Exception e) {
            System.out.println("Cannot read files.");
        }
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

    public void Run(ArrayList<Transaction> transactions) {

        for (Transaction transac : transactions) {
            login = transac.execute(this.userList, this.gamesList, this.market, this.login);
        }
    }

}


// for Admin transaction when toggling sale call market.toggleSale()