package test;

import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transactions.Finder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GiftTest {
    AdminUser adminUser1, adminUser2;
    BuyUser BuRecU1, BuSenU1, BuRecFailU1, BuSenFailU1;
    SellUser SeRecRejU1, SeSenU1, SeSenFailU1;
    FullStandardUser FSSenU1, FS3, FS4;
    Game G1, G2, G4, G6;
    Marketplace market;
    Finder finder;


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        adminUser1 = new AdminUser.UserBuilder("ad1").balance(42).build();
        adminUser2 = new AdminUser.UserBuilder("ad2").balance(42).build();
        BuRecU1 = new BuyUser.UserBuilder("b1").balance(34).build();
        BuSenU1 = new BuyUser.UserBuilder("b2").balance(34).build();
        BuRecFailU1 = new BuyUser.UserBuilder("b3").balance(34).build();
        BuSenFailU1 = new BuyUser.UserBuilder("b4").balance(34).build();
        SeRecRejU1 = new SellUser.UserBuilder("s1").balance(34.13).build();
        SeSenU1 = new SellUser.UserBuilder("s2").balance(34.13).build();
        SeSenFailU1 = new SellUser.UserBuilder("s3").balance(34.13).build();
        FSSenU1 = new FullStandardUser.UserBuilder("fs1").balance(20).build();
        FS3 = new FullStandardUser.UserBuilder("fs3").balance(20).build();
        FS4 = new FullStandardUser.UserBuilder("fs4").balance(20).build();
        G1 = new Game("G1", 5, "ad1", 1, 00.0);
        G2 = new Game("G2", 5, "s2", 2, 00.00);
        G4 = new Game("G6", 5, "ad1", 4, 00.0);
        G6 = new Game("G6", 5, "fs3", 6, 00.0);
        market = new Marketplace();
        finder = new Finder();
    }


    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Test for admin gifting from Market to StanBuy user
     *
     */
    @Test
    public void adminGift(){
        adminUser1.sell(G1,market);
        G1.changeHold();

        adminUser1.gift(G1, BuRecU1, market);
        String res1, res2, res3, res4;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "ad1 has gifted: G1 to b1\n";
        res4 = "b1 has received G1 from ad1\n";

        String result1 = res1 + res2 + res3 + res4;

        assertEquals(result1, outContent.toString());

    }


    /**
     * Admin not able to gift because the Game is on Hold
     *
     */
    @Test
    public void adminGameOnHold(){
        // admin started selling the game and then trying to gift
        adminUser1.sell(G1,market);
        adminUser1.gift(G1, BuRecU1, market);
        String res1, res2, res3, res4;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "ERROR: \\< Failed Constraint: G1 cannot be gifted as it is still on hold.\\>\n";
        res4 = "ERROR: \\< Failed Constraint: ad1 is currently not offering G1. Gift transaction failed.\\>\n";

        String res = res1+res2+res3+res4;
        assertEquals(res, outContent.toString());
    }

    /**
     * Admin not able to gift because receiver has a Game in inventory
     *
     */
    @Test
    public void admRecHasGameIN(){
        // setup
        adminUser1.sell(G1,market);
        G1.changeHold();
        BuRecU1.buy(adminUser1, G1, false, market);
        // test
        adminUser1.gift(G1,BuRecU1,market);
        String res1, res2, res3, res4;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "b1 has bought G1 from ad1 for $5.0.\n";
        res4 = "ERROR: \\< Failed Constraint: b1 already has G1. Gift transaction failed.\\>\n";
        String res = res1+res2+res3+res4;
        assertEquals(res, outContent.toString());
    }


    /**
     * Admin not able to gift because the receiver has the Game up for Sale
     *
     */
    @Test
    public void admRecSellGame(){
        // setup
        adminUser1.sell(G1,market);
        G1.changeHold();
        FSSenU1.sell(G1, market);
        // test
        adminUser1.gift(G1, FSSenU1, market);
        String res1, res2, res3, res4, res5;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "Seller: fs1 added to the market\n";
        res4 = "Game: G1 is now being sold by fs1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res5 = "ERROR: \\< Failed Constraint: fs1 already has G1. Gift transaction failed.\\>\n";

        String res = res1+res2+res3+res4+res5;
        assertEquals(res, outContent.toString());
    }


    /**
     * Admin not able to gift because the receiver is a standard Sell user
     *
     */
    @Test
    public void admStanSellGift(){
        // setup
        adminUser1.sell(G1,market);
        G1.changeHold();
        // test
        adminUser1.gift(G1, SeRecRejU1, market );
        String res1, res2, res3;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "ERROR: \\< Failed Constraint:  Sell User can not accept any gifts.\\>\n";
        String result = res1 + res2 + res3;
        assertEquals(result, outContent.toString());
    }


    /**
     * Admin gifting to another Admin
     *
     *
     */
    @Test
    public void adminToadminGift(){
        // setup
        adminUser1.sell(G1,market);
        G1.changeHold();
        // test
        adminUser1.gift(G1, adminUser2, market);
        String res1, res2, res3, res4;
        res1 = "Seller: ad1 added to the market\n";
        res2 = "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n";
        res3 = "ad1 has gifted: G1 to ad2\n";
        res4 = "ad2 has received G1 from ad1\n";

        String result = res1 + res2 + res3 + res4;
        assertEquals(result, outContent.toString());
    }


    /**
     * Stan-sell user gifting but the Game is on hold
     *
     */
    @Test
    public void sSellGameOnHold(){
        SeSenU1.sell(G2, market);
        SeSenU1.gift(G2, adminUser1, market);
        String res3;
        res3 = "Seller: s2 added to the market\n" +
                "Game: G2 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint: G2 is currently on Hold on Market until the next day." +
                " s2 cannot gift the game. Gift transaction failed.\\>\n";

        assertEquals(res3, outContent.toString());
    }


    /**
     * Stan-sell gifting a game they don't own
     *
     */
    @Test
    public void sSellGameNotPresent(){
        SeSenU1.gift(G1, adminUser1, market);
        String res;
        res = "ERROR: \\< Failed Constraint: s2 does not have game: G1 to be gifted. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-sell gifting but receiver has the game in inventory
     *
     */
    @Test
    public void sSellGameInRecInv(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        adminUser1.buy(SeSenU1, G1, false, market);

        SeSenU1.gift(G1, adminUser1, market);

        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ad1 has bought G1 from s2 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: ad1 already has G1. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-sell gifting but the receiver has the game up on the Market
     *
     */
    @Test
    public void sSellGameInRecMarket(){
        SeSenU1.sell(G1, market);
        adminUser1.sell(G1, market);
        G1.changeHold();

        SeSenU1.gift(G1, adminUser1, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint: ad1 already has G1. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-sell gifting to a stan-sell user
     *
     */
    @Test
    public void sSellGiftSS(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, SeRecRejU1, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint: s1is a sell user and cannot accept gifts.\\>\n";
        assertEquals(res, outContent.toString());
    }

    /**
     * Stan-sell gifting to Stan-buy
     *
     */
    @Test
    public void sSellToStanBuy(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, BuSenU1, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "s2 has gifted: G1 to b2.\n" +
                "b2 has received G1 from s2.\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-sell gifting to full-Standard
     *
     */
    @Test
    public void sSellToFullStan(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, FSSenU1, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "s2 has gifted: G1 to fs1.\n" +
                "fs1 has received G1 from s2.\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-sell gifting to admin
     *
     */
    @Test
    public void sSellToAdmin(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, adminUser1, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "s2 has gifted: G1 to ad1.\n" +
                "ad1 has received G1 from s2.\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-buy gifting a game they just bought and have it on hold
     *
     */
    @Test
    public void sBuyGameHold(){
        adminUser1.sell(G1,market);
        G1.changeHold();
        BuSenU1.buy(adminUser1, G1, false, market);

        BuSenU1.gift(G1, adminUser2, market);
        String res;
        res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "G1 cannot be removed as it is on hold.\n" +
                "ERROR: \\< Failed Constraint: G1 is on Hold and will be up for processing the next day. Gift transaction failed.\\>\n";

        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-buy gifting a game they got gifted recently
     *
     */
    @Test
    public void sBuyRecGiftReGiftHold(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, BuSenU1, market);
        BuSenU1.gift(G1, adminUser2, market);
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "s2 has gifted: G1 to b2.\n" +
                "b2 has received G1 from s2.\n" +
                "G1 cannot be removed as it is on hold.\n" +
                "ERROR: \\< Failed Constraint: G1 is on Hold and will be up for processing the next day. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-Buy gifting a game they were gifted and the gift is no longer on hold to a stan-Buy user
     *
     */
    @Test
    public void sBuyRecGiftGift(){
        SeSenU1.sell(G1, market);
        G1.changeHold();
        SeSenU1.gift(G1, BuSenU1, market);
        // find the Game
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();
        BuSenU1.gift(game, adminUser1, market );
        String res;
        res = "Seller: s2 added to the market\n" +
                "Game: G1 is now being sold by s2 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "s2 has gifted: G1 to b2.\n" +
                "b2 has received G1 from s2.\n" +
                "b2 has gifted: G1 to ad1\n" +
                "ad1 has received G1 from b2\n";
        assertEquals(res, outContent.toString());
    }




    /**
     * Stan-Buy gifting a game that the receiver has up on Market
     *
     */
    @Test
    public void sBuyGiftOnRecMark(){
        adminUser1.sell(G1, market);
        G1.changeHold();

        BuSenU1.buy(adminUser1, G1, false, market);
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();

        BuSenU1.gift(G1, adminUser1, market);

        String res;
        res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: ad1 already has G1. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Stan-Buy gifting a game that the receiver has in their inventory
     *
     *
     */
    @Test
    public void sBuyGiftOnRecIn(){
        adminUser1.sell(G1, market);
        G1.changeHold();
        adminUser2.buy(adminUser1, G1, false, market);
        BuSenU1.buy(adminUser1, G1, false, market);
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();
        BuSenU1.gift(G1, adminUser2, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ad2 has bought G1 from ad1 for $5.0.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: ad2 already has G1. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());

    }

    /**
     * Stan-Buy gifting to Stan-Sell
     *
     */
    @Test
    public void sBuyGiftToSsell(){
        adminUser1.sell(G1, market);
        G1.changeHold();
        BuSenU1.buy(adminUser1, G1, false, market);
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();

        BuSenU1.gift(G1, SeSenU1, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: s2is a sell user and cannot accept gifts.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     *
     * Stan-Buy gifting to admin
     *
     */
    @Test
    public void sBuyGiftAdmin(){
        adminUser1.sell(G1, market);
        G1.changeHold();

        BuSenU1.buy(adminUser1, G1, false, market);
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();
        BuSenU1.gift(G1, adminUser2, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "b2 has gifted: G1 to ad2\n" +
                "ad2 has received G1 from b2\n";
        assertEquals(res, outContent.toString());
    }

    /**
     * Stan-Buy gifting to Full-Stand
     *
     */
    @Test
    public void sBuyGiftFullStan(){
        adminUser1.sell(G1, market);
        G1.changeHold();

        BuSenU1.buy(adminUser1, G1, false, market);
        Game game = finder.findGame("G1", BuSenU1.getInventory());
        // change the on-Hold for the game
        game.changeHold();
        BuSenU1.gift(G1, FSSenU1, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "b2 has bought G1 from ad1 for $5.0.\n" +
                "b2 has gifted: G1 to fs1\n" +
                "fs1 has received G1 from b2\n" ;
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game on hold in their Inventory
     *
     */
    @Test
    public void fsGiftHoldIn(){
        adminUser1.sell(G1, market);
        G1.changeHold();
        FS3.buy(adminUser1, G1, false, market);

        FS3.gift(G1, adminUser2, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "fs3 has bought G1 from ad1 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: fs3 is currently not offering G1 as Game is on Hold. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game they were gifted recently
     *
     */
    @Test
    public void fsGiftonHold(){
        adminUser1.sell(G1, market);
        G1.changeHold();
        adminUser1.gift(G1, FS3, market);

        FS3.gift(G1, adminUser2, market);
        String res = "Seller: ad1 added to the market\n" +
                "Game: G1 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ad1 has gifted: G1 to fs3\n" +
                "fs3 has received G1 from ad1\n" +
                "ERROR: \\< Failed Constraint: fs3 is currently not offering G1 as Game is on Hold. Gift transaction failed.\\>\n";

        assertEquals(res, outContent.toString());

    }

    /**
     * Full-Stan gifting a game that is on hold on Market                                                                       -Come back
     *
     */
    @Test
    public void fsGiftHoldOnMark(){
        FS3.sell(G6, market);

        FS3.gift(G6, adminUser2, market);
        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint: G6 cannot be gifted as it is still on hold.\\>\n" +
                "ERROR: \\< Failed Constraint: fs3 is currently not offering G6. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }

    /**
     * Full-Stan gifting a game they don't own
     *
     */
    @Test
    public void fsGiftDNE(){
        FS3.gift(G1,adminUser2, market);
        String res = "ERROR: \\< Failed Constraint: fs3 is currently not offering G1. Gift transaction failed.\\>\n";

        assertEquals(res, outContent.toString());

    }

    /**
     * Full-Stan gifting a game to stan-Sell
     *
     */
    @Test
    public void fsGiftToSS(){
        FS3.sell(G6, market);
        G6.changeHold();

        FS3.gift(G6, SeSenU1,market);
        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint:  Sell User can not accept any gifts.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game the receiver has in their inventory
     *
     */
    @Test
    public void fsGiftInFail(){
        FS3.sell(G6, market);
        G6.changeHold();
        adminUser1.buy(FS3, G6, false, market);

        FS3.gift(G6, adminUser1,market);
        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ad1 has bought G6 from fs3 for $5.0.\n" +
                "ERROR: \\< Failed Constraint: ad1 already has G6. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game the receiver has up for Sale on Market
     *
     *
     */
    @Test
    public void fsGiftMarFail(){
        adminUser1.sell(G4, market);
        FS3.sell(G6, market);
        G6.changeHold();
        FS3.gift(G6, adminUser1,market);

        String res = "Seller: ad1 added to the market\n" +
                "Game: G6 is now being sold by ad1 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "ERROR: \\< Failed Constraint: ad1 already has G6. Gift transaction failed.\\>\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game to admin
     *
     *
     */
    @Test
    public void fsGiftToAdmin(){
        FS3.sell(G6, market);
        G6.changeHold();
        FS3.gift(G6, adminUser1,market);

        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "fs3 has gifted: G6 to ad1\n" +
                "ad1 has received G6 from fs3\n";
        assertEquals(res, outContent.toString());
    }


    /**
     * Full-Stan gifting a game to full-Stan
     *
     */
    @Test
    public void fsGiftToFs(){
        FS3.sell(G6, market);
        G6.changeHold();
        FS3.gift(G6, FSSenU1,market);

        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "fs3 has gifted: G6 to fs1\n" +
                "fs1 has received G6 from fs3\n";
        assertEquals(res, outContent.toString());
    }

    /**
     * Full-Stan gifting to stan-buy
     *
     */
    @Test
    public void fsGiftToSb(){
        FS3.sell(G6, market);
        G6.changeHold();
        FS3.gift(G6, BuSenU1,market);

        String res = "Seller: fs3 added to the market\n" +
                "Game: G6 is now being sold by fs3 for $5.0 at a 0.0% discount, will be available for purchase tomorrow.\n" +
                "fs3 has gifted: G6 to b2\n" +
                "b2 has received G6 from fs3\n";
        assertEquals(res, outContent.toString());
    }
}