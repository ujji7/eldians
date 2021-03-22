package main;

import java.util.ArrayList;

public class TransactionHistory {

    private ArrayList<String> history;

    /**
     * Creates a new TransactionHistory object with a previous history
     *
     * @param h, ArrayList<String> representing the previous transaction history
     */
    public TransactionHistory(ArrayList<String> h) {
        this.history = h;
    }

    /**
     * Creates a new TransactionHistory with an empty history
     */
    public TransactionHistory() {
        this.history = new ArrayList<String>();
    }

    /**
     * Adds an event to the TransactionHistory
     *
     * @param transaction, String representing the transaction that took place
     */
    public void addTransaction(String transaction) {
        this.history.add(transaction);
    }

    /**
     * Returns the history held in this object
     *
     * @return ArrayList<String> representing the history of transactions made
     */
    public ArrayList<String> getHistory() {
        return this.history;
    }

}
