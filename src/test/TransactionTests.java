package test;

import main.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import transactions.AddCredit;
import transactions.AuctionSale;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransactionTests {

    AdminUser adminUser1;
    BuyUser buyUser1;
    SellUser sellUser1;
    FullStandardUser fullStandardUser1;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    ArrayList<AbstractUser> userList;
    ArrayList<Game> gameList;
    Marketplace market;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        adminUser1 = new AdminUser.UserBuilder("a1").build();
        buyUser1 = new BuyUser.UserBuilder("b1").build();
        sellUser1 = new SellUser.UserBuilder("s1").build();
        fullStandardUser1 = new FullStandardUser.UserBuilder("f1").build();

        userList = new ArrayList<AbstractUser>();
        userList.add(adminUser1);
        userList.add(buyUser1);
        userList.add(sellUser1);
        userList.add(fullStandardUser1);

        gameList = new ArrayList<Game>();

        market = new Marketplace();

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

    @Test
    public void AddCreditTest1() {
        AddCredit transac = new AddCredit(adminUser1.getUsername(), "AA", "12.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(adminUser1.getAccountBalance(), 12.00);
    }

    @Test
    public void AddCreditTest2() {
        AddCredit transac = new AddCredit(buyUser1.getUsername(), "AA", "12.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertEquals(buyUser1.getAccountBalance(), 12.00);
        assertEquals(adminUser1.getAccountBalance(), 0.00);
    }

    @Test
    public void AddCreditTest3() {
        AddCredit transac = new AddCredit(buyUser1.getUsername(), "BS", "12.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertEquals(buyUser1.getAccountBalance(), 12.00);
    }

    @Test
    public void AddCreditTest4() {
        AddCredit transac = new AddCredit(sellUser1.getUsername(), "SS", "12.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertEquals(sellUser1.getAccountBalance(), 12.00);
    }

    @Test
    public void AddCreditTest5() {
        AddCredit transac = new AddCredit(fullStandardUser1.getUsername(), "FS", "12.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
        assertEquals(login, fullStandardUser1);
        assertEquals(fullStandardUser1.getAccountBalance(), 12.00);
    }

    @Test
    public void AddCreditTest6() { }

    @Test
    public void AddCreditTest7() { }

    /*
    AuctionSale:

    1) Admin calls
    2) Buy calls
    3) Sell calls
    4) FS calls
     */
    @Test
    public void AuctionSaleTest1() {
        AuctionSale transac = new AuctionSale(adminUser1.getUsername(), "AA", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, adminUser1);
        assertEquals(login, adminUser1);
        assertTrue(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest2() {
        AuctionSale transac = new AuctionSale(buyUser1.getUsername(), "AA", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, buyUser1);
        assertEquals(login, buyUser1);
        assertFalse(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest3() {
        AuctionSale transac = new AuctionSale(sellUser1.getUsername(), "AA", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, sellUser1);
        assertEquals(login, sellUser1);
        assertFalse(market.getAuctionSale());
    }

    @Test
    public void AuctionSaleTest4() {
        AuctionSale transac = new AuctionSale(fullStandardUser1.getUsername(), "AA", "0.00");
        AbstractUser login = transac.execute(userList, gameList, market, fullStandardUser1);
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
    public void BuyTest1() {}

    @Test
    public void BuyTest2() {}

    @Test
    public void BuyTest3() {}

    @Test
    public void BuyTest4() {}

    @Test
    public void BuyTest5() {}

    @Test
    public void BuyTest6() {}

    @Test
    public void BuyTest7() {}

    @Test
    public void BuyTest8() {}

    /*
    Create:

    1) Admin create
    2) Buy create
    3) Sell create
    4) FS create
     */

    @Test
    public void CreateTest1() {}

    @Test
    public void CreateTest2() {}

    @Test
    public void CreateTest3() {}

    @Test
    public void CreateTest4() {}

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
    public void DeleteTest1() {}

    @Test
    public void DeleteTest2() {}

    @Test
    public void DeleteTest3() {}

    @Test
    public void DeleteTest4() {}

    @Test
    public void DeleteTest5() {}

    @Test
    public void DeleteTest6() {}

    @Test
    public void DeleteTest7() {}

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
    public void GiftTest1() {}

    @Test
    public void GiftTest2() {}

    @Test
    public void GiftTest3() {}

    @Test
    public void GiftTest4() {}

    @Test
    public void GiftTest5() {}

    @Test
    public void GiftTest6() {}

    @Test
    public void GiftTest7() {}

    @Test
    public void GiftTest8() {}

    /*
    Login:

    1) Admin login
    2) Buy login
    3) Sell login
    4) FS login
    5) Someone is already logged in
    6) funds don't match
    7) type doesn't match
     */
    @Test
    public void LoginTest1() {}

    @Test
    public void LoginTest2() {}

    @Test
    public void LoginTest3() {}

    @Test
    public void LoginTest4() {}

    @Test
    public void LoginTest5() {}

    @Test
    public void LoginTest6() {}

    @Test
    public void LoginTest7() {}

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
    public void LogoutTest1() {}

    @Test
    public void LogoutTest2() {}

    @Test
    public void LogoutTest3() {}

    @Test
    public void LogoutTest4() {}

    @Test
    public void LogoutTest5() {}

    @Test
    public void LogoutTest6() {}

    @Test
    public void LogoutTest7() {}

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
    public void RefundTest1() {}

    @Test
    public void RefundTest2() {}

    @Test
    public void RefundTest3() {}

    @Test
    public void RefundTest4() {}

    @Test
    public void RefundTest5() {}

    @Test
    public void RefundTest6() {}

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
    public void RemoveTest1() {}

    @Test
    public void RemoveTest2() {}

    @Test
    public void RemoveTest3() {}

    @Test
    public void RemoveTest4() {}

    @Test
    public void RemoveTest5() {}

    @Test
    public void RemoveTest6() {}

    @Test
    public void RemoveTest7() {}

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
    public void SellTest1() {}

    @Test
    public void SellTest2() {}

    @Test
    public void SellTest3() {}

    @Test
    public void SellTest4() {}

    @Test
    public void SellTest5() {}

    @Test
    public void SellTest6() {}


}
