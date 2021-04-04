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

public class AddCreditTest {
    AdminUser adminUser1;
    BuyUser buyUser1;
    BuyUser buyUser2;
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
        buyUser2 = new BuyUser.UserBuilder("benny").balance(999998.00).build();
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
    // Admin add credit to
    public void adminAddCreditTo(){
        adminUser1.addCreditTo(100.00, buyUser1);
        String result = "$100.00 was added to the user's account";
        assertEquals(result, outContent.toString());
    }

    @Test
    // admin add credit to self
    public void adminAddCreditToSelf(){
        adminUser1.addCreditTo(100.00, adminUser1);
        String result = "$100.00 was added to the user's account";
        assertEquals(result, outContent.toString());
    }

    @Test
    // admin add credit
    public void adminAddCredit(){
        adminUser1.addCredit(100.00);
        String result = "$100.00 was added to the user's account";
        assertEquals(result, outContent.toString());    }

    @Test
    //Regular add credit
    public void standardAddCredit(){
        sellUser1.addCredit(100.00);
        String result = "$100.00 was added to the user's account";
        assertEquals(result, outContent.toString());
    }

    @Test
    //add credit passes the max for the day
    public void passDailyMax(){
        fullStandardUser1.addCredit(1001.99);
        String result = "ERROR: \\ < Failed Constraint: swiper's daily limit would" +
                " be reached upon addition of funds!\n" +
                "You can only add $965.87 to the account for the rest of today.";
        assertEquals(result, outContent.toString());
    }

    @Test
    // passes the user max
    public void passUserMax(){
        buyUser2.addCredit(2.00);
        String result = "ERROR: \\ < Failed Constraint: benny"+
                "'s balance was Maxed out!\n$1.99 was added to the account"
    }

}

//package test;
//import main.*;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class addCreditTest {
//    // attribute declaration
//    BuyUser buyUser;
//    SellUser sellUser;
//    FullStandardUser fsUser;
//    AdminUser adminUser;
//    BuyUser nearMax;
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final PrintStream originalOut = System.out;
////    Marketplace marketplace;
////    Marketplace marketCopy;
////    Game mariokart;
////    Game BOTW;
////    Game Spiritfarer;
////    Game COD;
////    Game CSGO;
////    Game Valheim;
//
//    /* Test helpers */
//    private void assertMarketsEqual(Marketplace actual, Marketplace expected){
//        actual.getGamesOnSale().equals(expected.getGamesOnSale());
//    }
//
//    @BeforeEach
//    public void setUpStreams(){
//        System.setOut(new PrintStream(outContent));
//        buyUser = new BuyUser("doja", 300.00f);
//        sellUser = new SellUser("Megan", 9000.00f);
//        fsUser = new FullStandardUser("Nicki", 1000.00f);
//        adminUser = new AdminUser("Rihanna", 1.00f);
//        nearMax = new BuyUser("SZA", 998990.99f);
//    }
//
//    @AfterEach
//    public void restoreStreams() {
//        System.setOut(originalOut);
//    }
//
//    @Test
//    public void testAddCreditBuyUser(){
//        float amount = 1001.00f;
//        buyUser.addCredit(amount);
//        String result = "$" + amount + " added to" + buyUser.username;
//        assertEquals(result, outContent.toString());
//    }
//
//    @Test
//    public void testAddCreditSellUser(){
//        float amount = 1001.00f;
//        sellUser.addCredit(amount);
//        String result = "$" + amount + " added to" + sellUser.username;
//        assertEquals(result, outContent.toString());
//    }
//
//    @Test
//    public void testAddCreditFullStandardUser(){
//        float amount = 1001.00f;
//        fsUser.addCredit(amount);
//        String result = "$" + amount + " added to" + fsUser.username;
//        assertEquals(result, outContent.toString());
//    }
//
//    @Test
//    public void testAddCreditAdminUser(){
//        float amount = 1001.00f;
//        adminUser.addCredit(amount);
//        String result = "$" + amount + " added to" + adminUser.username;
//        assertEquals(result, outContent.toString());
//    }
//
//    @Test
//    public void testAddCreditTo(){
//        float amount = 1007.00f;
//        adminUser.addCreditTo(amount, sellUser);
//        String result = "$" + amount + " added to" + sellUser.getUsername();
//        assertEquals(result, outContent.toString());
//    }
//
//    //account + amount == MAX
//    @Test
//    public void testAmountToMax(){
//        float amount = 1009.00f;
//        nearMax.addCredit(amount);
//        String result = "$" + amount + " added to" + sellUser.username;
//        assertEquals(result, outContent.toString());
//    }
//
//    // account + amount > MAX
//    @Test
//    public void testAmountPassMax(){
//        float amount = 2009.00f;
//        nearMax.addCredit(amount);
//        String result = "ERROR: \\ < Failed Constraint: " + nearMax.username +
//                " daily limit be reached! No funds were added";
//        assertEquals(result, outContent.toString());
//    }
//}
