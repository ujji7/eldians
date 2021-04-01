package main;

import transactions.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Application {

    public static ArrayList<AbstractUser> userList;

    public Marketplace market;
//    public Marketplace yesterdaysMarketplace;
    public ArrayList<Game> gamesList;
    public AbstractUser login;

    public Application(){
        this.userList = new ArrayList<>();
    }

    public static void addUser(AbstractUser user) {
        userList.add(user);
    }

    private void BODRead() {
//        ReadingJSON readJson = new ReadingJSON();
        List<Game> games = ReadingJSON.readGamesFile();
//        System.out.println(games);
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
//        System.out.println(users);
        Marketplace market = ReadingJSON.readMarketFile(games, users);
        userList = (ArrayList<AbstractUser>) users;
        this.gamesList = (ArrayList<Game>) games;
        this.market = market;
        Set<String> usersv2 = this.market.getGamesOnSale().keySet();
        for (String u : usersv2) {
            System.out.println(u + "'s game: " + market.getGamesOnSale().get(u));
        }
//        System.out.println(market);
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


    public void Run(ArrayList<Transaction> transactions) {
        BODRead();
        for (Transaction transac : transactions) {
            login = transac.execute(this.userList, this.gamesList, this.market, this.login);
        }
        EODWrite();
    }
}