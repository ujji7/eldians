package test;
//NEVER CHECKS NEGATIVE PRICES
import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// source for how to test print statements
//https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println

public class AbstractUserTest {
    AdminUser adminUser1;
    BuyUser buyUser1;
    BuyUser buyUserOOF;
    SellUser sellUser1;
    FullStandardUser fullStandardUser1;
    Game monopoly, pacman, pacman1, sonic;
    Marketplace market;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        adminUser1 = new AdminUser.UserBuilder("diego").balance(42).build();
        buyUser1 = new BuyUser.UserBuilder("dora").balance(41.58).build();
        buyUserOOF = new BuyUser.UserBuilder("map").balance(20.0).build();
        sellUser1 = new SellUser.UserBuilder("boots").balance(34.13).build();
        fullStandardUser1 = new FullStandardUser.UserBuilder("swiper").balance(32.40).build();
        monopoly = new Game("Monopoly", 23.5, "boots", 1, 00.0);
        pacman = new Game("Pacman", 20.0, "swiper", 2 , 10.0);
        pacman1 = new Game("Pacman", 22.0, "swiper", 3 , 0 );
        sonic = new Game("Sonic", 22.0, "swiper", 4 , 0 );
        market = new Marketplace();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSellEasy() {
        sellUser1.sell(monopoly, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.5" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow.\r\n";
        assertEquals(result1 + result, outContent.toString());
        assertEquals(monopoly ,market.getGamesOnSale().get(sellUser1.getUsername()).get(0));
    }
    // Test for a seller trying to sell the same game(game title) again[Maybe implement it in application?]
    // Test for full-standard trying to sell a game in their Inventory

    @Test
    public void testSellFromBuyUser() {
        Game pacmanR = new Game("Pacman returns", 20.0, "dora", 2 , 10.0);
        buyUser1.sell(pacmanR, market);
        String result = "ERROR: \\< Failed Constraint: dora does not have the ability to sell games.\\>";
        assertEquals(result + "\r\n", outContent.toString());
        assertNull(market.getGamesOnSale().get(sellUser1.getUsername()));
    }

    @Test
    public void testSellFromBothUser() {
        fullStandardUser1.sell(pacman, market);
        String result1 = "Seller: swiper added to the market\r\n";
        String result = "Game: Pacman" + " is now being sold by " + "swiper" + " for $" +
                "20.0" + " at a " + "10.0" +"% discount, will be available for purchase tomorrow.\r\n";
        assertEquals(result1 + result, outContent.toString());
        assertEquals(pacman ,market.getGamesOnSale().get(fullStandardUser1.getUsername()).get(0));
    }

    @Test
    public void testSellFromAdminUser() {
        Game monopolyAdmin = new Game("MonopolyForAdmins", 53.5, "diego", 2, 00.0);
        adminUser1.sell(monopolyAdmin, market);
        String result1 = "Seller: diego added to the market\r\n" +
                "Game: MonopolyForAdmins is now being sold by diego for $53.5 at a 0.0% discount, will be available for " +
                "purchase tomorrow.\r\n";
                
        String result = "Game: MonopolyForAdmins" + " is now being sold by " + "diego" + " for $" +
                "53.50" + " at a " + "0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result1, outContent.toString());
        System.out.println(market.getGamesOnSale().get(adminUser1.getUsername()));
        assertEquals(monopolyAdmin ,market.getGamesOnSale().get(adminUser1.getUsername()).get(0));
    }

    //check this out
    @Test
    public void testSellFromSellButAlreadyBought() {
        sellUser1.sell(monopoly, market);
        monopoly.changeHold();
        fullStandardUser1.buy(sellUser1, monopoly, false, market);
        fullStandardUser1.sell(monopoly, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.5" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow.\r\n";
        String result2 = "swiper" + " has bought " + "Monopoly" + " from " + "boots" + " for "
                + "$23.5" + ".\r\n";
        String result3 = "ERROR: \\< Failed Constraint: swiper could not sell Monopoly as they have " +
                "bought this exact game.\\>\r\n";
        assertEquals(result1 + result+result2+result3, outContent.toString());
    }

    // tests to check validness of game

    @Test
    public void testSellConstraintsTooHighPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 1000.0, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\< Failed Constraint: boots could not sell Monopoly2.0 for $1000.0 as it " +
                "exceeds the maximum sale price.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsMaxPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 999.99, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly2.0" + " is now being sold by " + "boots" + " for $" +
                "999.99" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow.";
        assertEquals(result1 + result + "\r\n", outContent.toString());
    }

    @Test
    public void testSellConstraintsNegativePrice() {
        Game monopolyHigh = new Game("Monopoly2.0", -99.99, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\< Failed Constraint: boots could not sell Monopoly2.0 for $-99.99 as the price " +
                "cannot be negative.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    public void testSellConstraintsZeroPrice() {
        Game monopolyHigh = new Game("Monopoly2.0", 0.0, "boots", 2, 0.00);
        sellUser1.sell(monopolyHigh, market);
        String result1 = "Seller: boots added to the market";
        String result = "Game: Monopoly2.0" + " is now being sold by " + "boots" + " for $" +
                "0.0" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow." + "\r\n";
        assertEquals(result1 + "\r\n" + result, outContent.toString());
    }

    @Test
    public void testSellConstraintsNameExactLength() { //25 characters
        Game monopolyHigh = new Game("Monopoly2.0Monopoly2.0Mon", 12.0, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly2.0Monopoly2.0Mon" + " is now being sold by " + "boots" + " for $" +
                "12.0" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow." + "\r\n";
        assertEquals(result1 + result, outContent.toString());
    }

    @Test
    public void testSellConstraintsNameTooLong() { //26 characters
        Game monopolyHigh = new Game("Monopoly2.0Monopoly2.0Mono", 12.0, "boots", 2, 00.0);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\< Failed Constraint: boots could not sell Monopoly2.0Monopoly2.0Mono " +
                "for $12.0 as it exceeds the maximum name length.\\>";
        assertEquals(result + "\r\n", outContent.toString());
    }

    @Test
    public void testSellConstraintsDiscountExact() {
        Game monopolyHigh = new Game("Monopoly2.0", 12.0, "boots", 2, 90.0);
        sellUser1.sell(monopolyHigh, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly2.0" + " is now being sold by " + "boots" + " for $" +
                "12.0" + " at a " + "90.0" +"% discount, will be available for purchase tomorrow.\r\n";
        assertEquals(result1 + result, outContent.toString());
    }

    //can discount have decimals? like 90.1% if not change this to 91%
    @Test
    public void testSellConstraintsDiscountTooHigh() {
        Game monopolyHigh = new Game("Monopoly2.0", 12.0, "boots", 2, 90.1);
        sellUser1.sell(monopolyHigh, market);
        String result = "ERROR: \\< Failed Constraint: boots could not sell Monopoly2.0 with 90.1% discount as " +
                "it exceeds the maximum discount amount.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    //tests for if seller is already in market

    @Test
    public void testSellConstraintsAlreadyInMarket() {
        sellUser1.sell(monopoly, market);
        Game monopolyHigh = new Game("Monopoly2.0", 22.0, "boots", 3, 50);
        sellUser1.sell(monopolyHigh, market);
        String result1 = "Seller: boots added to the market\r\n";
        String result = "Game: Monopoly" + " is now being sold by " + "boots" + " for $" +
                "23.5" + " at a " + "0.0" +"% discount, will be available for purchase tomorrow.\r\n";

        String result2 = "Game: Monopoly2.0" + " is now being sold by " + "boots" + " for $" +
                "22.0" + " at a " + "50.0" +"% discount, will be available for purchase tomorrow.\r\n";
        assertEquals(result1 + result + result2, outContent.toString());
        assertEquals(monopoly ,market.getGamesOnSale().get(sellUser1.getUsername()).get(0));
        assertEquals(monopolyHigh, market.getGamesOnSale().get(sellUser1.getUsername()).get(1));
    }


    // Test for Buy
    @Test
    // Test when right type of User to buy a game
    public void testSimpleBuy(){
        sellUser1.sell(monopoly, market);
        monopoly.changeHold();
        String sell = "Seller: boots added to the market\r\nGame: Monopoly is now being sold by boots for $23.5 at a " +
                "0.0% discount, will be available for purchase tomorrow.\r\n";
        buyUser1.buy(sellUser1, monopoly, market.getAuctionSale(), market);
        String result = "dora" + " has bought Monopoly from boots for $23.5.\r\n";
        assertEquals(sell + result, outContent.toString());
    }

    // Test for buying a game with AuctionSale on
    @Test
    public void auctionSaleBuy(){
        fullStandardUser1.sell(pacman, market);
        pacman.changeHold();
        buyUser1.buy(fullStandardUser1, pacman, true , market);
        String result1 = "Seller: swiper added to the market\r\n" +
                "Game: Pacman is now being sold by swiper for $20.0 at a 10.0% discount, will be available for " +
                "purchase tomorrow.\r\n";
        String result = "dora" + " has bought Pacman from swiper for $18.0.\r\n";
        assertEquals(result1 + result, outContent.toString());
    }

    // Test for right type of user to buy a game already in their inventory
    @Test
    public void buyAgainCheck(){
        fullStandardUser1.sell(pacman, market);
        pacman.changeHold();
        buyUser1.buy(fullStandardUser1, pacman, market.getAuctionSale(), market);
        String before = "Seller: swiper added to the market\r\n" +
                "Game: Pacman is now being sold by swiper for $20.0 at a 10.0% discount, will be available for " +
                "purchase tomorrow.\r\n" +
                "dora has bought Pacman from swiper for $20.0.\r\n";
        buyUser1.buy(fullStandardUser1, pacman, market.getAuctionSale(), market);
        String result = "ERROR: \\< Failed Constraint: dora already owns Pacman. Can't buy it again.\\>\r\n";
        assertEquals(before + result, outContent.toString());
    }

    // Test for user with insuffcient funds trying to buy a game
    @Test
    public void outOfFundsCheck(){
        sellUser1.sell(sonic, market);
        sonic.changeHold();
        buyUserOOF.buy(sellUser1, sonic, market.getAuctionSale(), market);
        String result1 = "Seller: boots added to the market\r\n" +
                "Game: Sonic is now being sold by boots for $22.0 at a 0.0% discount, will be available for purchase" +
                " tomorrow.";
        String result = "ERROR: \\< Failed Constraint: map doesn't have enough funds to buy Sonic.\\>\r\n";
        assertEquals(result1 + "\r\n" + result, outContent.toString());
    }

    // Test for Full-Standard to buy a game they have up for sale
    @Test
    public void fullStanBuyGameFromTheirItemList() {
        fullStandardUser1.getInventory().add(pacman1);
        pacman1.changeHold();
        sellUser1.sell(pacman1, market);
        String before = "Seller: boots added to the market\r\n" +
                "Game: Pacman is now being sold by boots for $22.0 at a 0.0% discount, will be available for " +
                "purchase tomorrow.\r\n";
        fullStandardUser1.buy(sellUser1, pacman1, true, market);
        String result = "ERROR: \\< Failed Constraint: swiper already owns Pacman. Can't buy it again.\\>\r\n";
        assertEquals(before + result, outContent.toString());
    }
    // Test for Seller's account maxing out upon the purchase
    @Test
    public void accountMaxOnBuy(){
        FullStandardUser bezoz = new FullStandardUser.UserBuilder("Jeff").balance(999999.99).build();
        Game amazon = new Game("Alexa", 22, "Jeff", 03, 0);
        bezoz.sell(amazon, market);
        amazon.changeHold();
        adminUser1.buy(bezoz, amazon, market.getAuctionSale(), market);
        String result1 = "Seller: Jeff added to the market\r\n" +
                "Game: Alexa is now being sold by Jeff for $22.0 at a 0.0% discount, will be available for " +
                "purchase tomorrow.\r\n" +
                "diego has bought Alexa from Jeff for $22.0.\r\n";
        String result =  "Warning: diego's balance was maxed out upon sale of game.\r\n";
        assertEquals(result1 + result, outContent.toString());

    }

    // Test for standard-Sell to buy a game[Maybe implement it in application?]
    @Test
    public void fullStanBuy(){
        sellUser1.sell(sonic, market);
        sonic.changeHold();
        String before = "Seller: boots added to the market\r\n" +
                "Game: Sonic is now being sold by boots for $22.0 at a 0.0% discount, will be available for " +
                "purchase tomorrow.\r\n";
        fullStandardUser1.buy(sellUser1, sonic, market.getAuctionSale(), market);
        String result = "swiper " + "has bought Sonic from boots for $22.0.\r\n";
        assertEquals(before + result, outContent.toString());
    }

    // Test for Admin to buy a game
    public void adminBuy(){
        adminUser1.buy(sellUser1, sonic, market.getAuctionSale(), market);
        String result = "diego " + "has bought Sonic from boots for 22.00.";
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

