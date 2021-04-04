package main;

/** A Game class that represents a game object that is in the marketplace. Every game has a name, price, supplierID,
 * which is the seller's username, a unique id that is generated when a game is sold, and a discount that may be 
 * applied to the game. It also has an on hold attribute that signifies whether the game can be boguht/sold currently. 
 * If the on hold is true, then the game is on hold and cannot be exchanged. When a new day starts, the on hold 
 * attribute becomes turned on.
 * 
 */
public class Game{
    private String name;
    private double price;
    private String supplierID;
    private int uniqueID;
    private double discount;
    private boolean onHold = true;


    /** Create a new game with the given name, price, supplier ID, unique ID, and discount
     * 
     * @param name name of the game
     * @param price price of the game
     * @param supplierID seller of the game
     * @param uniqueID unique ID of the game
     * @param discount discount for the game
     */
    public Game(String name, double price, String supplierID, int uniqueID, double discount){
        this.name = name;
        this.price = price;
        this.supplierID = supplierID;
        this.uniqueID = uniqueID;
        this.discount = discount;
    }

    /** Changes the on hold attribute to false.
     * 
     */
    public void changeHold(){
        this.onHold = false;
    }

    /** Returns the on hold attribute
     * 
     * @return the on hold attribute
     */
    public boolean getHold(){
        return this.onHold;
    }

    /** Returns the name of the game
     * 
     * @return the name of the game
     */
    public String getName(){
        return this.name;
    }

    /** Returns the price of the game
     * 
     * @return the price of the game
     */
    public double getPrice(){
        return this.price;
    }

    /** Returns the game's seller
     * 
     * @return the seller of the game
     */
    public String getSupplierID(){
        return  this.supplierID;
    }

    /** Return the unique ID of the game
     * 
     * @return the game's unique ID
     */
    public int getUniqueID(){
        return this.uniqueID;
    }

    /** Return the discount of the game
     * 
     * @return the game's discount
     */
    public double getDiscount(){return this.discount;}

    // will need to override the .equal() method to check if this is the game for addition of game in inventory

    /** Returns the price of the game, taking into account any applicable discount. If auction 
     * 
     * @param saleToggle true if the auction sale is on, false otherwise
     * @return the price of the game, taking into account applicable discounts
     */
    public double getPriceWithDiscount(boolean saleToggle) { //a more appropriate name would be onSale
        if (saleToggle) { //check if auction sale is on and get correct price
            return (double) Math.round((price * (1 - this.discount/100)) * 100) / 100; // decimal places remain at 2
        }
        return this.price;
    }
}