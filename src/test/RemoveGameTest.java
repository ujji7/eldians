package test;

import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveGameTest {
    
    AdminUser A1;
    BuyUser B1;
    SellUser S1;
    FullStandardUser F1;
    Game G1, G2, G3, G4;
    Marketplace market;
    boolean sale;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    String result = "Seller: F1 added to the market\r\n" +
            "Game: G2 is now being sold by F1 for $20.0 at a 10.0% discount, will be available for purchase tomorrow.\r\n" +
            "B1 has bought G2 from F1 for $20.0.\r\n";

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        A1 = new AdminUser.UserBuilder("A1").balance(42).build();
        B1 = new BuyUser.UserBuilder("B1").balance(41.58).build();
        S1 = new SellUser.UserBuilder("S1").balance(34.13).build();
        F1 = new FullStandardUser.UserBuilder("F1").balance(32.40).build();
        G1 = new Game("G1", 23.5, "S1", 1, 00.0);
        G2 = new Game("G2", 20.0, "F1", 2, 10.0);
        G3 = new Game("G3", 22.0, "F1", 3, 0);
        G3.changeHold(); //
        market = new Marketplace();
        sale = market.getAuctionSale();
        F1.sell(G2, market);
        G2.changeHold();
        B1.buy(F1, G2, sale, market); //now cannot remove it from inv
        
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        A1 = new AdminUser.UserBuilder("A1").balance(42).build();
        B1 = new BuyUser.UserBuilder("B1").balance(41.58).build();
        S1 = new SellUser.UserBuilder("S1").balance(34.13).build();
        F1 = new FullStandardUser.UserBuilder("F1").balance(32.40).build();
        G1 = new Game("G1", 23.5, "S1", 1, 00.0);
        G2 = new Game("G2", 20.0, "F1", 2, 10.0);
        G3 = new Game("G3", 22.0, "F1", 3, 0);
        G3.changeHold(); //
        market = new Marketplace();
        sale = market.getAuctionSale();
        F1.sell(G2, market);
        G2.changeHold();
        B1.buy(F1, G2, sale, market);
        
    }
    
    
    @Test
    public void testRemoveInvGood() {
        Game gamma = null;
        for (Game g : B1.getInventory()) {
            if (g.getName() == "G2") {
                gamma = g;
            }
        }
        assertNotNull(gamma);
        gamma.changeHold();
        B1.removeGame(gamma, market);
        String result1 = "G2 was removed from the user's inventory.\r\n";
        assertEquals(result + result1, outContent.toString());
    }

    //remove from game sold exists
    @Test
    public void testRemoveSaleGood() {
        F1.removeGame(G2, market);
        String result1 = "G2 was removed from the User's offering on the Market.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    
    //remove from game no seller
    @Test
    public void testRemoveSaleNoSeller() {
        S1.removeGame(G2, market);
        String result1 = "Seller: S1 does not exist in the market.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    //remove game from inventory on hold should not go thru
    @Test
    public void testRemoveInvNotToday() {
        B1.removeGame(G2, market);
        String result1 = "G2 cannot be removed as it is on hold.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    //remove game from on sale on hold should not go through
    @Test
    public void testRemoveSaleNotToday() {
        S1.sell(G1, market);
        S1.removeGame(G1, market);
        String result1 = "Seller: S1 added to the market\r\n" +
                "Game: G1 is now being sold by S1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "Game: G1, sold by S1 is currently on hold, cannot be exchanged today.\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
    }
    
    // remove game doesn't exist anywhere
    @Test
    public void testRemoveNoGame() {
        F1.removeGame(G3, market);
        String result2 = "Seller: F1 is currently not offering G3.\r\n" +
                "G3 was not found in the User's inventory.\r\n";
        assertEquals(result + result2, outContent.toString());
    }
    
    // remove game for admin - check first mkt, then inv - but game has hold - THIS HAS AN EXTRA THING
    @Test
    public void testRemoveAdminSaleNotToday() {
        A1.sell(G1, market);
        A1.removeGame(G1, market);
        String result1 = "Seller: A1 added to the market\r\n" +
                "Game: G1 is now being sold by A1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "Game: G1, sold by A1 is currently on hold, cannot be exchanged today.\r\n";
        String result3 = "G1 was not found in the User's inventory.\r\n";
        assertEquals(result + result1 + result2 + result3, outContent.toString());
    }

    // remove game for admin - check first mkt, then inv - game can be exchanged
    @Test
    public void testRemoveAdminSaleGood() {
        A1.sell(G1, market);
        G1.changeHold();
        A1.removeGame(G1, market);
        String result1 = "Seller: A1 added to the market\r\n" +
                "Game: G1 is now being sold by A1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "G1 was removed from the User's offering on the Market.\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
    }
    
    //remove game for admin inventory from inventory other - check first mkt, then inv
    @Test
    public void testRemoveAdminInvGood() {
        A1.buy(F1, G2, sale, market);
        Game x = G2;
        x.changeHold();
        A1.removeGame(x, market);
        String result1 = "A1 has bought G2 from F1 for $20.0.\r\n";
        String result3 = "Seller: A1 does not exist in the market.\r\n" +
                "G2 cannot be removed as it is on hold.\r\n";
        String result2 = "G2 was removed from the user's inventory.\r\n";
        assertEquals(result + result1 + result3 + result2, outContent.toString());
    }

    //remove game for admin for other user - check first mkt, then inv
    @Test
    public void testRemoveAdminOtherSeller() {
        
        A1.removeGame(G2, F1, market);
        String result1 = "G2 was removed from the User's offering on the Market.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    //remove game for admin from other buy user's inventory
    @Test
    public void testRemoveAdminOtherBuyer() {
        Game gamma = null;
        for (Game g : B1.getInventory()) {
            if (g.getName() == "G2") {
                gamma = g;
            }
        }
        assertNotNull(gamma);
        gamma.changeHold();
        A1.removeGame(gamma, B1, market);
        String result1 = "G2 was removed from the user's inventory.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
}
