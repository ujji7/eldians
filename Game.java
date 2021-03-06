public class Game{
    public String name;
    public float price;
    public String supplierID;
    public int uniqueID;


    public Game(String name, float price, String supplierID, int uniqueID){
        this.name = name;
        this.price = price;
        this.supplierID = supplierID;
        this.uniqueID = uniqueID;
    }

    public String getName(){
        return this.name;
    }

    public float getPrice(){
        return this.price;
    }

    public String getSupplierID(){
        return  this.supplierID;
    }

    public int getUniqueID(){
        return this.uniqueID;
    }

    // will need to override the .equal() method to check if this is the game



}