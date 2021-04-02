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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    String filePath;
    String fileNameGameErrors, fileNameGameNonExistent, fileNameGameEmpty, fileNameGameFormat, fileNameGameOneGame,
    fileNameGameGood;//games file
    String fileNameUserErrors, fileNameUserNonExistent, fileNameUserEmpty, fileNameUserFormat, fileNameUserOneUser;
    String fileNameMarketErrors, fileNameMarketNonExistent, fileNameMarketEmpty, fileNameMarketFormat,
            fileNameMarketNoAuction, fileNameMarketAuctionWrong, fileNameMarketNoUID;

    String fileNotFoundError = " file not found. An empty ";
    String fileFormatError = " file not in correct format. An empty ";
    String fileErrorEnd = " will be created.\r\n";

    ArrayList<Game> emptyGames;
    ArrayList<AbstractUser> emptyUsers;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        filePath = "DatabaseTestFiles/";
        fileNameGameErrors = filePath + "GameErrors.json";
        fileNameGameNonExistent = filePath + "GameNonExistent.json";
        fileNameGameEmpty = filePath + "GameEmpty.json";
        fileNameGameFormat = filePath + "GameFormat.json";
        fileNameGameOneGame = filePath + "GameOneGame.json";
        fileNameGameGood = filePath + "GameGood.json";
        fileNameUserErrors = filePath + "UserErrors.json";
        fileNameUserNonExistent = filePath + "UserNonExistent.json";
        fileNameUserEmpty = filePath + "UserEmpty.json";
        fileNameUserFormat = filePath + "UserFormat.json";
        fileNameUserOneUser = filePath + "UserOneUser.json";
        fileNameMarketErrors = filePath + "MarketErrors.json";
        fileNameMarketNonExistent = filePath + "MarketNonExistent.json";
        fileNameMarketEmpty = filePath + "MarketEmpty.json";
        fileNameMarketFormat = filePath + "MarketFormat.json";
        fileNameMarketNoAuction = filePath + "MarketNoAuction.json";
        fileNameMarketAuctionWrong = filePath + "MarketAuctionWrong.json";
        fileNameMarketNoUID = filePath + "MarketNoUID.json";
        emptyGames = new ArrayList<Game>();
        emptyUsers = new ArrayList<AbstractUser>();
    }

    @AfterEach
    public void restore() {
        System.setOut(originalOut);
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
