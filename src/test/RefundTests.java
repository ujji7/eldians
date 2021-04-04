package test;

import main.AdminUser;
import main.BuyUser;
import main.FullStandardUser;
import main.SellUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        adminUser1 = new AdminUser.UserBuilder("diego").balance(42).build();
        refundUser1 = new BuyUser.UserBuilder("Armin").balance(0.00).build();
        refundUser2 = new SellUser.UserBuilder("Bertholdt").balance(0.00).build();
        refundUser3 = new BuyUser.UserBuilder("Reiner").balance(13.35).build();
        refundUser4 = new SellUser.UserBuilder("Ymir").balance(25.65).build();
        refundUserMax = new BuyUser.UserBuilder("Eren").balance(999999.99).build();
        refundSellToMax = new SellUser.UserBuilder("Mikasa").balance(101.00).build();
        refundBuy = new BuyUser.UserBuilder("Hange").balance(100.00).build();
        refundSell = new SellUser.UserBuilder("Erwin").balance(100.00).build();
        refundFS = new FullStandardUser.UserBuilder("Levi").balance(100.00).build();
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
        boolean worked = adminUser1.refund(refundUser1, refundUser2, 15.65);
        String result = "ERROR: \\< Failed Constraint: Bertholdt could not make a refund to Armin for $15.65 " +
                "due to insufficient funds.\\>\r\n";
        assertEquals(result, outContent.toString());
        assertEquals(worked, false);
        assertEquals(refundUser1.getAccountBalance(), 0.00);
        assertEquals(refundUser2.getAccountBalance(), 0.00);
    }

    /**
     * Tests that a valid refund returns true, and that the correct amount is taken from the sellers AccountBalace
     * and is added to the buyer's AccountBalance.
     */
    @Test
    public void testValidRefund() {
        boolean worked = adminUser1.refund(refundUser3, refundUser4, 12.65);
        assertEquals(worked, true);
        assertEquals(26.00, refundUser3.getAccountBalance());
        assertEquals(13.00, refundUser4.getAccountBalance());
    }

    /**
     * Tests that BuyUser cannot issue a refund.
     */
    @Test
    public void testBuyRefund() {
        boolean worked = refundBuy.refund(refundSell, refundFS, 1.00);
        String result = "ERROR: \\< Failed Constraint: Hange does not have the ability to issue a refund.\\>\r\n";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a SellUser cannot issue a refund.
     */
    @Test
    public void testSellRefund() {
        boolean worked = refundSell.refund(refundBuy, refundFS, 1.00);
        String result = "ERROR: \\< Failed Constraint: Erwin does not have the ability to issue a refund.\\>\r\n";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a FullStandardUser cannot issue a refund.
     */
    @Test
    public void testFSRefund() {
        boolean worked = refundFS.refund(refundBuy, refundSell, 1.00);
        String result = "ERROR: \\< Failed Constraint: Levi does not have the ability to issue a refund.\\>\r\n";
        assertEquals(worked, false);
        assertEquals(result, outContent.toString());
    }

    /**
     * Tests that a User who has max AccountBalance cannot recieve a refund
     */
    @Test
    public void testMaxRefund() {
        boolean worked = adminUser1.refund(refundUserMax, refundSellToMax, 1.00);
        String result = "ERROR: \\ < Failed Constraint: "+ refundUserMax.getUsername() +
                " balance was Maxed up upon addition of more funds!";
    }
}
