package test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** A Database Readtest J unit 5.4 test class that tests that the database reads all files properly and initializes
 * the game, user and market objects appropriately.
 * 
 */
public class DatabaseReadTest {

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

    /** Returns a list of integers that are different between both lists.
     * 
     * @param search first list of integers 
     * @param all second list of integers
     * @return list of difference integers
     */
    public ArrayList<Integer> unequalIntegers(ArrayList<Integer> search, ArrayList<Integer> all) {
        
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
    
    // Game file Non Existent
    @Test
    public void testGameNonExistent() {
        ReadingJSON.setGameFileName(fileNameGameNonExistent);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileNotFoundError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    //game file empty
    @Test
    public void testGameEmpty() {
        ReadingJSON.setGameFileName(fileNameGameEmpty);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    //game file has like .
    @Test
    public void testGameFormat() {
        ReadingJSON.setGameFileName(fileNameGameFormat);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    //game file is not an array of games, but only one
    @Test
    public void testGameOneGame() {
        ReadingJSON.setGameFileName(fileNameGameOneGame);
        List<Game> games = ReadingJSON.readGamesFile();
        assertEquals("Games" + fileFormatError + "list of games" + fileErrorEnd, outContent.toString());
        assertEquals(emptyGames, games);
    }

    // test size of game errors game
    @Test
    public void testGameErrors1() {
        assertEquals( 7, games.size());
    }

    //test that none of the games with errors got created
    @Test
    public void testGameErrors2() {
        ArrayList<Integer> allIds = new ArrayList<Integer>();
        for (Game g : games) {
            allIds.add(g.getUniqueID());
        }
        ArrayList<Integer> search = new ArrayList<>(Arrays.asList(1, 2, 5, 6, 7, 12, 13));
        assertTrue(allIds.containsAll(search));
        assertEquals(new ArrayList<Integer>(), unequalIntegers(search, allIds));
    }

    // test that game object gets created properly
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

    // check that onhold changes
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
    
    //user file does not exist
    @Test
    public void testUserNonExistent() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserNonExistent);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileNotFoundError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    //user file is empty
    @Test
    public void testUserEmpty() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserEmpty);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    //user file has a ,
    @Test
    public void testUserFormat() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserFormat);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }

    //user file doesnt have a list of users, only has one
    @Test
    public void testUserOneUser() {
        ReadingJSON.setGameFileName(fileNameGameGood);
        ReadingJSON.setUserFileName(fileNameUserOneUser);
        List<Game> games = ReadingJSON.readGamesFile();
        List<AbstractUser> users = ReadingJSON.readUsersFile(games);        
        assertEquals("Users" + fileFormatError + "list of users" + fileErrorEnd, outContent.toString());
        assertEquals(emptyUsers, users);
    }


    /** Class that checks for each user error possible
     * 
     */
    @Nested
    class UsersErrorsTest {
        List<AbstractUser> users;
        List<Game> games;
        
        @BeforeEach
        void beforeEach() {
            ReadingJSON.setGameFileName(fileNameGameGood);
            ReadingJSON.setUserFileName(fileNameUserErrors);
            games = ReadingJSON.readGamesFile();
            users = ReadingJSON.readUsersFile(games);
        }

        @AfterEach
        void afterEach() {
            games = new ArrayList<>();
            users = new ArrayList<>();
        }

        /** Return a list of any strings that are different in both files.
         * 
         * @param search first list of strings
         * @param all second list of strings
         * @return list of any different strings
         */
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

        //basic test that games and users initialized properly
        @Test
        void sizeTest() {
            assertEquals(4, games.size());
            assertEquals(15, users.size());
        }

        /** test to confirm that any user with invalid types: name too long, seller with inventory, etc are not added
         * to the system.
         * 
         */
        @Test
        void equalityTest() {
            ArrayList<String> userNames= new ArrayList<String>();
            for (AbstractUser u : users) {
                userNames.add(u.getUsername());
            }
            
            ArrayList<String> correctUsers = new ArrayList<>(Arrays.asList("YesValve2", "YesValve23", "YesMadeo", 
                    "YesMadeoNoBuy", "YesNoTrans", "YesRegular", "YesGameNoExist", "YesCreditLimit", 
                    "YesCreditInt","YesOneInv", "SameName", "YesInvWrongType", "S1", "A1", "YesInvWrong"));
            assertEquals(new ArrayList<>(), differentStrings(userNames, correctUsers));
        }
        
        // test that sellers have no inventory
        @Test
        void sellerInventory() {
            for (AbstractUser u : users) {
                if (u instanceof SellUser) {
                    assertNull(u.getInventory());
                }
            }
        }

        // test that with an inventory of wrong type, empty inventory created
        @Test
        void inventoryWrongType() {
            AbstractUser user = null;
            for (AbstractUser u : users) {
                if (u.getUsername().equals("YesInvWrongType")) {
                    user = u;
                }
            }
            assertNotNull(user);
            assertEquals(new ArrayList<>(), user.getInventory());
        }

        // check that games added to inventory accurately
        @Test
        void checkInventory() {
            AbstractUser user = null;
            for (AbstractUser u : users) {
                if (u.getUsername().equals("YesCreditInt")) {
                    user = u;
                }
            }
            assertNotNull(user);
            assertEquals(999.0, user.getAccountBalance());
            assertEquals(1, user.getInventory().size());
            assertEquals("G2", user.getInventory().get(0).getName());
        }

        //check that for wrong inventory items in inventory, it sets inventory to empty
        @Test
        void inventoryZero() {
            AbstractUser user = null;
            for (AbstractUser u : users) {
                if (u.getUsername().equals("YesInvWrong")) {
                    user = u;
                }
            }
            assertNotNull(user);
            assertEquals(999.9, user.getAccountBalance());
            assertEquals(0, user.getInventory().size());
        }

        //test that if one of inventory object is incorrect, others are unaffected
        @Test
        void inventoryOneWrong() {
            AbstractUser user = null;
            for (AbstractUser u : users) {
                if (u.getUsername().equals("YesOneInv")) {
                    user = u;
                }
            }
            assertNotNull(user);
            assertEquals(2, user.getInventory().size());
        }        
    }


    /** test for marketplace reading
     * 
     */
    @Nested
    class MarketBasicTest {
        List<AbstractUser> users;
        List<Game> games;

        @BeforeEach
        void beforeEach() {
            System.setOut(new PrintStream(outContent));
            ReadingJSON.setGameFileName(fileNameGameGood);
            ReadingJSON.setUserFileName(fileNameUserGood);
            games = ReadingJSON.readGamesFile();
            users = ReadingJSON.readUsersFile(games);
        }

        @AfterEach
        void afterEach() {
            games = new ArrayList<>();
            users = new ArrayList<>();
            System.setOut(originalOut);
        }

        //basic size test
        @Test
        void sizeTest() {
            assertEquals(4, games.size());
            assertEquals(3, users.size());
        }

        //no market file
        @Test
        public void testMarketNonExistent() {
            ReadingJSON.setMarketFileName(fileNameMarketNonExistent);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertEquals("Market" + fileNotFoundError + "market" + fileErrorEnd, outContent.toString());
            assertFalse(market.getAuctionSale());
            assertEquals(new HashMap<>(), market.getGamesOnSale());
        }

        //market file empty
        @Test
        public void testMarketEmpty() {
            ReadingJSON.setMarketFileName(fileNameMarketEmpty);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);
            assertFalse(market.getAuctionSale());
            assertEquals(new HashMap<>(), market.getGamesOnSale());
            assertEquals("Market" + fileFormatError + "market" + fileErrorEnd, outContent.toString());
        }
        

        //market file is ,
        @Test
        public void testMarketFormat() {
            ReadingJSON.setMarketFileName(fileNameMarketFormat);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertEquals("Market" + fileFormatError + "market" + fileErrorEnd, outContent.toString());
            assertFalse(market.getAuctionSale());
            assertEquals(new HashMap<>(), market.getGamesOnSale());
        }

        //market has no auction - gets set with false
        @Test
        public void testMarketNoAuction() {
            ReadingJSON.setMarketFileName(fileNameMarketNoAuction);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            
            assertNotNull(market);
            assertFalse(market.getAuctionSale());
        }

        //market with wrong auction type gets set with false
        @Test
        public void testMarketAuctionWrong() {
            ReadingJSON.setMarketFileName(fileNameMarketAuctionWrong);
            Marketplace market = ReadingJSON.readMarketFile(games, users);

            assertNotNull(market);
            assertFalse(market.getAuctionSale());
            assertEquals(2, market.getGamesOnSale().size());

            ArrayList<String> sellers = new ArrayList<String>(market.getGamesOnSale().keySet());
            assertEquals(2, sellers.size());
        }

        //market with no uid gets set to 0
        @Test
        public void testMarketNoUID() {
            ReadingJSON.setMarketFileName(fileNameMarketNoUID);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);
            assertTrue(market.getAuctionSale());
            assertEquals(0, market.getUid());
            assertEquals(2, market.getGamesOnSale().size());

            ArrayList<Game> gamesNames = new ArrayList<Game>();
            for (String s : market.getGamesOnSale().keySet()) {
                gamesNames.addAll(market.getGamesOnSale().get(s));
            }
            assertEquals(3, gamesNames.size());
        }

        //market that is correct gets initialized properly
        @Test
        public void testMarketGood() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);
            assertFalse(market.getAuctionSale());
            assertEquals(2, market.getGamesOnSale().size());
            assertEquals(4, market.getUid());

            ArrayList<Game> gamesNames = new ArrayList<Game>();
            for (String s : market.getGamesOnSale().keySet()) {
                gamesNames.addAll(market.getGamesOnSale().get(s));
            }
            assertEquals(3, gamesNames.size());
        }
        
        
    }

    /** This market errors tests specific errors when market is given wrong objects
     * 
     */
    @Nested
    class MarketErrorsTest {
        List<AbstractUser> users;
        List<Game> games;
        Marketplace market;
        

        @BeforeEach
        void beforeEach() {
            ReadingJSON.setGameFileName(fileNameGameGood);
            ReadingJSON.setUserFileName(fileNameUserGood);
            ReadingJSON.setMarketFileName(fileNameMarketErrors);
            games = ReadingJSON.readGamesFile();
            users = ReadingJSON.readUsersFile(games);
            market = ReadingJSON.readMarketFile(games, users);
        }

        @AfterEach
        void afterEach() {
            games = new ArrayList<>();
            users = new ArrayList<>();
            market = new Marketplace();
        }

        // basic test that games and sizes are correct - sanity check
        @Test
        void sizeTest() {
            assertEquals(4, games.size());
            assertEquals(3, users.size());
        }
        
        // get number of games and uid
        @Test
        public void testMarketBasic() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);
            assertEquals(2, market.getGamesOnSale().size());
            assertEquals(4, market.getUid());
        }

        //duplicate sellers ignored
        @Test
        public void testMarketDuplicateSeller() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);

            ArrayList<Game> userGames = market.getGamesOnSale().get("S1");
            ArrayList<Integer> gameIDS = new ArrayList<Integer>();
            ArrayList<Integer> correctIDS = new ArrayList<Integer>(Arrays.asList(1, 3));
            for (Game g : userGames) {
                gameIDS.add(g.getUniqueID());
            }
            
            assertEquals(2, userGames.size());
            assertEquals(new ArrayList<>(), unequalIntegers(gameIDS, correctIDS));
        }

        //duplicate ids within a sellers games on sale are ignored - only first one considered
        @Test
        public void testMarketDuplicateID() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);

            ArrayList<Game> userGames = market.getGamesOnSale().get("A1");
            ArrayList<Integer> gameIDS = new ArrayList<Integer>();
            ArrayList<Integer> correctIDS = new ArrayList<Integer>(Collections.singletonList(2));
            for (Game g : userGames) {
                gameIDS.add(g.getUniqueID());
            }

            assertEquals(1, userGames.size());
            assertEquals(new ArrayList<>(), unequalIntegers(gameIDS, correctIDS));
        }

        // does not add a game that does not previously exist from game file reading
        @Test
        public void testMarketNoExistGame() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);

            ArrayList<Game> userGames = market.getGamesOnSale().get("A1");
            ArrayList<Integer> gameIDS = new ArrayList<Integer>();
            ArrayList<Integer> correctIDS = new ArrayList<Integer>(Collections.singletonList(2));
            for (Game g : userGames) {
                gameIDS.add(g.getUniqueID());
            }

            assertEquals(1, userGames.size());
            assertEquals(new ArrayList<>(), unequalIntegers(gameIDS, correctIDS));
        }

        //buy users not added to marketplace
        @Test
        public void testMarketBuyUser() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);

            assertNull(market.getGamesOnSale().get("B1"));
        }

        //non users not added (must be previously from users list
        @Test
        public void testMarketNonUser() {
            ReadingJSON.setMarketFileName(fileNameMarketGood);
            Marketplace market = ReadingJSON.readMarketFile(games, users);
            assertNotNull(market);

            assertNull(market.getGamesOnSale().get("A2"));
        }
        
    }
    
}
