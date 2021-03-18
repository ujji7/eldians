package Transactions;

public class Error implements Transaction{

    public Error() { System.out.println("Fatal Error: This is not a valid transaction code"); }
}
