package test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    
    public boolean equalArraylist(ArrayList<Integer> search, ArrayList<Integer> all) {
        
        for (Integer i : search) {
            if (!all.contains(i)) {
                System.out.println(i);
                return false;
            }
        }

        for (Integer j : all) {
            if (!search.contains(j)) {
                System.out.println(j);
                return false;
            }
        }
        
        return true;
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
        for (Game g : games) {
            System.out.println(g.getUniqueID());
        }
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
        System.out.println(equalArraylist(search, allIds));
//        assertTrue(equalArraylist(search, allIds));
        System.out.println(outContent.toString());
        assertTrue(equalArraylist(search, allIds));
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
    
}
