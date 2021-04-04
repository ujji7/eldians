package test;

import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transactions.Finder;

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
        G3.changeHold(); // game can be sold
        market = new Marketplace();
        sale = market.getAuctionSale();
        F1.sell(G2, market);
        G2.changeHold(); // G2 can be sold
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

        Finder find = new Finder();
        Game g = find.findGame("G2", B1.getInventory());
        g.changeHold();
        
        assertEquals(1, B1.getInventory().size());
        B1.removeGame(G2, market);
        String result1 = "G2 was removed from B1's inventory.\r\n";
        assertEquals(result + result1, outContent.toString());
        assertEquals(0, B1.getInventory().size());
    }

    //remove from game sold exists
    @Test
    public void testRemoveSaleGood() {
        assertEquals(1, market.getGamesOnSale().get(F1.getUsername()).size());
        F1.removeGame(G2, market);
        String result1 = "G2 was removed from F1's offering on the market.\r\n";
        assertEquals(result + result1, outContent.toString());
        assertEquals(0, market.getGamesOnSale().get(F1.getUsername()).size());
    }
    
    //remove from game no seller
    @Test
    public void testRemoveSaleNoSeller() {
        assertNull(market.getGamesOnSale().get(S1.getUsername()));
        S1.removeGame(G2, market);
        String result1 = "ERROR: \\< Failed Constraint: G2 is not being sold by S1, cannot be removed.\\>\r\n";
        assertEquals(result + result1, outContent.toString());
        assertNull(market.getGamesOnSale().get(S1.getUsername()));
    }
    
    //remove game from inventory on hold should not go thru
    @Test
    public void testRemoveInvNotToday() {
        assertEquals(1, B1.getInventory().size());
        B1.removeGame(G2, market);
        String result1 = "G2 cannot be removed as it is on hold.\r\n";
        assertEquals(result + result1, outContent.toString());
        assertEquals(1, B1.getInventory().size());
    }
    
    //remove game from on sale on hold should not go through
    @Test
    public void testRemoveSaleNotToday() {
        assertNull(market.getGamesOnSale().get(S1.getUsername()));
        S1.sell(G1, market);
        assertEquals(1, market.getGamesOnSale().get(S1.getUsername()).size());
        S1.removeGame(G1, market);
        String result1 = "Seller: S1 added to the market\r\n" +
                "Game: G1 is now being sold by S1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "ERROR: \\< Failed Constraint: G1 cannot be removed as it is on hold in the market.\\>\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
        assertEquals(1, market.getGamesOnSale().get(S1.getUsername()).size());
    }
    
    // remove game doesn't exist anywhere
    @Test
    public void testRemoveNoGame() {
        F1.removeGame(G3, market);
        String result2 = "ERROR: \\< Failed Constraint: G3 cannot be removed as it cannot be found.\\>\r\n";
        assertEquals(result + result2, outContent.toString());
    }
    
    // remove game for admin - check first mkt, then inv - but game has hold - THIS HAS AN EXTRA THING
    @Test
    public void testRemoveAdminSaleNotToday() {
        
        A1.sell(G1, market);
        assertEquals(1, market.getGamesOnSale().get(A1.getUsername()).size());
        A1.removeGame(G1, market);
        assertEquals(1, market.getGamesOnSale().get(A1.getUsername()).size());
        String result1 = "Seller: A1 added to the market\r\n" +
                "Game: G1 is now being sold by A1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "ERROR: \\< Failed Constraint: G1 cannot be removed as it is on hold in the market.\\>\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
    }

    // remove game for admin - check first mkt, then inv - game can be exchanged
    @Test
    public void testRemoveAdminSaleGood() {
        A1.sell(G1, market);
        assertEquals(1, market.getGamesOnSale().get(A1.getUsername()).size());
        G1.changeHold();
        A1.removeGame(G1, market);
        assertEquals(0, market.getGamesOnSale().get(A1.getUsername()).size());
        String result1 = "Seller: A1 added to the market\r\n" +
                "Game: G1 is now being sold by A1 for $23.5 at a 0.0% discount, will be available for purchase " +
                "tomorrow.\r\n";
        String result2 = "G1 was removed from A1's offering on the market.\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
    }
    
    //remove game from admin inventory - check first mkt, then inv
    @Test
    public void testRemoveAdminInvGood() {
        A1.buy(F1, G2, sale, market);
        assertEquals(1, A1.getInventory().size());
        Finder find = new Finder();
        Game g = find.findGame("G2", A1.getInventory());
        g.changeHold();
        A1.removeGame(G2, market);
        assertEquals(0, A1.getInventory().size());
        String result1 = "A1 has bought G2 from F1 for $20.0.\r\n";
        String result2 = "G2 was removed from A1's inventory.\r\n";
        assertEquals(result + result1 + result2, outContent.toString());
    }

    //remove game for admin for other seller
    @Test
    public void testRemoveAdminOtherSeller() {
        assertEquals(1, market.getGamesOnSale().get(F1.getUsername()).size());
        A1.removeGame(G2, F1, market);
        String result1 = "G2 was removed from F1's offering on the market.\r\n";
        assertEquals(result + result1, outContent.toString());
        assertEquals(0, market.getGamesOnSale().get(F1.getUsername()).size());
    }
    
    //remove game for admin from other buy user's inventory
    @Test
    public void testRemoveAdminOtherBuyer() {
        assertEquals(1, B1.getInventory().size());

        Finder find = new Finder();
        Game g = find.findGame("G2", B1.getInventory());
        g.changeHold();
        
        A1.removeGame(g, B1, market);
        assertEquals(0, B1.getInventory().size());
        String result1 = "G2 was removed from B1's inventory.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
}
