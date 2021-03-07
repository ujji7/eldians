public class Game{
    private String name;
    private float price;
    private String supplierID;
    private int uniqueID;
    private double discount;


    public Game(String name, float price, String supplierID, int uniqueID, double discount){
        this.name = name;
        this.price = price;
        this.supplierID = supplierID;
        this.uniqueID = uniqueID;
        this.discount = discount;
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

    public double getDiscount(){return this.discount;}

    // will need to override the .equal() method to check if this is the game for addition of game in inventory




}