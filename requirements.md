# Requirements and Clarifications

**TODO: Add Requirements and clarifications as specified in the README.md**
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

##Refund:
####Requirements:

####Clarifications:



##AddCredit:
-limit of 1000 per day

##AuctionSale:
-applies a discount to the price

### Users:
#### Requirements:
- username(=< 15 chars, UNIQUE), account balance(=<999,999), and inventory of games that they own or have put up for purchase.
    - thinking some sort of parent class
    - Having a USER abstract class with attribute "name"
      -Having:
        - buyers 
        - sellers
        - full-standard users
          - (who can buy and sell)
        - system staff
          - (admin users) 
        Extend from the USER with addition attributes,
            furthermore I am thinking of having another abstract class for buyer/seller/both.
            So essentially User extends 2 classes(Higher Priv, Lower privlage).
            Lower priv -> Buy/Sell/Both
            Higher priv -> Admin, higher priv can just be a normal child class not necessarily an abs class
            
    - User(Abstract):
      - Features: Addfunds
  
    - Higher Priv(Admin)
        - Features: Create User, Delete User given username(username != current admin name)
        Addfunds(Override), given username and amount.
        Trigger AuctionSale
  
    - Lower Priv(NEED TO FIGURE OUT CLASS STRUCTURE);
      - Features: Buy, Sell, Addfunds(inherit), refund
    - Standard Buy only;
      - Features: Buy, refund, Addfunds(inherit)
    - Standard Seller
      - Features: Sell, Addfunds(inherit)
    - Standard Both
      - Features: Buy, Sell, Addfunds(inherit), refund
  
    -Have a parent User class with all the methodologies
#### Clarifications:
- whether users can have a negative account balance [they cannot]).
- can admin only addfunds, any possibility of withdrawal of funds for user?
- When does an Admin delete a USER?
- Do we implement a feature of add NEW games to the inventory of the seller? Perhaps have the Admin implement this transaction

- are we supposed to keep each user from each run of a DTTF

### Code Smells:
#### Requirements:
- not to hard code values into your program(NO MAGIC #s)
#### Clarifications:

### Game:
#### Requirements:
- Game name(=< 25 chracters)
- Price(=<),
- Sell( true = up for sale, false = in the inventory not for sale)

#### Clarifications:
- Upon auctionSale trigger ALL games go on sale or add special attributes/sub-classes?
- Games are not unique to sellers? I can have both Nintendo and EA selling the game PacMan?

### Transactions:
#### Requirements:
  - CREATE-01/DELETE-02: ex: "02 Epic Games      SS 000456.89" Username + Balance + Usertype needs to be valid
  - Login-00/Logout-10:  ex: "00 Epic Games      SS 000456.89" Username + Balance needs to be valid
  - Add credit-06: ex: "06 Epic Games      SS 000999.89" Username + Credit/day needs to be valid

  - Sell-03: ex: "03 My Epic Game              Epic Games 00.00 999.99" Game name, Game name, Username + Game cost needs to be valid
  - Refund-05 ex: 
  - Buy-04 ex:
    
  - Need to verify the length for game name in all the transaction logs - the maximum length of a game name is 25 characters


#### Clarifications:
- For the sell transaction the 6 character price is the selling price including the discount?
  - I think we should check and match with prior transactions -DCT
- So if a game is put up for sale on March 2nd 11:59PM can it be put up for being purchased at March 3rd 12:00AM or do we put it up at March 3rd 11:59PM? Is this requirement calendar specific or time-specific?
- Add credit: again 1000.00/day is limit date specific or time-specific(wait 24hrs before next add)?
- Is there a default scenario for when Admin deletes a user? Perhaps when no inventory and no balance


### Back End Error Reporting:
#### Requirements:
- every return false if statement NEEDS a failed constraint error msg
- throwing exceptions with fatal errors as much as possible instead of no returns

#### Clarifications:
- So let's say I buy a game, max out or close to max out my account balance then ask for a refund. Should I Send an error to the USER for max account balance?



### Data Output Structure
#### Requirements:
- I think having like a standard format would be really clean

#### Clarifications:

###Marketplace
- a hashmap of all the availiable sellers with their games for sale as the values