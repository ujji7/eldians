## Overview

In this project, you will be required to design a working software application based on specifications provided (mimicking the sort of specifications you may receive from a real-life client).

## Part 1: Read the project specifications below

You will be creating the back-end of a digital distribution system for video games (similar to Valve Corporation's Steam) that allows users to buy or sell access to games.

Your program should work alongside a front-end interface that provides the back-end with data in the form of daily transaction text files, which contain a day's worth of transactions and user commands.

Your system will be used by four types of users: buyers, sellers, full-standard users (who can buy and sell), and system staff (admin users). Each user, including administators, will have a username, account balance, and inventory of games that they own or have put up for purchase.

### Transactions:

The Front End is capable of handling the following transactions, which will then be processed by your Back End:

**login** - start a Front End session

**logout** - end a Front End session

**create** - add a user with the ability to buy/sell games (privileged transaction)

**delete** - remove a user (privileged transaction)

**sell** - add a game to the user's inventory and to the list of games for sale

**buy** - purchase a game being sold by another user and add it to the user's inventory

**refund** - issue a credit to a buyer’s account from a seller’s account (privileged transaction)

**addcredit** - add credit directly into the system

**auctionsale** - change the prices of all games for sale to incorporate a seasonal discount (privileged transaction)

### Transaction Code Details:

**login** - start a Front End session

-   The front end will handle all of the login functionality, including passwords and security. You will not need to implement anything in your system to support this.

**logout** - end a Front End session

-   The front end will handle all of the logout functionality. You will not need to implement anything in your system to support this.

**create** – creates a new user with buying and/or selling privileges.

-   The front end will ask for the new username
-   The front end will ask for the type of user (admin or full-standard, buy-standard, sell-standard)
-   The front end will ask for the initial account balance of the new user
-   This information is saved to the daily transaction file
-   Constraints:
    -   privileged transaction - only accepted when logged in as admin user
    -   new user name is limited to at most 15 characters
    -   new user names must be different from all other current users
    -   maximum credit can be 999,999

**delete** - cancel any games for sale by the user and remove the user account.

-   The front end will ask for the username
-   This information is saved to the daily transaction file
-   Constraints:
    -   privileged transaction - only accepted when logged in as admin user
    -   username must be the name of an existing user but not the name of the current user
    -   no further transactions should be accepted on a deleted user’s behalf, nor should other users be able to purchase their games for sale

**sell** – put up a game for sale

-   The front end will ask for the game name
-   The front end will ask for the price of the game in dollars (e.g. 15.00)
-   The front end will ask for the sale discount when an auctionsale is taking place (e.g. 25.00 percent deducted)
-   This information is saved to the daily transaction file
-   Constraints:
    -   Semi-privileged transaction - only accepted when logged in any type of account except standard-buy.
    -   the maximum price for an game is 999.99
    -   the maximum length of an game name is 25 characters
    -   the maximum sale discount is 90 percent
    -   a game that was just put up for sale cannot be purchased until the following day.

**buy** – purchase an available game for sale

-   The front end will ask for the game name and the seller’s username
-   The price of the game should be deducted from the buyer's account balance and added to the seller's account balance
-   The game should be added to the buyer's inventory
-   This information is saved to the daily transaction file
-   Constraints:
    -   Semi-privileged transaction - only accepted when logged in any type of account except standard-sell.
    -   game name must be an existing game in the seller's inventory that is available for sale
    -   cannot purchase a game already in the user's inventory
    -   user must have enough available funds to purchase the game

**refund** - issue a credit to a buyer’s account from a seller’s account (privileged transaction)

-   The front end will ask for the buyer’s username, the seller’s username and the amount of credit to transfer.
-   The specified amount of credit should be transferred from the seller’s credit balance to the buyer’s credit balance.
-   This information is saved to the daily transaction file
-   Constraints:
    -   Buyer and seller both must be current users

**addcredit** - add credit into the system for the purchase of accounts

-   In admin mode, should ask for the amount of credit to add and the username of the account to which credit is being added.
-   In a standard account, should ask for the amount of credit to add to the user's own account.
-   This information is saved to the daily transaction file
-   Constraints:
    -   In admin mode, the username has to be an existing username in the system.
    -   A maximum of $1000.00 can be added to an account in a given day.

**auctionsale** - change the prices of all games for sale to incorporate a seasonal discount (privileged transaction)

-   Activate the discounts on all games for sale, changing the amount transferred during buy transactions
-   If an auctionsale is already on, this transaction should conclude the auctionsale and disable the discounts
-   Constraints:
    -   privileged transaction - only accepted when logged in as admin user

### Daily Transaction File:

At the end of each day, the front end provides a daily transaction file called daily.txt, listing every transaction made in the day.
Contains variable-length text lines of the following formats:

XX UUUUUUUUUUUUUUU TT CCCCCCCCC

Where:

-   XX
    -   is a two-digit transaction code: 00-login, 01-create, 02-delete, 06-addcredit, 10-logout
-   UUUUUUUUUUUUUUU
    -   is the username
-   TT
    -   is the user type (AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard)
-   CCCCCCCCC
    -   is the available credit

XX UUUUUUUUUUUUUUU SSSSSSSSSSSSSSS CCCCCCCCC

Where:

-   XX
    -   is a two-digit transaction code: 05-refund
-   UUUUUUUUUUUUUUU
    -   is the buyer’s username
-   SSSSSSSSSSSSSSS
    -   is the seller’s username
-   CCCCCCCCC
    -   is the refund credit

XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSS DDDDD PPPPPP

Where:

-   XX
    -   is a two-digit transaction code: 03-sell.
-   IIIIIIIIIIIIIIIIIII
    -   is the game name
-   SSSSSSSSSSSSSS
    -   is the seller’s username
-   DDDDD
    -   Is the discount percentage
-   PPPPPP
    -   is the sale price

XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSSSS UUUUUUUUUUUUUU

Where:

-   XX
    -   is a two-digit transaction code: 04-buy.
-   IIIIIIIIIIIIIIIIIII
    -   is the game name
-   SSSSSSSSSSSSSSS
    -   is the seller’s username
-   UUUUUUUUUUUUUUU
    -   is the buyer's username

Constraints:

-   numeric fields are right justified, filled with zeroes
    (e.g., 005.00 for a 5$ game)
-   alphabetic fields are left justified, filled with spaces
    (e.g. John Doe for account holder John Doe)
-   unused numeric fields are filled with zeros
    (e.g., 0000)
-   In a numeric field that is used to represent a monetary value or percentage, “.00” is appended to the end of the value (e.g. 00110.00 for 110)
-   unused alphabetic fields are filled with spaces (blanks)
    (e.g., Mike M         )
-   all sequences of transactions begin with a login (00) transaction code and end with a logout (10) transaction code

### Back End Error Recording:

All recorded errors should be of the form: ERROR: \<msg\>

-   For failed constraint errors, \<msg\> should contain the type and description of
    the error and the transaction that caused it to occur.
-   For fatal errors, \<msg\> should contain the type and description and the file that
    caused the error.

### Data output structure:

All output should be written to the screen using text. For example, your program can have println statements saying things like "$20.00 has been added to the balance of user Mike Miljanovic".

## Part 2: Test Suite

Conduct **White Box testing** by using junit tests.

## Part 3: Do a code sprint

Conduct one-week long code sprints.

### Git Workflow during code sprint

We will be learning to use a few new git commands through this assignment -- git branch, git checkout, and git merge. Read this nice tutorial for an overview and some examples of each of these commands (use the sidebar to navigate through the "Using branches" section): https://www.atlassian.com/git/tutorials/using-branches

For this project, each task will have its own "branch" within your repository where it is coded, and one person in your team (the "team manager") can be in charge of merging each branch to the "master" branch as tasks get completed. (Note: You do not need to make new branches for sub-tasks of each user story task if you have them (such as 3.1.x))

So, anytime you begin working on a new task, you should do the following:

-   git pull: this is very important to do before you start coding! This will pull the latest history for this branch from the remote (origin) and merge with the latest commit. Do this on a clean workspace (before you make any local changes), otherwise things get complicated.

-   git branch NEW_BRANCH where NEW_BRANCH is an identifying name for the task this branch will be dedicated to; this command branches off the latest version of working code from the remote

-   git checkout NEW_BRANCH: this command switches your working area to the branch that you want to checkout. Do NOT forget to do this after creating the new branch! or else you will still remain in master. (Note: You can type 'git branch' without anything after the command to get a list of all the available branches, and the current branch that you are working on will be highlighted).

## Part 4 (IMPORTANT): Tell us about your code and how to use your software

We will mainly use this file to grade your code. This means, if you do NOT complete these files, you will receive a grade of 0 in this assignment.

`FEATURES.md`

Complete the file `FEATURES.md` that explains how to run your code, and lists and describes all the features of your project, and which part of the provided specifications they fulfill. The description should mention any important design decisions you made in coding that feature (e.g. if you used inheritance, or a design pattern, etc. clearly let us know in this file, and briefly explain why you made this decision). If you have any bonus features you added in, clearly include this here with a title "Bonus feature".

This file should instruct the marker on how to run your program and any other information we need to set up and run your code. If you use other configuration files besides daily.txt (such as files for items or users) please include detailed instructions so that we know how we can and cannot modify those files in order to get your program to run.

## Notes

### Citing Code:

If you use any code you find, cite it according to the format described in the "Examples of citing code sources" section of the "Writing Code" page of the MIT Academic Integrity handbook here: https://integrity.mit.edu/handbook/writing-code

### Some other helpful git commands:

Check the current status of all the files in the repository:
git status

Tools for undo-ing all your local changes:
git reset --hard: put tracked files back as they were
git clean -fd: remove untracked files

#### Extensibility:

Keep in mind that your imaginary client may ask to add further requirements over the next month or so. The requests might involve expanding your software to include more features or handle more inputs. Be sure to design your code with this in mind.

In real life, you would be able to ask a contact with the distribution company for further clarification regarding the software they want from you. For the purposes of this project, you can direct such questions to the discussion board. Any response from instructors is to be taken as the company's response. You are also invited to do your own research regarding digital distribution systems. For example, what really happens when a game is put up for sale on Steam?
