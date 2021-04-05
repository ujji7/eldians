# Features

## Planning

To see a UML Diagram of how we built our system, look at EldiaTrade (5).pdf located in this repository.

## Setup for the system:
###GSON Repository
Our system requires the GSON Maven Repository.\
To load GSON package:\
Go to this website: \
https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.6

It should have categories such as: License, Categories, Date, Files, etc.\
On the right of Files, click on the file (it should say jar 234kb).

You may get an error saying the file is harmful, just ignore it and continue to download the file. Remember where you 
stored it.\
Now, go back to intellij and open up the a2-eldians folder. \
Click File from the toolbar\
 Go to Project Structure (CTRL + SHIFT + ALT + S on Windows/Linux, ⌘ + ; on Mac OS X)\
 Select Modules at the left panel\
 Go to Dependencies tab\
 '+' → JARs or directories\
 Go to where you stored the downloaded jar file and select it. \
 Click apply\
 Click ok.\
Now any errors with Json files (such as ReadingJSON) should disappear.

### Remove Trailing spaces on Intellij
File > Settings > Editor > General > Scroll down to On Save \
Ensure that "Remove trailing spaces on" is unchecked.

### Running the program 
Need database files and daily.txt file to run the program. Explained below in more detail.

To run our program, navigate to src/main/client as the client is our main method. Run client.main.

Within the main method is the name of the daily.txt file to read for transactions. Set it appropriately.

## DATABASE READING/WRITING: 
Done using GSON library that is used to convert Java Objects into a JSON representation and vice versa:
There must be 3 files, one for the game objects, one for the user objects, and one for
the Marketplace object. Indenting and new lines do not matter for the files, as json only looks for brackets '{'
These 3 files must be stored in the a2-eldians directory (not in src, but outside of it)

games file is by default named: games.json, users file = users.json and marketplace file is market.json

If you would like to change the name of database files, rename the attributes in the application file. 


### GAMES FILE: (games.json)
Must be in the given format, where the file begins with a '[' and ends with a ']': \
[{
"name": "Game1",
"price": 20.00,
"supplierID": "Alice",
"uniqueID": 1,
"discount": 10.06,
"onHold": false
},
{
"name": "Game2",
"price": 20.99,
"supplierID": "admin1",
"uniqueID": 2,
"discount": 9.85,
"onHold": true
}]

Where:
* "name" corresponds to the name of the game (String) 
* "price" is the price of the game (double),
* "supplierID" is the name of the seller (String)
* "uniqueID" is a unique ID given to a game (integer)
* "discount" is the discount applied to a game (double)
* "onHold" is a boolean attribute that specifies if a game is on Hold 
  * True means it cannot be bought/removed today as it is on Hold
  * At the beginning of each day, the onHold attribute is changed to false, as a new day has begun.

The unique id's must begin with 1 and increase as more and more games are added. If another 
game is written into the system with the same unique id as one that has already been created, it will not be 
added to the system. 

If any game object does not follow the specifications, it will not be added to the system.
If the entire game file does not follow the specified format, aka an empty file, a file with just "," 
would lead to the creation of an empty list of games.


### USERS FILE (users.json):
Must be in the given format, where the file begins with a '[' and ends with a ']':

[{
"username": "Alice",
"type": "SS",
"accountBalance": 117.56,
"inventory": [],
"newFunds": 0.0,
"transactionHistory": []
},
{
"username": "admin1",
"type": "AA",
"accountBalance": 112.0,
"inventory": [
{
"uniqueID": 3
}
],
"newFunds": 0.0,
"transactionHistory": [
"Created Alice user",
"Created Bob user",
"Bought Clue from Alice",
"User: admin1 is now selling GameCool for 23.56"]
}]

Where:
* "username" is the name of the user (String)
* "type" is the user type (String)
* "accountBalance" is the user's credit (double)
* "inventory" is the games that the user has bought (list)
  * If the user is a sell type user, the inventory must be empty (as sell type users cannot buy games), or the user 
    will not be created
  * Each game object in the inventory must be in the format: {"uniqueID": 3 }, where 3 is the unique id of the game
  * an inventory with more than one game would look like: [{"uniqueID": 3 }, {"uniqueID": 4 }]
* "newFunds" is the new funds added this day
  * by default, this would be 0 (and if not, it would be set to 0) as a new day has begun
  * this is an optional parameter that does not need to be in the file
* "transactionHistory" is the user's transaction history
  * transaction History is all the transactions this user has completed to date including but not limited to buying and 
 selling games, creating and deleting users.
  * if the user has not done any transactions, this attribute must be an empty list like: [] 

All user objects must have all these attributes (except for newFunds) regardless of whether the attribute is empty or not in order
to be added to the system.\
If any user object does not follow the specifications, it will not be added to the system.
If the entire users file does not follow the specified format, aka an empty file, a file with just ","
would lead to the creation of an empty list of users.


### MARKETPLACE FILE (marketplace.json): 

This is a list of each seller, and the games they are selling

Must be in the given format, where the file begins with a '{' and ends with a '}':

{"auctionSale": false,
"uid": 0,
"gamesOnSale": {
"Alice": [{"uniqueID": 1 }, {"uniqueID": 3 }],
"admin1": [{"uniqueID": 2 }, {"uniqueID": 101 }
]}}

Where:
* "auctionSale" is a boolean telling whether the auction sale is on (true) or off (false)
  * if no "auctionSale" attribute exists in the file, or it is not a boolean, it will be defaulted to false
* "uid" is an integer telling the correct unique id in the database
    * As more games are added, this Uid increases to allow for new games with unique ids to be created.
    * If there is no uid, or it is not an integer, or it is <0, it will be default set to 0
* "gamesOnSale" is the actual marketplace, consisting of usernames which correspond to a list of 
game unique ids
  * if "gamesOnSale" does not exist or does not follow the specified format, an empty marketplace will be created
  * If any username does not already exist in the system (from the prior reading of the users file), 
 that user and their list of games will not be added to the market
  * Each id in the user's list will be checked for its existence from the games file - only if 
 it exists and that game is being sold by this seller will it be added to the marketplace
  * If any game object does not match the given format (1 instead of {"uniqueID": 1}), that game will not be
    added to the seller's list of games.
  * duplicate unique ids and duplicate sellers will be ignored - only the first occurring seller would be added.

If the entire market file does not follow the specified format, aka an empty file, a file with random words such as
just "agenda" would lead to the creation of a new, empty Marketplace with auction sale set as false, and uid set to 0.

##Client
Reads the daily.txt file and sends the transaction details to the transaction factory to for the Transactions to be made.
In order for the Client to read the daily.txt the file needs to be placed in the folder that is the source root of the program.
Client's job is to first read the daily.txt, check for valid formatted transactions then send each transaction to the Transaction Factory to
get made and then finally send these transaction objects which are received from the factory to the application for execution.

The Client basically deals with all the fatal errors related to the format of the transactions that are received from the dailt.txt file
We used REGEX to check for the right formatting for each transaction type.
Unfortunately due to the way we have the Client running and setup we were unable to display the Junit tests for Cient but
locally on the side we were and did test all the possible scenarios and have included the test sample .txt files used for testing with our submission. 

## Transaction Creation / TransactionFactory.java and Transaction.java

The TransactionFactory uses the factory design pattern to create Transaction objects.

* The method TransactionFactory.buildTransaction takes in an ArrayList of strings where:
    * The ArrayList is a single line from the daily.txt broken up into attributes.
    * each string represents an attribute required for the transaction being created (transaction type, username, user type, etc).
* The first string in the ArrayList is always the type of transaction, which tells the factory what transaction
to create and what attributes the rest of the strings in theArrayList represent.

We chose to use the factory design pattern here to simplify the creation of Transaction objects and allow each
Transaction to handle itself through its Transaction.execute method depending on its type. This simplified our
code as well and made it much easier to implement good unit tests since we could test each Transaction object
in isolation.



## Running the program
Ensure that all 4 files (daily.txt, games.json, users.json, and market.json) are in the a2-eldians directory\
Run client.main file. 

## User (Builder)

We chose to use the builder design pattern to create our 4 users as having multiple constructors for if an account 
balance is given, if a transaction history and inventory exist were very disorganized. To create users in the daily.txt 
vs database files used different constructors.



### Abstract User
We chose to use an abstract user because many of the users had the same method.
They were only implemented differently in certain user types which we chose to override  
in their respective classes. All users have a:
- Username 
- Type (SS = sell standard, BS = buy standard, FS = full standard, AA = admin) 
- Inventory; which stores all the games they have bought (sellers always have an empty inventory)
- Account Balance; which stores their current amount of funds in their account
- New Funds; which is the credit they have added to their account today
- Transaction History; which lists all the transactions they have completed 



## Game
Each game object has a name, price, seller id (seller name) and discount. 

In addition, they also have an onHold attribute which signifies whether they can be bought/sold/gifted/removed.

- When a game is first sold on the marketplace, this on hold attribute is set to true as the game cannot
be used that day. 
- When a new day begins (ie. the database is read), the on hold attributes of all the games are set to 
false as a new day has occurred.
- When a game is bought, in the buyer's inventory, the game on hold attribute is set to true as right after
a game is bought, it cannot be removed or gifted. 


## Marketplace

The Marketplace stores 
- A hashmap called gamesOnSale which has the username of a seller as the key and an ArrayList of games they have on sale as the value
- An integer called uid which stores the highest game unique id in the system, used for assigning new unique id's
- A boolean called auctionsale which is true when an auctionsale is on and false when an auctionsale is off.

The marketplace has built in methods which help simplify transactions executed by Transaction objects and AbstractUser methods. We decided
to use an object to represent the marketplace for this reason.

##Application
We have an application class which executes all the transaction taken from the transaction factory. 


##Bonus Features
### Eldians Front End Application:

The Weblink to the Front End: https://daniellecreary-thomas.github.io/eldians-front/

Follow the youtube link to access the screen recorded tutorial for the front end: 
https://youtu.be/RCo_yipNkXc

The GitHub Repository for hosting:
https://github.com/DanielleCreary-Thomas/eldians-front

### Additional Features:
Every user has a transaction history that lists all the transactions they have done (ex. Buying, selling games,
creating/deleting users, etc)

##Testing
We ran into a slight issue with Junit testing there was an issue with the CRLF vs LF. We tried changing multiple setting
on intelliJ but nothing seemed to work.
Here the issue was that the test files which were written on Mac did not pass the tests on windows and vice-versa. So
here due to shortage of time we were unaable to effectively come up with a solution to resolve this issue. 
This issue is caused by
the line break of \n vs \r\n and visibly the output that is received is no different just the issue between CRLF vs LF.