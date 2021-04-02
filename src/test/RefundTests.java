package test;

import jdk.jfr.StackTrace;
import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// source for how to test print statements
//https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println

public class RefundTests {

    AdminUser adminUser1;
    BuyUser refundUser1;
    SellUser refundUser2;
    BuyUser refundUser3;
    SellUser refundUser4;
    BuyUser refundUserMax;
    SellUser refundSellToMax;
    BuyUser refundBuy;
    SellUser refundSell;
    FullStandardUser refundFS;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));

        adminUser1 = new AdminUser("DariusZach", 0.00);

        refundUser1 = new BuyUser("Armin", 0.00);
        refundUser2 = new SellUser("Bertholdt", 0.00);
        refundUser3 = new BuyUser("Reiner", 13.35);
        refundUser4 = new SellUser("Ymir", 25.65);
        refundUserMax = new BuyUser("Eren", 999999.99);
        refundSellToMax = new SellUser("Mikasa", 101.00);
        refundBuy = new BuyUser("Hange", 100.00);
        refundSell = new SellUser("Erwin", 100.00);
        refundFS = new FullStandardUser("Levi", 100.00);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Tests that an invalid refund returns false, the correct error statement is printed to console and both
     * User's AccountBalance has not been changed.
     */
    @Test
    public void testRefundFromUserWithoutFunds() {
        boolean worked = adminUser1.refund(refundUser1, refundUser2, 15.65f);
        String result = "ERROR: \\ < Failed Constraint: " + "Bertholdt" + " could not make a refund to " + "Armin" +
                " for $" + "15.65" + " due to insufficient funds. > //";
        assertEquals(result, outContent.toString());
        assertEquals(worked, false);
        assertEquals(refundUser1.getAccountBalance(), 0.00f);
        assertEquals(refundUser2.getAccountBalance(), 0.00f);
    }

    /**
     * Tests that a valid refund returns true, and that the correct amount is taken from the sellers AccountBalace
     * and is added to the buyer's AccountBalance.
     */
    @Test
    public void testValidRefund() {
        boolean worked = adminUser1.refund(refundUser3, refundUser4, 12.65f);
        assertEquals(worked, true);
        assertEquals(refundUser3.getAccountBalance(), 26.00f);
        assertEquals(refundUser4.getAccountBalance(), 13.00f);
    }

    /**
     * Tests that BuyUser cannot issue a refund.
     */
    @Test
    public void testBuyRefund() {
        boolean worked = refundBuy.refund(refundSell, refundFS, 1.00f);
        String result = "ERROR: \\ < Failed Constraint: " + refundBuy.getUsername() + " does not have the ability to " +
                "issue " + "a refund.";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a SellUser cannot issue a refund.
     */
    @Test
    public void testSellRefund() {
        boolean worked = refundSell.refund(refundBuy, refundFS, 1.00f);
        String result = "ERROR: \\ < Failed Constraint: " + refundBuy.getUsername() + " does not have the ability to " +
                "issue " + "a refund.";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a FullStandardUser cannot issue a refund.
     */
    @Test
    public void testFSRefund() {
        boolean worked = refundFS.refund(refundBuy, refundSell, 1.00f);
        String result = "ERROR: \\ < Failed Constraint: " + refundBuy.getUsername() +
                " does not have the ability to issue a refund.";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a User who has max AccountBalance cannot recieve a refund
     */
    @Test
    public void testMaxRefund() {
        boolean worked = adminUser1.refund(refundUserMax, refundSellToMax, 1.00f);
        String result = "ERROR: \\ < Failed Constraint: "+ refundUserMax.getUsername() +
                " balance was Maxed up upon addition of more funds!";
    }
}
