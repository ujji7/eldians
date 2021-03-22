package Transactions;

public class Gift implements Transaction {

    String gameName;
    String ownerName;
    String receiverName;

    /**
     * Constructs a Gift object with the given parameters.
     *
     * @param g String representing the GameName being gifted.
     * @param o String representing the OwnerName of the game being gifted.
     * @param r String representing the RecieverName who is receiving the gift.
     */
    public Gift(String g, String o, String r) {
        this.gameName = g;
        this.ownerName = o;
        this.receiverName = r;
    }
}
