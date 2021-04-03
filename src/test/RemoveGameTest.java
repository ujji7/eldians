package test;

import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RemoveGameTest {
    
    AdminUser A1;
    BuyUser B1;
    SellUser S1;
    FullStandardUser F1;
    Game G1, G2, G3, G4;
    Marketplace market;
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
//        G2.changeHold(); //G2 is allowed to be bought
        G3 = new Game("G3", 22.0, "F1", 3, 0);
        G3.changeHold(); //
        market = new Marketplace();
        boolean sale = market.getAuctionSale();
        F1.sell(G2, market);
        G2.changeHold();
        B1.buy(F1, G2, sale, market); //now cannot remove it from inv
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    
    
//    removegame - remove a game from a user's inventory or from being sold
//    The front end will prompt for the game name and if necessary, the game's owner.
//    This information is saved to the daily transaction file
//    Constraints:
//    semi-privileged transaction - Non-admins can only remove their own games. In admin mode, this allows any game to be removed from any user.
//    game can be in the owner's inventory OR one of those the user has put up for sale
//    cannot remove a game that was purchased or put up to sale on the same day
    
    
 
    //remove from inventory exists and hold is good
//    @Test
//    public void testRemoveInv() {
//        B1.getInventory();
//        
//        B1.removeGame(G2, market);
//        String result1 = "B1 cannot remove the game";
//        assertEquals(result1, outContent.toString());
//    }
    
    //remove from game sold exists
    @Test
    public void testRemoveSaleToday() {
        S1.removeGame(G2, market);
        String result1 = "G2 was removed from the User's offering on the Market.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    //remove game from inventory on hold should not go thru
    @Test
    public void testRemoveInvNotToday() {
        B1.removeGame(G2, market);
        String result1 = "G2 cannot be removed as it is on hold.\r\n\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    //remove game from on sale on hold should not go through
    @Test
    public void testRemoveSaleNotToday() {
        S1.sell(G1, market);
        S1.removeGame(G1, market);
        String result1 = "G1 cannot be removed as it is on hold.\r\n\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    
    // remove game doesn't exist anywhere
    
    // remove game for admin - check first mkt, then inv
    
    //remove game for admin other - check first mkt, then inv
    
    //remove game for buy type - only checks their inventory
    
    // remove game for sell - only checks on market

    @Test
    public void testRemoveSaleTodayFull() {
        F1.removeGame(G2, market);
        String result1 = "G2 was removed from the User's offering on the Market.\r\n";
        assertEquals(result + result1, outContent.toString());
    }
    
    
    //remove game for full - checks first market, then inv
    
    
}
