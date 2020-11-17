# Getting Started

### Reference Documentation
Starting this as a simple spring boot application is enough to view the following pages.
All APIs are available at the link http://localhost:8080/swagger-ui
To check the datatbase details go to http://localhost:8080/h2-console

UserController - A way to create the user. First user can be created by giving a dummy value in the header while creating the account.
AccountController - A way to manage bank accounts.
TransactionController - A way to  manage transactions.
PayeeController - A wy to manage payees.

All above controllers may have their  own versions for employee and customer. they may have different rights for editing some information.

This is defined in the idea of having a front end cover for some of the validations etc... 
I have skipped a lot of validations and such other things for making it less complicated.
  