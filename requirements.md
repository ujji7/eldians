# Requirements and Clarifications

**TODO: Add Requirements and clarifications as specified in the README.md**
## Part 1
### Daily Transaction Text Files (DTTF):
####Requirements:

####Clarifications:

- 
- 
    - 
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
  
    -Higher Priv(Admin);
      -Features: Create User, Delete User given username(username != current admin name)
        Addfunds(Override), given username and amount.
        Trigger AuctionSale
  
    - Lower Priv(NEED TO FIGURE OUT CLASS STRUCTURE);
      -Features: Buy, Sell, Addfunds(inherit), refund
    - Standard Buy only;
      - Features: Buy, refund, Addfunds(inherit)
    - Standard Seller
      - Features: Sell, Addfunds(inherit)
    - Standard Both
      - Features: Buy, Sell, Addfunds(inherit), refund
  
    -
#### Clarifications:
- whether users can have a negative account balance [they cannot]).
- can admin only addfunds, any possibility of withdrawal of funds for user?
- When does an Admin delete a USER?
- Do we implement a feature of add NEW games to the inventory of the seller? Perhaps have the Admin implement this transaction
### Code Smells:
#### Requirements:
- not to hard code values into your program(NO MAGIC #s)
#### Clarifications:

### Game:
#### Requirements:
- Game name(=< 25 chracters), Price(=<), Sell( true = up for sale, false = in the inventory not for sale)

#### Clarifications:
- Upon auctionSale trigger ALL games go on sale or add special attributes/sub-classes?
- Games are not unique to sellers? I can have both Nintendo and EA selling the game PacMan?

### Transactions:
#### Requirements:
  - CREATE-01/DELETE-01: ex: "02 Epic Games      SS 000456.89" Username + Balance + Usertype needs to be valid
  - Login-00/Logout-10:  ex: "00 Epic Games      SS 000456.89" Username + Balance needs to be valid
  - Add credit-06: ex: "06 Epic Games      SS 000999.89" Username + Credit/day needs to be valid

  - Sell-03: ex: "03 My Epic Game              Epic Games 00.00 999.99" Game name, Game name, Username + Game cost needs to be valid
  - Refund-05 ex: 
  - Buy-04 ex:

#### Clarifications:
- Need to verify the lenght for game name in all the transaction logs
- For the sell transaction the 6 character price is the selling price including the discount?
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

