package test;
//NEVER CHECKS NEGATIVE PRICES
import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String result = "$100.0 added to dora.\r\nMost updated account balance is $141.58.\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    // admin add credit to self
    public void adminAddCreditToSelf(){
        adminUser1.addCreditTo(100.00, adminUser1);
        String result = "$100.0 added to diego.\r\nMost updated account balance is $142.0.\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    // admin add credit
    public void adminAddCredit(){
        adminUser1.addCredit(100.00);
        String result = "$100.0 added to diego.\r\nMost updated account balance is $142.0.\r\n";
        assertEquals(result, outContent.toString());    }

    @Test
    //Regular add credit
    public void standardAddCredit(){
        sellUser1.addCredit(100.00);
        String result = "$100.0 added to boots.\r\nMost updated account balance is $134.13.\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    //add credit passes the max for the day
    public void passDailyMax(){
        fullStandardUser1.addCredit(1001.99);
        String result = "ERROR: \\< Failed Constraint: swiper's daily limit would be reached upon addition of " +
                "funds!\n" + "You can only add $1000.0 to the account for the rest of today.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    // passes the user max
    public void passUserMax(){
        buyUser2.addCredit(2.00);
        String result = "ERROR: \\< Failed Constraint: benny's balance was Maxed out! $1.99 was added to " +
                "their account.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    //negative credit standard
    public void negativeCreditStandard(){
        buyUser2.addCredit(-5.00);
        String result = "ERROR: \\< Failed Constraint: Amount should be greater than $0.\\>\r\n";
        assertEquals(result, outContent.toString());
    }

    @Test
    //negative credit add credit to
    public void negativeCreditAddCreditTo(){
        adminUser1.addCreditTo(-5.00, buyUser1);
        String result = "ERROR: \\< Failed Constraint: Amount should be greater than $0.\\>\r\n";
        assertEquals(result, outContent.toString());
    }


    // add credit to user doesn't exist

    //add credit to empty username field

    //standard user add credit to

}

