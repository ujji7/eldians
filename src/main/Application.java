package main;

import transactions.Transaction;

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

    public void Run(ArrayList<Transaction> transactions) {

        for (Transaction transac : transactions) {
            login = transac.execute(this.userList, this.gamesList, this.market, this.login);
        }
    }

}