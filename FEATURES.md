# Features

**TODO: Add features of the program**\
Our system requires the GSON Maven Repository.\
To load GSON package:\
Go to this website: \
https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.6

It should have categories such as: License, Categories, Date, Files, etc.\
On the right of Files, click on the file (it should say jar 234kb).

You may get an error saying the file is harmful, just ignore it and continue to download the file.\
Now, go back to intellij and open up the a2-eldians folder. \
Click File from the toolbar\
 Go to Project Structure (CTRL + SHIFT + ALT + S on Windows/Linux, ⌘ + ; on Mac OS X)\
 Select Modules at the left panel\
 Go to Dependencies tab\
 '+' → JARs or directories\
 Go to where you stored the downloaded jar file and select it. \
 Click apply\
 Click ok.\
It should work now.\

File > Settings > Editor > General > Scroll down to On Save \
Ensure that "Remove trailing spaces on" is unchecked.

## DATABASE READING/WRITING: 
Done using GSON library that is used to convert Java Objects into a JSON representation and vice versa:
There must be 3 files, one for the game objects, one for the user objects, and one for 
the Marketplace object. Indenting and new lines do not matter for the files, as json 

games file must be named: games.json, users file must be named users.json and marketplace file must
be name market.json

### GAMES FILE: \
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

### USERS FILE:
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
"id": 3
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
  * Each game object in the inventory must be in the format: {"id": 3 }, where 3 is the unique id of the game
  * an inventory with more than one game would look like: [{"id": 3 }, {"id": 4 }]
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

### MARKETPLACE FILE: 
This is a list of each seller, and the games they are selling

Must be in the given format, where the file begins with a '{' and ends with a '}':

{"auctionSale": false,
"Uid": 0,
"gamesOnSale": {
"Alice": [{"id": 1 }, {"id": 3 }],
"admin1": [{"id": 2 }, {"id": 101 }
]}}

Where:
* "auctionSale" is a boolean telling whether the auction sale is on (true) or off (false)
  * if no "auctionSale" attribute exists in the file, or it is not a boolean, it will be defaulted to false
* "Uid" is a integer telling the correct unique id in the database
    * As more games are added, this Uid increases to allow for new games with unique ids to be created.
    * If there is no Uid, or it is not an integer, or it is <0, it will be default set to 0
    
* "gamesOnSale" is the actual marketplace, consisting of usernames which correspond to a list of 
game unique ids
  * if "gamesOnSale" does not exist or does not follow the specified format, an empty marketplace will be created
  * If any username does not already exist in the system (from the prior reading of the users file), 
 that user and their list of games will not be added to the market
  * Each id in the user's list will be checked for its existence from the games file - only if 
 it exists and that game is being sold by this seller will it be added to the marketplace
  * If any game object does not match the given format (1 instead of {"id": 1}), that game will not be
    added to the seller's list of games.
  * duplicate unique ids will be ignored.  

If the entire market file does not follow the specified format, aka an empty file, a file with just "aknda"
would lead to the creation of a new, empty Marketplace with auction sale set as false.