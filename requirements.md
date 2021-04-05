# Requirements and Clarifications

**TODO: Add Requirements and clarifications as specified in the README.md**
A workflowy where additional requirements and clraifications are stored:

## Part 1
### Daily Transaction Text Files (DTTF):
####Requirements:
- Must be of .txt format
- Needs to be stored in the same folder that is the root of the project


####Clarifications:

- Q) does each transaction file count as a day?
    - Yes each transaction file counts as a day 
- Q) Invalid format for a transaction in the file is considered a fatal error?
    -  Yes

    
###Transactions:
- we'll need to save and load each user to and from a .txt
    - will have to hold every attribute
    
##Sell:
####Requirements:
- The transaction requires Game name the price and the discount to be offered for the game
- Game Name: an alphanumeric String of max length of 25
- Game price: a positive 2dp significant double that can not exceed 999.99
- Discount: a positive 2dp significant float that can exceed 90.00

####Clarifications:
- Q) Can an admin user put up a game for other users?
    - No
    
- Q) Can a full-Standard/Admin user put up a Game for sale that is in their inventory?
    - No

- Q) Can I have multiple copies of the same Game with different prices/discounts up for sale? 
    - No, you can only have a single copy up for Sale
    
- Q) Do you execute the transaction, if the Seller's name is not provided or does not match the current logged-in User's name?
    - Yes, display an error but execute the transaction.

##Buy:
####Requirements:
- The transaction requires a game name and a seller name
- Game Name: an alphanumeric String of max length of 25
- Seller, a valid Seller selling the game


####Clarifications:
- Q) Can an admin user buy a game on behalf of other users?
    - No

- Q) Can a full-Standard/Admin user buy a Game that they are selling?
    - No
    
- Q) Can I buy same game titles from different Sellers?
    - No

- Q) Do you execute the transaction, if the Buyer's name is not provided or does not match the current logged-in User's name?
    - Yes, display an error but execute the transaction.
    
- Q) What is to be done is the case when a Seller's account will be maxed out upon the transaction?
    - Execute the transaction and max out the Seller's account, print out a warning

- Q) Restrictions to buying games?
    - cannot buy a game that is sold the same day

##Refund:
####Requirements:
- The transaction requires a valid Buyer's and Seller's user name and amount to be refunded
- Name and Buyer the same Username formats
- A valid positive amount with max amount =< 999,999.99
- Transaction is only made by Admin user.

####Clarifications:
- Q) What to do in the case of Buyer's account maxing out upon refund?
    - Fail the transaction and prompt User with an error message
    

##AddCredit:
####Requirements:
- limit of 1000.00 per day
- In Admin mode: Requires a valid Username or the current Admin's username in the transaction code to execute the transaction


####Clarifications:
- Q) For a non-Privlaged user if the username is not provided or is invalid do we execute the transaction?
    - Yes

- Q) In Privlaged mode if the username received in the transaction is invalid or empty do we execute the add-Credit on Admin?
    - No, in Privlaged mode for admin to execute addCredit the Username must either match their name or a valid Username for the transaction to execute
    
- Q) If the current transaction would exceed the daily limit do we add the diffrence to the User?
    - No, do not execute the transaction and print out a warning.

##AuctionSale:
####Requirements:
- 07 is the transaction code for Auction Sale
- Only admin can toggle the auction-sale in the MarketPlace

####Clarifications:
- Q) Do we carry out the current auction-sale toggle instance to the next day?
    - Yes, we keep a record of the state of the auction-sale.

##Gift:
####Requirements:
- The game being gifted must either be in the sender's inventory or they must be selling it
- The receiver must be a valid User that is not of type Standard Sell
- In privileged mode the provided user-names must be valid for both the sender and the receiver.



####Clarifications:
- Q) In admin mode if the transaction received does not have a valid sender user-name. Do we execute the transaction on behalf of the admin?
    - No, in admin mode you need valid name for both the users in order for the transaction to go through
    

##Remove Game:
####Requirements:
- The game being removed must be in the User's inventory or they must have it up for Sale on the Market.
- The game cannot be on-hold(ie must be from a previous day)
- In privileged mode the provided user-names must be valid for the person who's game is being removed.

####Clarifications:
- Q) In admin mode if the transaction received does not have a valid user-name. Do we execute the transaction on behalf of the admin?
    - No, in admin mode you need valid username for the transaction to go through
    

## Users:
### Requirements:
- username(=< 15 chars, UNIQUE), account balance(=<999,999.99), and inventory of games that they own or have put up for purchase.
    - Having a USER abstract class with attribute "name"
      -Having:
        - buyers can only buy games
        - sellers can only sell games
        - full-standard users can buy and sell
        - Admin can do everything full standard can do plus refund delete and create
    
    
### Clarifications:
- users cannot have a negative account balance
- users cannot withdraw funds
- Admin cannot delete themselves 

### Code Smells:
- Code repetition
- Methods less than 35 lines
- Clear Java Docs
- Proper Use of inheritance

#### Requirements:
- not to hard code values into your program(NO MAGIC #s)
#### Clarifications:

### Game:
#### Requirements:
- Game name(=< 25 characters)
- Price(=< 999.99),
- discount less than or equal to 90

#### Clarifications:
- Q) Upon auctionSale trigger ALL games go on sale or add special attributes/sub-classes?
    - Yes, all games go up on sale, the game with 0.00 discount are not up for sale.
    
- Q) Games are not unique to sellers? I can have both Nintendo and EA selling the game PacMan?
    - Yes, multiple sellers can be selling the same game.  

### Transactions:
#### Requirements:
  - Create-01/Delete-02: ex: "02 Epic Games      SS 000456.89" Username + Balance + Usertype needs to be valid
  - Login-00/Logout-10:  ex: "00 Epic Games      SS 000456.89" Username + Balance needs to be valid
  - Add credit-06: ex: "06 Epic Games      SS 000999.89" Username + Credit/day needs to be valid

  - Sell-03: ex: "03 My Epic Game              Epic Games 00.00 999.99" Game name, Game name, Username + Game cost needs to be valid
  - Refund-05 ex: "05 admin2          admin1          000000.01"
  - Buy-04 ex: "04 Valorant                  admin2          admin1         "
    
  - Need to verify the length for game name in all the transaction logs - the maximum length of a game name is 25 characters

### Back End Error Reporting:
#### Requirements:
- every return false if statement NEEDS a failed constraint error msg
- throwing exceptions with fatal errors as much as possible instead of no returns


### Data Storage
#### Requirements:
- database must store previous day transactions including users and games


