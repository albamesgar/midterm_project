# Midterm Project - Alba Mesa
## Welcome to the banking system simulation API
This is an API simulating a banking service. With this API users can be added, modified and removed, and the same can be done with accounts. 
All these actions are secured with HTTP Basic Authentitication, so depending on your role you will be able to do some actions or some others.

### API structure
#### Roles
There are two roles: Administrators (*ADMIN*) and Account Holders (*ACCOUNT_HOLDER*). Administrators can create users and accounts. They can also modify the data of
the users and the accounts, including modifying the balance of any account. Finally, they can also delete users and accounts from the database, having in mind that at least
one administrator should exist in the database. Account Holders can access to their own accounts and they can transfer founds to another account (regardless of owner).

Both roles and an administration are going to be included in the database once all the installation and setup explained in the following lines are done.

#### Users
Althought there are only two roles, there are three types if users in this banking system. There are Administrators (admins) and Account Holders, as it has been explained before,
and there are also Third Parties. A Third Party doesn't have any account related and the only things that can do is to receive and send money to other accounts.

Admins have the following attributes:
- username
- password
- role

Account holders have the same attributes than admins and also:
- date of birth
- primary address
- mailing address (optional)

Third parties only have the following attributes:
- hashed key
- name

#### Accounts
There are four kind of accounts: Checking, Student Checking, Credit Card, and Savings accounts. Administrators can only call for the creation of three of these four kind of accounts.
They can call for the creation of a Credit Card account, a Savings account, and a Checking account. For the last case, a Student Checking account will be created automatically 
instead of a Checking account if the primary owner is younger than 24. Interests and fees will be applied automatically to any account when it is accessed.

All accounts have the following attributes:
- balance
- primary owner
- secondary owner (optional)
- penalty fee: always 40
- secret key
- creation date
- status (ACTIVE or FROZEN)
- account type (CHECKING, STUDENT_CHECKING, CREDIT_CARD or SAVINGS)

Student Checking accounts don't have any other attribute.

Checking accounts have also the following attributes:
- minimum balance (always 250)
- monthly maintenance fee (always 12)

Credit Card accounts also have:
- credit limit (100 by default and 100000 as a maximum)
- interest rate (0.2 by default and 0.1 as a minimum)

Savings accounts have also the following attributes:
- minimum balance (1000 by default and 100 as a minimum)
- interest rate (0.0025 by default and 0.5 as a maximum)

#### Transactions
All the transactions done (transferences and third parties refund and discharges) are stored in the database together with their type, date, the amount transfered, 
the receiving account and the third party doing it or the sending account.
The application recognizes patterns that indicate fraud and freeze the account status when potential fraud is detected. Patterns that indicate fraud include:

- Transactions made in 24 hours total to more than 150% of the customers highest daily total transactions in any other 24 hour period.
- More than 2 transactions occurring on a single account within a 1 second period.

All the roles, users, accounts and transactions have also an Id attribute.

## Getting Started
To get a local copy up and running follow these simple steps.
1. Clone the repository in your computer
2. Start your MySql Workbench and run the following code to create the database, the roles and the first administrator:

```sh
-- Create midterm and midterm_test schemas
DROP SCHEMA IF EXISTS midterm;
CREATE SCHEMA midterm;
USE midterm;

DROP SCHEMA IF EXISTS midterm_test;
CREATE SCHEMA midterm_test;

-- Insert roles into database
INSERT INTO role (name) VALUES
("ADMIN"),
("ACCOUNT_HOLDER");

-- Insert admin into database 
INSERT INTO user(username, password, role_id) VALUES
("Lua","$2a$10$IPv2SNSsFeueY67KnCl/YOBK2NVEgVm/qvF0Ou2nF8sovt30ydV8i", 1);

INSERT INTO admin(id) VALUES
(1);
```

3. Run the application
4. Call any command with the authentication of the created admin:
  - username: Lua
  - password: 1234
  
## Usage
Start the application by running MidtermProjectApplication class or inserting in the terminal of the project ```mvn spring-boot:run```. The application is based on data sent via API, so in order to use the functionality you need to open a browser at http://localhost:8080 following the address of a specific request.

Since the application includes JSON formatted data transfer as well as requires data to be provided in the header, using Postman application is highly recommended.

### Pathways
#### Actions that can be done by an **ADMIN**
- Accounts pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
GET | /accounts | NONE | Show all accounts in the database
GET | /accounts/checkings | NONE | Show all checking accounts in the database
GET | /accounts/student-checkings | NONE | Show all student checking accounts in the database
GET | /accounts/credit-cards | NONE | Show all credit card accounts in the database
GET | /accounts/savings | NONE | Show all savings accounts in the database
GET | /accounts/{id} | Long - AccountId | Show account with the corresponding Id
POST | /new/checking | CheckingDTO | Create a checking/student-checking account
POST | /new/credit-card | CreditCardDTO | Create a credit card account
POST | /new/savings | SavingsDTO | Create a savings account
PUT | /accounts/checkings/{id}/modify-data | Long - AccountId, CheckingDTO | Modify a checking/student-checking account
PUT | /accounts/credit-cards/{id}/modify-data | Long - AccountId, CreditCardDTO | Modify a credit card account
PUT | /accounts/savings/{id}/modify-data | Long - AccountId, SavingsDTO | Modify a savings account
PATCH | /accounts/{id}/modify-balance | Long - AccountId, BalanceDTO | Modify the balance of an account
DELETE | /delete/account/{id} | Long - AccountId | Delete an account

- Users pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
GET | /users | NONE | Show all admins and account holders in the database
GET | /users/admins | NONE | Show all admins in the database
GET | /users/account-holders | NONE | Show all account holders in the database
GET | /users/third-parties | NONE | Show all third parties in the database
GET | /users/{id} | Long - UserId | Show user with the corresponding Id
GET | /users/third-parties/{id} | Long - UserId | Show third party with the corresponding Id
GET | /my-user | NONE | Show the corresponding user information
POST | /new/admin | AdminDTO | Create an admin user
POST | /new/account-holder | AccountHolderDTO | Create an account holder user
POST | /new/third-party | ThirdPartyDTO | Create a third-party
PUT | /users/admins/{id}/modify-data | Long - UserId, AdminDTO | Modify an admin user
PUT | /users/account-holders/{id}/modify-data | Long - UserId, AccountHolderDTO | Modify an account holder user
PUT | /users/third-parties/{id}/modify-data | Long - UserId, ThirdPartyDTO | Modify a third-party
DELETE | /delete/user/{id}} | Long - UsertId | Delete an user
DELETE | /delete/third-party/{id}} | Long - UsertId | Delete a third party

#### Actions that can be done by an **ACCOUNT_HOLDER**
- Accounts pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
GET | /my-accounts | NONE | Show all accounts of the account holder in the database
GET | /my-accounts/checkings | NONE | Show all checking accounts of the account holder in the database
GET | /my-accounts/student-checkings | NONE | Show all student checking accounts of the account holder in the database
GET | /my-accounts/credit-cards | NONE | Show all credit card accounts of the account holder in the database
GET | /my-accounts/savings | NONE | Show all savings accounts of the account holder in the database
GET | /my-accounts/{id} | Long - AccountId | Show account of the account holder with the corresponding Id

- Users pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
GET | /my-user | NONE | Show the corresponding user information

- Transactions pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
POST | /my-accounts/{id}/transfer | Long - AccountId, TransferDTO | Do a transfer from my account to another account

#### Actions that can be done by anyone
- Transactions pathways:

Route Type | Route | Input Required | Action Done
--- | --- | --- |--- 
POST | /third-party/{hashedKey}/refund | ThirdPartyTransactionDTO | Do a refund to an account
POST | /third-party/{hashedKey}/discharge | ThirdPartyTransactionDTO | Do a discharge to an account

### DTO Templates
Please note: In all DTOs, any reference to secondary owner is optional, and can be removed if not required. The same happens with the mailing address.

#### Accounts
- CheckingDTO
- 
```sh
{"balance":{"amount":NUMBER, "currency":CURRENCY}, "primaryOwnerId":USERID, "secondaryOwnerId":USERID, "secretKey":STRING}
```

- CreditCardDTO (creditLimitAmount, creditLimitCurrency and interestRate are optional)

```sh
{"balance":{"amount":NUMBER, "currency":CURRENCY}, "primaryOwnerId":USERID, "secondaryOwnerId":USERID, "secretKey":STRING, "creditLimitAmount":NUMBER, "creditLimitCurrency":CURRENCY, "interestRate":DECIMAL}
```

- SavingsDTO (minimumBalanceAmount, minimumBalanceCurrency and interestRate are optional)

```sh
{"balance":{"amount":NUMBER, "currency":CURRENCY}, "primaryOwnerId":USERID, "secondaryOwnerId":USERID, "secretKey":STRING, "minimumBalanceAmount":NUMBER, "minimumBalanceCurrency":CURRENCY, "interestRate":DECIMAL}
```

- BalanceDTO

```sh
{"newBalance":{"amount":NUMBER, "currency":CURRENCY}}
```

#### Users
- AdminDTO

```sh
{"username":STRING, "password":STRING, "role":STRING}
```

- AccountHolderDTO

```sh
{"username":STRING, "password":STRING, "role":STRING, "dateOfBirth":"yyyy-mm-dd", "primaryAddress":{"street":STRING, "homeNumber":NUMBER, "city":STRING, "postalCode":NUMBER, "country":STRING}, "mailingAddress":{"street":STRING, "homeNumber":NUMBER, "city":STRING, "postalCode":NUMBER, "country":STRING}}
```

- ThirdPartyDTO

```sh
{"hashedKey":STRING, "name":STRING}
```

#### Transactions
- TransferDTO

```sh
{"amount":{"amount":NUMBER, "currency":CURRENCY}, "ownerName":STRING, "accountId":NUMBER}
```

- ThirdPartyTransactionDTO

```sh
{"amount":{"amount":NUMBER, "currency":CURRENCY},  "accountId":NUMBER, "accountSecretKey":STRING}
```
