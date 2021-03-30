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

    private void EODWrite(){
        DatabaseController databaseController = new DatabaseController();
        try {
            databaseController.writeGame(this.gamesList);
            databaseController.writeUser(this.userList);
            databaseController.writeMarket(this.market);
        }catch (IOException e){
            System.out.println("Cannot write files");
        }
    }

    private void BODRead() {
        ReadingJSON readJson = new ReadingJSON();
        List<Game> games = readJson.readGamesFile();
        List<AbstractUser> users = readJson.readUsersFile(games);
        readJson.readMarketFile(games, users);
    }

    public void Run(ArrayList<Transaction> transactions) {
//        BODRead();
        for (Transaction transac : transactions) {
            login = transac.execute(this.userList, this.gamesList, this.market, this.login);
        }
        EODWrite();
    }

}