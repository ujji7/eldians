package main;

import transactions.Transaction;
import transactions.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** An Application class that stores the current list of users and games that exist in the database, as well as the 
 * current market and the user that is currently logged in. It reads the database at the beginning of the day, 
 * executes the transactions in the daily.txt file, and writes to the database at the end of the day.
 * 
 */
public class Application {
    
    public static ArrayList<AbstractUser> userList;
    public Marketplace market;
    public ArrayList<Game> gamesList;
    public AbstractUser login;
    String gameFile = "games.json";
    String userFile = "users.json";
    String marketFile = "market.json";

    /** Initialize a new application with an empty user list
     * 
     */
    public Application(){
        userList = new ArrayList<>();
    }

    /** Adds a user to the current userlist
     * 
     * @param user User to be added to the userlist
     */
    public static void addUser(AbstractUser user) {
        userList.add(user);
    }

    /** Reads the database at the beginning of day and sets the user list, games list, and market objects
     * 
     */
    private void BODRead() {

        ReadingJSON.setGameFileName(gameFile);
        ReadingJSON.setUserFileName(userFile);
        ReadingJSON.setMarketFileName(marketFile);
        
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        Marketplace market = ReadingJSON.readMarketFile(games, users);
        
        userList = (ArrayList<AbstractUser>) users;
        this.gamesList = (ArrayList<Game>) games;
        this.market = market;
    }

    /** Writes the new games, users and marketplace object to the database at the end of the day.
     * 
     */
    private void EODWrite(){
        DatabaseController databaseController = new DatabaseController();
        databaseController.setGameFileName(gameFile);
        databaseController.setUserFileName(userFile);
        databaseController.setMarketFileName(marketFile);
        try {
            databaseController.writeGame(this.gamesList);
            databaseController.writeUser(userList);
            databaseController.writeMarket(this.market);
        }catch (IOException e){
            System.out.println("ERROR: \\<FATAL: Cannot write files. \\>");
        }
    }


    /** Reads the database at the beginning of day, executes the transactions, and writes to the database at end of day
     * 
     * @param transactions an arraylist of transactions to execute
     */
    public void Run(ArrayList<Transaction> transactions) {
       
        
        BODRead();
        for (Transaction transac : transactions) {
            if (transac instanceof Login || login != null) {
                login = transac.execute(userList, this.gamesList, this.market, this.login);
            } else {
                System.out.println("ERROR: \\<FATAL: There is no user logged in, cannot execute transaction. \\>");
            }
        }
        EODWrite();
    }
}