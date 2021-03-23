package Transactions;

public class RemoveGame implements Transaction {

    String gameName;
    String ownerName;

    /**
     * Constructs a RemoveGame object with the given parameters.
     *
     * @param g String representing the GameName of the game being removed.
     * @param o String representing the OwnerName of the game being removed.
     */
    public RemoveGame(String g, String o) {
        this.gameName = g;
        this.ownerName = o;
    }
}
