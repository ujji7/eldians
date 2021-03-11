package Test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// source for how to test print statements
//https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println

public class AbstractUserTest {
    AdminUser adminUser1;
    BuyUser buyUser1;
    SellUser sellUser1;
    FullStandardUser fullStandardUser1;
    Game monopoly, pacman, pacman1, sonic;
    Marketplace market;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        adminUser1 = new AdminUser("diego", 22.00f);
        buyUser1 = new BuyUser("dora", 41.58f);
        sellUser1 = new SellUser("boots", 34.13f);
        fullStandardUser1 = new FullStandardUser("swiper", 32.40f);

        monopoly = new Game("Monopoly", 23.5f, "boots", 1, 00.0);
        pacman = new Game("Pacman", 20.0f, "swiper", 2 , 10.0);

        pacman1 = new Game("Pacman", 22.0f, "swiper", 2 , 0 );
        sonic = new Game("Sonic", 22.0f, "swiper", 2 , 0 );
        market = new Marketplace();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSellEasy() {
        sellUser1.sell(monopoly, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
        assertEquals(monopoly ,market.getGamesOnSale().get(sellUser1)[0]);
    }
    // Test for a seller trying to sell the same game(game title) again[Maybe implement it in application?]
    // Test for full-standard trying to sell a game in their Inventory

    @Test
    public void testSellFromBuyUser() {
        Game pacmanR = new Game("Pacman returns", 20.0f, "dora", 2 , 10.0);
        buyUser1.sell(pacmanR, market);
        String result = "ERROR: \\ < Failed Constraint: "+ "dora" + " cannot sell games.";
        assertEquals(result, outContent.toString());
        assertNull(market.getGamesOnSale().get(sellUser1));
    }

    @Test
    public void testSellFromBothUser() {
        fullStandardUser1.sell(pacman, market);
        String result = "Game: Pacman" + " is now being sold by " + "swiper" + " for $" +
                "23.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
        assertEquals(pacman ,market.getGamesOnSale().get(fullStandardUser1)[0]);
    }

    @Test
    public void testSellFromAdminUser() {
        Game monopolyAdmin = new Game("MonopolyForAdmins", 53.5f, "diego", 2, 00.0);
        adminUser1.sell(monopolyAdmin, market);
        String result = "Game: MonopolyForAdmins" + " is now being sold by " + "diego" + " for $" +
                "53.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
        assertEquals(monopolyAdmin ,market.getGamesOnSale().get(adminUser1)[0]);
    }

    //check this out
    @Test
    public void testSellFromSellButAlreadyBought() {
        sellUser1.sell(monopoly, market);
        fullStandardUser1.buy(sellUser1, monopoly, false);
        fullStandardUser1.sell(monopoly, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        String result2 = "swiper" + " has bought " + "Monopoly" + " from " + "boots" + " for "
                + "23.50" + ".";
        String result3 = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly" + " as User has bought this exact game. > //";
        assertEquals(result+result2+result3, outContent.toString());
    }

    // tests to check validness of game

    @Test
    public void testSellConstraintsTooHighPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 1000.0f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly2.0 + " + "for $" + "1000.0" + " as it exceeds the maximum sale price. > //";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsMaxPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 999.99f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "999.99" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsNegativePrice() {
        Game monopolyHigh = new Game("Monopoly2.0", -99.99f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly2.0 + " + "for $" + "-99.99" + " as the price cannot be negative. > //";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsZeroPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 0.0f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "0.0" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsNameExactLength() { //25 characters
        Game monopolyHigh = new Game("Monopoly2.0Monopoly2.0Mon", 12.0f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "12.0" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsNameTooLong() { //26 characters
        Game monopolyHigh = new Game("Monopoly2.0Monopoly2.0Mono", 12.0f, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly2.0Monopoly2.0Mono" + " for $" + "12.0" + " as it exceeds the maximum name length. > //";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsDiscountExact() {
        Game monopolyHigh = new Game("Monopoly2.0", 12.0f, "boots", 2, 90.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "12.0" + " at a " + "90" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result, outContent.toString());
    }

    //can discount have decimals? like 90.1% if not change this to 91%
    @Test
    public void testSellConstraintsDiscountTooHigh() {
        Game monopolyHigh = new Game("Monopoly2.0", 12.0f, "boots", 2, 90.1);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly2.0" + " with " + "90.1" + "% discount as it exceeds the maximum discount " +
                "amount. > //";
        assertEquals(result, outContent.toString());
    }


    //this tests for already selling the game and uses game name
    // may have to edit result to include the first sell's print statement
    @Test
    public void testSellConstraintsAlreadySelling() {
        sellUser1.sell(monopoly, market);
        Game monopolyHigh = new Game("Monopoly", 12.0f, "boots", 2, 90.1);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\ < Failed Constraint: " + "boots" + " could not sell " +
                "Monopoly" + " as User is already selling this exact game > //";
        assertEquals(result, outContent.toString());
    }

    //tests for if seller is already in market

    @Test
    public void testSellConstraintsAlreadyInMarket() {
        sellUser1.sell(monopoly, market);
        Game monopolyHigh = new Game("Monopoly2.0", 22.0f, "boots", 3, 50);
        sellUser1.sell(monopolyHigh, market);
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";

        String result2 = "Game: Monopoly2.0" + " is now being sold by " + "boots" + " for $" +
                "22.0" + " at a " + "50" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result+result2, outContent.toString());
        assertEquals(monopoly ,market.getGamesOnSale().get(sellUser1)[0]);
        assertEquals(monopolyHigh, market.getGamesOnSale().get(sellUser1)[1]);
    }


    // Test for Buy
    @Test
    // Test when right type of User to buy a game
    public void testSimpleBuy(){
        buyUser1.buy(sellUser1, monopoly, market.getAuctionSale());
        String result = "dora" + " has bought Monopoly from boots for 23.50.";
                                                                // check about multiple print statemetns check!!
        assertEquals(result, outContent.toString());
    }

    // Test for buying a game with AuctionSale on
    @Test
    public void auctionSaleBuy(){
        fullStandardUser1.sell(pacman, market);
        buyUser1.buy(fullStandardUser1, pacman, true );
        String result = "dora" + " has bought Monopoly from swiper for 18.00.";
        assertEquals(result, outContent.toString());
    }

    // Test for right type of user to buy a game already in their inventory
    @Test
    public void buyAgainCheck(){
        buyUser1.buy(fullStandardUser1, pacman, market.getAuctionSale() );
        String result = "ERROR: \\ < Failed Constraint:  " + "dora already owns Monopoly. Cannot buy it again.";
        assertEquals(result, outContent.toString());
    }

    // Test for right type of user to buy a game already in their inventory from a different seller
    public void buyAgainFromDiffSeller(){
        sellUser1.sell(pacman1, market);
                                                            // check about multiple print statemetns check!!
        buyUser1.buy(sellUser1, pacman1, market.getAuctionSale());
        String result = "ERROR: \\ < Failed Constraint:  " + "dora already owns Monopoly. Cannot buy it again.";
        assertEquals(result, outContent.toString());
    }

    // Test for user with insuffcient funds trying to buy a game
    @Test
    public void outOfFundsCheck(){
        sellUser1.sell(sonic, market);
        buyUser1.buy(sellUser1, sonic, market.getAuctionSale());
        String result = "ERROR: \\ < Failed Constraint:  " + "dora does not have enough funds to buy Sonic.";
        assertEquals(result, outContent.toString());
    }

    // Test for Full-Standard to buy a game they have up for sale
    @Test
    public void fullStanBuyGameFromTheirItemList(){
                                                                            // Missing from current Buy()
        fullStandardUser1.buy(sellUser1, pacman1, true );
        String result = "ERROR: \\ < Failed Constraint:  " + "You can't buy Pacman as you are also selling it.";
        assertEquals(result, outContent.toString());
    }
    // Test for Seller's account maxing out upon the purchase
    @Test
    public void accountMaxOnBuy(){
        FullStandardUser bezoz = new FullStandardUser("Jeff", 999999.99f);
        Game amazon = new Game("Alexa", 22f, "Jeff", 03, 0);
        bezoz.sell(amazon, market);
        adminUser1.buy(bezoz, amazon, market.getAuctionSale());
        String result =  "ERROR: \\ < Failed Constraint: "+ "Jeff" +
                "'s balance was Maxed up upon addition of more funds!";
        assertEquals(result, outContent.toString());

    }

    // Test for standard-Sell to buy a game[Maybe implement it in application?]
    @Test
    public void fullStanBuy(){
        fullStandardUser1.buy(sellUser1, sonic, market.getAuctionSale());
        String result = "swiper " + " has bought Sonic from boots for 22.00.";
        assertEquals(result, outContent.toString());
    }

    // Test for Admin to buy a game
    public void adminBuy(){
        adminUser1.buy(sellUser1, sonic, market.getAuctionSale());
        String result = "diego " + " has bought Sonic from boots for 22.00.";
        assertEquals(result, outContent.toString());
    }

    //REFUND TEST

    // Wrong usertype trying to refund
    // Buyer's account not having enough funds
    // Seller's account maxing out
    // Requsting a refund among the wrong users (AllOtherType-Sell) (Buy-AllOtherTypes)


    // Create

    // Wrong user trying to call it(All non-priv types)
    // Admin creating themselves
    // Admin creating a username already in the System
    // Admin creating every other types of Users


    // Add Credit

    // Admin adding credits to someone's account
    // User adding credit to their account
    // User's account maxing out upon addition of new funds



    // DISCOUNT TEST [Implement in Market/Application]

    // Wrong user trying to call it(All non-priv types)
    // Toggle discount off
    // Toggle discount onn

}

