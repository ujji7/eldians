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
