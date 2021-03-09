package main;
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

    public float getPriceWithDiscount(boolean saleToggle) { //a more appropriate name would be onSale
        float chargedPrice = this.price;//if you are using an attribute you don't need to make it a local var
        if (saleToggle) { //check if auction sale is on and get correct price
            //just make this the return, you don't need thar var
            chargedPrice = (float) Math.round((price * (1 - this.discount)) * 100) / 100; // decimal places remain at 2
        }
        return chargedPrice;
    }
}