package test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//THIS TEST IS TO CHECK THAT DATABASE READS INFO CORRECTLY
//HAVE DIFF TEST FILES - ONE FOR ALL THE WEIRD AND EDGE CASE
//ONE EMPTY FILE
//ONE NON EXISTENT FILE
//ONE FILE WITH WEIRD STUFF
//FOR THE GAME AND USERS, ONE FILE WITH {}

/** A Database Readtest J unit 5.4 test class that tests that the database reads all files properly and initializes
 * the game, user and market objects appropriately.
 * 
 */
public class DatabaseReadTest {

    File Game, User, Market;
    String filePath = "DatabaseTestFiles/";
    String fileNameGameErrors = filePath + "GameErrors.json";
    String fileNameGameNonExistent = filePath + "GameNonExistent.json";
    String fileNameGameEmpty = filePath + "GameEmpty.json";
    String fileNameGameFormat = filePath + "GameFormat.json";
    String fileNameGameOneGame = filePath + "GameOneGame.json";
    String fileNameGameGood = filePath + "GameGood.json";
    String fileNameUserErrors = filePath + "UserErrors.json";
    String fileNameUserNonExistent = filePath + "UserNonExistent.json";
    String fileNameUserEmpty = filePath + "UserEmpty.json";
    String fileNameUserFormat = filePath + "UserFormat.json";
    String fileNameUserOneUser = filePath + "UserOneUser.json";
    String fileNameUserGood = filePath + "UserGood.json";
    String fileNameMarketErrors = filePath + "MarketErrors.json";
    String fileNameMarketNonExistent = filePath + "MarketNonExistent.json";
    String fileNameMarketEmpty = filePath + "MarketEmpty.json";
    String fileNameMarketFormat = filePath + "MarketFormat.json";
    String fileNameMarketNoAuction = filePath + "MarketNoAuction.json";
    String fileNameMarketAuctionWrong = filePath + "MarketAuctionWrong.json";
    String fileNameMarketNoUID = filePath + "MarketNoUID.json";
    String fileNameMarketGood = filePath + "MarketGood.json";
    String fileNotFoundError = " file not found. An empty ";
    String fileFormatError = " file not in correct format. An empty ";
    String fileErrorEnd = " will be created.\r\n";
    ArrayList<Game> emptyGames;
    ArrayList<AbstractUser> emptyUsers;
    List<Game> games;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        ReadingJSON.setGameFileName(fileNameGameErrors);
        games = ReadingJSON.readGamesFile();
        emptyGames = new ArrayList<Game>();
        emptyUsers = new ArrayList<AbstractUser>();
    }

    @AfterEach
    public void restore() {
        System.setOut(originalOut);
    }

    
    public ArrayList<Integer> unequalValues(ArrayList<Integer> search, ArrayList<Integer> all) {
        
        ArrayList<Integer> diff = new ArrayList<Integer>();
        for (Integer i : search) {
            if (!all.contains(i)) {
                diff.add(i);
            }
        }

        for (Integer j : all) {
            if (!search.contains(j)) {
                diff.add(j);
            }
        }
        
        return diff;
    }
    
    @Test
    public void testGameNonExistent() {
        ReadingJSON.setGameFileName(fileNameGameNonExistent);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileNotFoundError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    @Test
    public void testGameEmpty() {
        ReadingJSON.setGameFileName(fileNameGameEmpty);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    @Test
    public void testGameFormat() {
        ReadingJSON.setGameFileName(fileNameGameFormat);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    @Test
    public void testGameOneGame() {
        ReadingJSON.setGameFileName(fileNameGameOneGame);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    @Test
    public void testGameErrors1() {
        assertEquals( 7, games.size());
    }

    @Test
    public void testGameErrors2() {
        ArrayList<Integer> allIds = new ArrayList<Integer>();
        for (Game g : games) {
            allIds.add(g.getUniqueID());
        }
        ArrayList<Integer> search = new ArrayList<>(Arrays.asList(1, 2, 5, 6, 7, 12, 13));
        assertTrue(allIds.containsAll(search));
        assertEquals(new ArrayList<Integer>(), unequalValues(search, allIds));
    }

    @Test
    public void testGameErrorsCheckItemType() {
        Game game = null;
        for (Game g : games) {
            if (g.getUniqueID() == 1) {
                game = g;
            }
        }
        assertNotNull(game);
        assertEquals("YesCounter-Strike", game.getName());
        assertEquals(20.99, game.getPrice());
        assertEquals("Valve", game.getSupplierID());
        assertEquals(1, game.getUniqueID());
        assertEquals(10.06, game.getDiscount());
        assertFalse(game.getHold());
    }

    @Test
    public void testGameErrorsCheckItemTypeFalseHold() {
        Game game = null;
        for (Game g : games) {
            if (g.getUniqueID() == 5) {
                game = g;
            }
        }
        assertNotNull(game);
        assertEquals("YesHalf-LifeButBobInFront", game.getName());
        assertEquals(23.45, game.getPrice());
        assertEquals("Valve", game.getSupplierID());
        assertEquals(5, game.getUniqueID());
        assertEquals(85, game.getDiscount());
        assertFalse(game.getHold());
    }
    
    @Test
    public void testUserNonExistent() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserNonExistent);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileNotFoundError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    @Test
    public void testUserEmpty() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserEmpty);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    @Test
    public void testUserFormat() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserFormat);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    @Test
    public void testUserOneUser() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserOneUser);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);        
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    @Nested
    class UsersErrorsTest {
        List<AbstractUser> users;
        
        @BeforeEach
        void beforeEach() {
            ReadingJSON.setGameFileName(fileNameGameGood);
            ReadingJSON.setUserFileName(fileNameUserErrors);
            List<Game> games = ReadingJSON.readGamesFile();
            users = ReadingJSON.readUsersFile(games);
        }

        @AfterEach
        void afterEach() {
            
        }

        ArrayList<String> differentStrings(ArrayList<String> search, ArrayList<String> all) {

            ArrayList<String> diff = new ArrayList<String>();
            for (String i : search) {
                if (!all.contains(i)) {
                    diff.add(i);
                }
            }

            for (String j : all) {
                if (!search.contains(j)) {
                    diff.add(j);
                }
            }

            return diff;
        }

        @Test
        void sizeTest() {
            assertEquals(12, users.size());
        }

        @Test
        void equalityTest() {
            ArrayList<String> userNames= new ArrayList<String>();
            for (AbstractUser u : users) {
                userNames.add(u.getUsername());
            }
            
            ArrayList<String> correctUsers = new ArrayList<>(Arrays.asList("YesValve2", "YesValve23", "YesMadeo", 
                    "YesMadeoNoBuy", "YesNoTrans", "YesRegular", "YesGameNoExist", "YesCreditLimit", 
                    "YesCreditInt","YesOneInv", "SameName", "YesInvWrongType"));
            assertEquals(new ArrayList<>(), differentStrings(userNames, correctUsers));
        }
        
        @Test
        void emptyInventory() {
            for (AbstractUser u : users) {
                if (u instanceof SellUser) {
                    assertNull(u.getInventory());
                }
            }
        }
    }
    
    
}
