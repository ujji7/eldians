package test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// source for how to test print statements
//https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println

public class AbstractUserTest {
    AdminUser adminUser1;
    BuyUser buyUser1;
    SellUser sellUser1;
    FullStandardUser fullStandardUser1;
    Game monopoly;
    Marketplace market;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        adminUser1 = new AdminUser("diego", 0.00f);
        buyUser1 = new BuyUser("dora", 34.08f);
        sellUser1 = new SellUser("boots", 34.13f);
        fullStandardUser1 = new FullStandardUser("swiper", 32.40f);
        monopoly = new Game("Monopoly", 23.5f, "sellUser1", 1, 00);
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
    }

    @Test
    public void testSellFromBuyUser() {
        buyUser1.sell(monopoly, market);
        String result = "ERROR: \\ < Failed Constraint: "+ "dora" + " cannot sell games.";
        assertEquals(result, outContent.toString());
    }





}

