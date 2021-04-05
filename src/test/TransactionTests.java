package test;

import main.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import transactions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {

    AdminUser adminUser1;
    BuyUser buyUser1;
    SellUser sellUser1;
    FullStandardUser fullStandardUser1;
    FullStandardUser tempUser;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    ArrayList<AbstractUser> userList;
    ArrayList<Game> gameList;
    Marketplace market;

    Game adminGame;
    Game buyGame;
    Game fullGame;
    Game csgo;
    Game valorant;

    double startBalance = 15.00;

    String adminName = "a1";
    String buyName = "b1";
    String sellName = "s1";
    String fullName = "f1";

    String beforeResult;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));

        adminGame = new Game("AdminGame", 5.00, adminName, 1, 0);
        adminGame.changeHold();
        buyGame = new Game("BuyGame", 5.00, buyName, 1, 0);
        buyGame.changeHold();
        fullGame = new Game("FSGame", 5.00, fullName, 1, 0);
        fullGame.changeHold();

        ArrayList<Game> adminGames = new ArrayList<Game>();
        adminGames.add(adminGame);
        adminUser1 = new AdminUser.UserBuilder(adminName).balance(startBalance).inventoryGames(adminGames).build();
        ArrayList<Game> buyGames = new ArrayList<Game>();
        buyGames.add(buyGame);
        buyUser1 = new BuyUser.UserBuilder(buyName).balance(startBalance).inventoryGames(buyGames).build();
        sellUser1 = new SellUser.UserBuilder(sellName).balance(startBalance).build();
        ArrayList<Game> fullGames = new ArrayList<Game>();
        fullGames.add(fullGame);
        fullStandardUser1 = new FullStandardUser.UserBuilder(fullName).balance(startBalance).inventoryGames(fullGames)
                .build();
        tempUser = new FullStandardUser.UserBuilder("temp").balance(startBalance).build();



        userList = new ArrayList<AbstractUser>();
        userList.add(adminUser1);
        userList.add(buyUser1);
        userList.add(sellUser1);
        userList.add(fullStandardUser1);
        userList.add(tempUser);

        gameList = new ArrayList<Game>();
        csgo = new Game("CSGO", 15.00, sellUser1.getUsername(), 101, 0);
        csgo.changeHold();
        valorant = new Game("Valorant", 5.00, fullStandardUser1.getUsername(), 102, 0);
        valorant.changeHold();

        gameList.add(csgo);
        gameList.add(valorant);
        gameList.add(buyGame);
        gameList.add(adminGame);
        gameList.add(fullGame);

        market = new Marketplace();
        market.addNewSeller(sellName);
        market.getGamesOnSale().get(sellName).add(csgo);
        market.addNewSeller(fullName);
        market.getGamesOnSale().get(fullName).add(valorant);

        beforeResult = outContent.toString();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /*
    AddCredit:

    1) Admin adds to self
    2) Admin adds to user
    3) Buy adds to self
    4) Sell adds to self
    5) FS adds to self
    6) Admin to user who doesnt exist
    7) admin to blank username
     */

    /**
     * Tests that admin can properly add funds to themself.
     */
    @Test
    public void AddCreditTest1() {
        AddCredit transac = new AddCredit(adminUser1.getUsername(), "AA", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(adminUser1.getAccountBalance(), startBalance + 15.00);
    }

    /**
     * tests that admin can properly add funds to another user.
     */
    @Test
    public void AddCreditTest2() {
        AddCredit transac = new AddCredit(buyUser1.getUsername(), "AA", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance + 15.00);
        assertEquals(adminUser1.getAccountBalance(), startBalance);
    }

    /**
     * tests that a BuyUser can add funds to themselves.
     */
    @Test
    public void AddCreditTest3() {
        AddCredit transac = new AddCredit(buyUser1.getUsername(), "BS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance + 15.00);
    }

    /**
     * tests that a SellUser can add funds to themselves.
     */
    @Test
    public void AddCreditTest4() {
        AddCredit transac = new AddCredit(sellUser1.getUsername(), "SS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertEquals(sellUser1.getAccountBalance(), startBalance + 15.00);
    }

    /**
     * tests that a FullStandardUser can add funds to themselves.
     */
    @Test
    public void AddCreditTest5() {
        AddCredit transac = new AddCredit(fullStandardUser1.getUsername(), "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        assertEquals(fullStandardUser1.getAccountBalance(), startBalance + 15.00);
    }

    /**
     * tests that an error is raised when the user being added to doesn't exist.
     */
    @Test
    public void AddCreditTest6() {
        String result = "ERROR: \\<Fatal: User fake Not Found.\\>\r\n";
        AddCredit transac = new AddCredit("fake", "AA", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    /**
     * tests than a warning is raised when the username field is empties and proceeds by adding funds to logged in
     * account
     */
    @Test
    public void AddCreditTest7() {
        String result = "WARNING: \\<Username field left empty, Adding funds to self.\\>\r\n";
        String afterResult = "$15.0 added to a1.\r\n" + "Most updated account balance is $30.0.\r\n";
        AddCredit transac = new AddCredit("", "AA", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result + afterResult, outContent.toString());
        assertEquals(login, adminUser1);
        assertEquals(adminUser1.getAccountBalance(), startBalance + 15.00);
    }

    /*
    AuctionSale:

    1) Admin calls
    2) Buy calls
    3) Sell calls
    4) FS calls
     */

    @Test
    public void AuctionSaleTest1() {
        AuctionSale transac = new AuctionSale(adminUser1.getUsername(), "AA", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertTrue(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest2() {
        String result = "ERROR: \\<Failed Constraint: User b1 does not have the authority to toggle an " +
                "auction sale.\\>\r\n";
        AuctionSale transac = new AuctionSale(buyName, "BS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, buyUser1);
        assertFalse(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest3() {
        String result = "ERROR: \\<Failed Constraint: User s1 does not have the authority to toggle an " +
                "auction sale.\\>\r\n";
        AuctionSale transac = new AuctionSale(sellName, "SS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, sellUser1);
        assertFalse(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest4() {
        String result = "ERROR: \\<Failed Constraint: User f1 does not have the authority to toggle an " +
                "auction sale.\\>\r\n";
        AuctionSale transac = new AuctionSale(fullName, "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, fullStandardUser1);
        assertFalse(market.getAuctionSale());
    }

    /*
    Buy:

    1) Admin buy
    2) Buy buy
    3) Sell buy
    4) FS buy
    5) buyer doesn't exist
    6) seller doesn't exist
    7) game doesn't exist
    8) buyer != logged in user
     */

    @Test
    public void BuyTest1() {
        Buy transac = new Buy(csgo.getName(), sellUser1.getUsername(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(startBalance - csgo.getPrice(), adminUser1.getAccountBalance());
        assertEquals(startBalance + csgo.getPrice(), sellUser1.getAccountBalance());
        Finder finder = new Finder();
        Game g = finder.findGame(csgo.getName(), adminUser1.getInventory());
        assertNotNull(g);
    }

    @Test
    public void BuyTest2() {
        Buy transac = new Buy(csgo.getName(), sellUser1.getUsername(), buyUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertEquals(startBalance - csgo.getPrice(), buyUser1.getAccountBalance());
        assertEquals(startBalance + csgo.getPrice(), sellUser1.getAccountBalance());
        Finder finder = new Finder();
        Game g = finder.findGame(csgo.getName(), buyUser1.getInventory());
        assertNotNull(g);
    }

    @Test
    public void BuyTest3() {
        Buy transac = new Buy(valorant.getName(), fullStandardUser1.getUsername(), sellUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertEquals(startBalance, sellUser1.getAccountBalance());
        assertEquals(startBalance, fullStandardUser1.getAccountBalance());
    }

    @Test
    public void BuyTest4() {
        Buy transac = new Buy(csgo.getName(), sellUser1.getUsername(), fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        assertEquals(startBalance - csgo.getPrice(), fullStandardUser1.getAccountBalance());
        assertEquals(startBalance + csgo.getPrice(), sellUser1.getAccountBalance());
        Finder finder = new Finder();
        Game g = finder.findGame(csgo.getName(), fullStandardUser1.getInventory());
        assertNotNull(g);
    }

    @Test
    public void BuyTest5() {
        String result = "ERROR: \\<Fatal: Buyer fake not found in system.\\>\r\n";
        Buy transac = new Buy(csgo.getName(), sellUser1.getUsername(), "fake");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
        assertEquals(startBalance, adminUser1.getAccountBalance());
        assertEquals(startBalance, sellUser1.getAccountBalance());
        assertFalse(adminUser1.getInventory().contains(csgo));
    }

    @Test
    public void BuyTest6() {
        String result = "ERROR: \\<Fatal: Seller fake not found in database.\\>\r\n";
        Buy transac = new Buy(csgo.getName(), "fake", adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
        assertEquals(startBalance, adminUser1.getAccountBalance());
        assertEquals(startBalance, sellUser1.getAccountBalance());
        assertFalse(adminUser1.getInventory().contains(csgo));
    }

    @Test
    public void BuyTest7() {
        String result = "ERROR: \\<Fatal: Game fake not found in database.\\>\r\n";
        Buy transac = new Buy("fake", sellUser1.getUsername(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
        assertEquals(startBalance, adminUser1.getAccountBalance());
        assertEquals(startBalance, sellUser1.getAccountBalance());
        assertFalse(adminUser1.getInventory().contains(csgo));
    }

    @Test
    public void BuyTest8() {
        String result = "ERROR: \\<Fatal: User b1 making the buy transaction is not the logged in user.\\>\r\n";
        Buy transac = new Buy(csgo.getName(), sellUser1.getUsername(), buyUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
        assertEquals(startBalance, adminUser1.getAccountBalance());
        assertEquals(startBalance, sellUser1.getAccountBalance());
        assertFalse(adminUser1.getInventory().contains(csgo));
    }

    /*
    Create:

    1) Admin create
    2) Buy create
    3) Sell create
    4) FS create
     */

    @Test
    public void CreateTest1() {
        Create transac = new Create("newUser", "FS", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("newUser", userList);
        assertNotNull(newU);
    }

    @Test
    public void CreateTest2() {
        Create transac = new Create("newUser", "FS", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("newUser", userList);
        assertNull(newU);
    }

    @Test
    public void CreateTest3() {
        Create transac = new Create("newUser", "FS", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("newUser", userList);
        assertNull(newU);
    }

    @Test
    public void CreateTest4() {
        Create transac = new Create("newUser", "FS", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("newUser", userList);
        assertNull(newU);
    }

    /*
    Delete:

    1) Admin delete
    2) Buy delete
    3) Sell delete
    4) FS delete
    5) user doesn't exist
    6) funds don't match
    7) type doesn't match
     */

    @Test
    public void DeleteTest1() {
        Delete transac = new Delete("temp", "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNull(newU);
    }

    @Test
    public void DeleteTest2() {
        Delete transac = new Delete("temp", "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNotNull(newU);
    }

    @Test
    public void DeleteTest3() {
        Delete transac = new Delete("temp", "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNotNull(newU);
    }

    @Test
    public void DeleteTest4() {
        Delete transac = new Delete("temp", "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNotNull(newU);
    }

    @Test
    public void DeleteTest5() {
        String result = "ERROR: \\<Failed Constraint: Cannot delete fake as the user does not exist in the " +
                "system.\\>\r\n";
        Delete transac = new Delete("fake", "FS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void DeleteTest6() {
        String result = "WARNING: \\<User temp being deleted does not have matching funds, " +
                "proceeding with deletion.\\>\r\n";
        String afterResult = "User: temp, deleted successfully.\r\n";
        Delete transac = new Delete("temp", "FS", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result + afterResult, outContent.toString());
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNull(newU);
    }

    @Test
    public void DeleteTest7() {
        String result = "WARNING: \\<User temp being deleted is not of correct type, proceeding with deletion.\\>\r\n";
        String afterResult = "User: temp, deleted successfully.\r\n";
        Delete transac = new Delete("temp", "SS", "15.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result + afterResult, outContent.toString());
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        AbstractUser newU = finder.findUser("temp", userList);
        assertNull(newU);
    }

    /*
    Gift:

    1) Buy gift
    2) Sell gift
    3) FS gift
    4) Admin gift to other
    5) Admin causes u1 to gift to u2
    6) game doesn't exist
    7) owner doesn't exist
    8) receiver doesn't exist
     */

    @Test
    public void GiftTest1() {
        Gift transac = new Gift(buyGame.getName(), buyUser1.getUsername(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        Finder finder = new Finder();
        Game g = finder.findGame(buyGame.getName(), adminUser1.getInventory());
        assertNotNull(g);
        assertFalse(buyUser1.getInventory().contains(buyGame));
    }

    @Test
    public void GiftTest2() {
        Gift transac = new Gift(csgo.getName(), sellUser1.getUsername(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        Finder finder = new Finder();
        Game g = finder.findGame(csgo.getName(), adminUser1.getInventory());
        assertNotNull(g);
        assertTrue(market.getGamesOnSale().get(sellName).contains(csgo));
    }

    @Test
    public void GiftTest3() {
        Gift transac = new Gift(fullGame.getName(), fullStandardUser1.getUsername(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        Finder finder = new Finder();
        Game g = finder.findGame(fullGame.getName(), adminUser1.getInventory());
        assertNotNull(g);
        assertFalse(fullStandardUser1.getInventory().contains(fullGame));
    }

    @Test
    public void GiftTest4() {
        Gift transac = new Gift(adminGame.getName(), adminUser1.getUsername(), fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        Game g = finder.findGame(adminGame.getName(), fullStandardUser1.getInventory());
        assertNotNull(g);
        assertFalse(adminUser1.getInventory().contains(adminGame));
    }

    @Test
    public void GiftTest5() {
        Gift transac = new Gift(buyGame.getName(), buyUser1.getUsername(), fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        Game g = finder.findGame(buyGame.getName(), fullStandardUser1.getInventory());
        assertNotNull(g);
        assertFalse(buyUser1.getInventory().contains(buyGame));
    }

    @Test
    public void GiftTest6() {
        String result = "ERROR: \\<Fatal: Cannot find fakeGame in the system.\\>\r\n";
        Gift transac = new Gift("fakeGame", adminUser1.getUsername(), fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void GiftTest7() {
        String result = "ERROR: \\<Fatal: Cannot find fakeOwn in the system.\\>\r\n";
        Gift transac = new Gift(adminGame.getName(), "fakeOwn", fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void GiftTest8() {
        String result = "ERROR: \\<Fatal: Cannot find fakeReceive in the system.\\>\r\n";
        Gift transac = new Gift(adminGame.getName(), adminUser1.getUsername(), "fakeReceive");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    /*
    Login:

    1) Admin login
    2) Buy login
    3) Sell login
    4) FS login
    5) Someone is already logged in
    6) funds don't match
    7) type doesn't match
    8) User doesn't exist
     */
    @Test
    public void LoginTest1() {
        Login transac = new Login(adminUser1.getUsername(), adminUser1.getType(),
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(login, adminUser1);
    }

    @Test
    public void LoginTest2() {
        Login transac = new Login(buyUser1.getUsername(), buyUser1.getType(),
                Double.toString(buyUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(login, buyUser1);
    }

    @Test
    public void LoginTest3() {
        Login transac = new Login(sellUser1.getUsername(), sellUser1.getType(),
                Double.toString(sellUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(login, sellUser1);
    }

    @Test
    public void LoginTest4() {
        Login transac = new Login(fullStandardUser1.getUsername(), fullStandardUser1.getType(),
                Double.toString(fullStandardUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(login, fullStandardUser1);
    }

    @Test
    public void LoginTest5() {
        String result = "ERROR: \\<Fatal: There is already a User who is logged in.\\>\r\n";
        Login transac = new Login(adminUser1.getUsername(), adminUser1.getType(),
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void LoginTest6() {
        String result = "WARNING: \\<User logging in a1 does not have matching funds, proceeding with login.\\>\r\n";
        Login transac = new Login(adminUser1.getUsername(), adminUser1.getType(), "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void LoginTest7() {
        String result = "WARNING: \\<User logging in a1 is not of correct type, proceeding with login.\\>\r\n";
        Login transac = new Login(adminUser1.getUsername(), "BS",
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void LoginTest8() {
        String result = "ERROR: \\<Fatal: User fake not found in database.\\>\r\n";
        Login transac = new Login("fake", adminUser1.getType(),
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, null);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, null);
    }

    /*
    Logout:

    1) username != login.username
    2) funds don't match
    3) type doesn't match
    4) Admin logout
    5) Buy logout
    6) Sell logout
    7) FS logout
     */

    @Test
    public void LogoutTest1() {
        String result = "WARNING: \\<User logging out fake does not match username of user currently logged in, " +
                "proceeding by logging out user currently logged in.\\>\r\n";
        Logout transac = new Logout("fake", adminUser1.getType(),
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertNull(login);
    }

    @Test
    public void LogoutTest2() {
        String result = "WARNING: \\<User logging out a1 does not have matching funds, proceeding with logout.\\>\r\n";
        Logout transac = new Logout(adminUser1.getUsername(), adminUser1.getType(), "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertNull(login);
    }

    @Test
    public void LogoutTest3() {
        String result = "WARNING: \\<User logging out a1 is not of correct type, proceeding with loggout.\\>\r\n";
        Logout transac = new Logout(adminUser1.getUsername(), "BS",
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertNull(login);
    }

    @Test
    public void LogoutTest4() {
        Logout transac = new Logout(adminUser1.getUsername(), adminUser1.getType(),
                Double.toString(adminUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertNull(login);
    }

    @Test
    public void LogoutTest5() {
        Logout transac = new Logout(buyUser1.getUsername(), buyUser1.getType(),
                Double.toString(buyUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertNull(login);
    }

    @Test
    public void LogoutTest6() {
        Logout transac = new Logout(sellUser1.getUsername(), sellUser1.getType(),
                Double.toString(sellUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertNull(login);
    }

    @Test
    public void LogoutTest7() {
        Logout transac = new Logout(fullStandardUser1.getUsername(), fullStandardUser1.getType(),
                Double.toString(fullStandardUser1.getAccountBalance()));
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertNull(login);
    }

    /*
    Refund:

    1) Admin refund
    2) Buy refund
    3) Sell refund
    4) FS refund
    5) buyer doesn't exist
    6) seller doesn't exist
     */
    @Test
    public void RefundTest1() {
        Refund transac = new Refund(buyUser1.getUsername(), sellUser1.getUsername(), "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance + 5.00);
        assertEquals(sellUser1.getAccountBalance(), startBalance - 5.00);
    }

    @Test
    public void RefundTest2() {
        Refund transac = new Refund(buyUser1.getUsername(), sellUser1.getUsername(), "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance);
        assertEquals(sellUser1.getAccountBalance(), startBalance);
    }

    @Test
    public void RefundTest3() {
        Refund transac = new Refund(buyUser1.getUsername(), sellUser1.getUsername(), "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance);
        assertEquals(sellUser1.getAccountBalance(), startBalance);
    }

    @Test
    public void RefundTest4() {
        Refund transac = new Refund(buyUser1.getUsername(), sellUser1.getUsername(), "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        assertEquals(buyUser1.getAccountBalance(), startBalance);
        assertEquals(sellUser1.getAccountBalance(), startBalance);
    }

    @Test
    public void RefundTest5() {
        String result = "ERROR: \\<Fatal: User fake cannot be found in system.\\>\r\n";
        Refund transac = new Refund("fake", sellUser1.getUsername(), "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void RefundTest6() {
        String result = "ERROR: \\<Fatal: User fake cannot be found in system.\\>\r\n";
        Refund transac = new Refund(buyUser1.getUsername(), "fake", "5.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    /*
    Remove game:

    1) Admin remove game from self
    2) Admin remove game from another
    3) Buy remove
    4) Sell remove
    5) FS remove
    6) owner doesn't exist
    7) game doesn't exist
     */

    @Test
    public void RemoveTest1() {
        RemoveGame transac = new RemoveGame(adminGame.getName(), adminUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertFalse(adminUser1.getInventory().contains(adminGame));
    }

    @Test
    public void RemoveTest2() {
        RemoveGame transac = new RemoveGame(buyGame.getName(), buyUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertFalse(buyUser1.getInventory().contains(buyGame));
    }

    @Test
    public void RemoveTest3() {
        RemoveGame transac = new RemoveGame(buyGame.getName(), buyUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertFalse(buyUser1.getInventory().contains(buyGame));
    }

    @Test
    public void RemoveTest4() {
        RemoveGame transac = new RemoveGame(csgo.getName(), sellUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertFalse(market.getGamesOnSale().get(sellUser1.getUsername()).contains(csgo));
    }

    @Test
    public void RemoveTest5() {
        RemoveGame transac = new RemoveGame(fullGame.getName(), fullStandardUser1.getUsername());
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        assertFalse(fullStandardUser1.getInventory().contains(fullGame));
    }

    @Test
    public void RemoveTest6() {
        String result = "ERROR: \\<Fatal: User: fake does not exist in database.\\>\r\n";
        RemoveGame transac = new RemoveGame(buyGame.getName(), "fake");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    @Test
    public void RemoveTest7() {
        String result = "ERROR: \\<Fatal: Game: fake does not exist in database.\\>\r\n";
        RemoveGame transac = new RemoveGame("fake", buyName);
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, adminUser1);
    }

    /*
    Sell:

    1) Admin sell
    2) Buy sell
    3) Sell sell
    4) FS sell
    5) seller != login.username
    6) seller has game on market
     */
    @Test
    public void SellTest1() {
        Sell transac = new Sell("newGame", adminUser1.getUsername(), "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        Game game = finder.findGame("newGame", gameList);
        assertNotNull(game);
        assertEquals(1, market.getGamesOnSale().get(adminName).size());
        assertTrue(market.getGamesOnSale().get(adminUser1.getUsername()).contains(game));
    }

    @Test
    public void SellTest2() {
        Sell transac = new Sell("newGame", buyUser1.getUsername(), "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        Finder finder = new Finder();
        Game game = finder.findGame("newGame", gameList);
        assertNull(game);
    }

    @Test
    public void SellTest3() {
        Sell transac = new Sell("newGame", sellUser1.getUsername(), "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        Finder finder = new Finder();
        Game game = finder.findGame("newGame", gameList);
        assertNotNull(game);
        assertTrue(market.getGamesOnSale().get(sellUser1.getUsername()).contains(game));
    }

    @Test
    public void SellTest4() {
        Sell transac = new Sell("newGame", fullStandardUser1.getUsername(), "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        Finder finder = new Finder();
        Game game = finder.findGame("newGame", gameList);
        assertNotNull(game);
        assertTrue(market.getGamesOnSale().get(fullStandardUser1.getUsername()).contains(game));
    }

    @Test
    public void SellTest5() {
        String result = "WARNING: \\<Logged in user does not match username: wrong, " +
                "proceeding using logged in user as the seller.\\>\r\n";
        String afterResult = "Seller: a1 added to the market\r\n" +
                "Game: newGame is now being sold by a1 for $10.0 at a 20.0% discount, " +
                "will be available for purchase tomorrow.\r\n";
        Sell transac = new Sell("newGame", "wrong", "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(beforeResult + result + afterResult, outContent.toString());
        assertEquals(login, adminUser1);
        Finder finder = new Finder();
        Game game = finder.findGame("newGame", gameList);
        assertNotNull(game);
        assertTrue(market.getGamesOnSale().get(adminUser1.getUsername()).contains(game));
    }

    @Test
    public void SellTest6() {
        String result = "ERROR: \\<Failed Constraint: s1 could not sell CSGO as User is already selling this " +
                "exact game.\\>\r\n";
        Sell transac = new Sell(csgo.getName(), sellUser1.getUsername(), "20.00", "10.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(beforeResult + result, outContent.toString());
        assertEquals(login, sellUser1);
        assertEquals(market.getGamesOnSale().get(sellUser1.getUsername()).size(), 1);
    }
}
