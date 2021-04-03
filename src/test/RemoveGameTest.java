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
        adminUser1 = new AdminUser.UserBuilder("diego").balance(42).build();
        buyUser1 = new BuyUser.UserBuilder("dora").balance(41.58).build();
        sellUser1 = new SellUser.UserBuilder("boots").balance(34.13).build();
        fullStandardUser1 = new FullStandardUser.UserBuilder("swiper").balance(32.40).build();
        monopoly = new Game("Monopoly", 23.5, "boots", 1, 00.0);
        pacman = new Game("Pacman", 20.0, "swiper", 2, 10.0);
        pacman1 = new Game("Pacman", 22.0, "swiper", 2, 0);
        sonic = new Game("Sonic", 22.0, "swiper", 2, 0);
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
                "23.5" + " at a " + "0" + "% discount, will be available for purchase tomorrow.\r\n";
        assertEquals(result1 + result, outContent.toString());
        assertEquals(monopoly, market.getGamesOnSale().get(sellUser1.getUsername()).get(0));
    }

//    removegame - remove a game from a user's inventory or from being sold
//    The front end will prompt for the game name and if necessary, the game's owner.
//    This information is saved to the daily transaction file
//    Constraints:
//    semi-privileged transaction - Non-admins can only remove their own games. In admin mode, this allows any game to be removed from any user.
//    game can be in the owner's inventory OR one of those the user has put up for sale
//    cannot remove a game that was purchased or put up to sale on the same day

    
 
    //remove from inventory
    
    //remove from game sold
    
    //remove game on hold should not go thru
    
    //HOW TO CHECK IF I BOUGHT THIS GAME TODAY??? - NEW GAME ATTRIBUTE
    
    
}
