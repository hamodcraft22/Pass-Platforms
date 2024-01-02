# Setting up & starting BackEnd

* Complete all previous steps (azure, database)

## Basic Setup
* replace `MANAGER_ID` with the actual manager id of the platform
* helps to use `ctrl + shift + r` in IntelliJ and replace

## Auth Setup
* inside the directory `src/main/java/PassPlatforms_Backend/Util/TokenValidation`, replace `TANENT_ID` on line 21 with the proper Tenant ID of the Active Directory
* inside the directory `src/main/java/PassPlatforms_Backend/Util/UsersService`, replace `TANENT_ID`, `CLIENT_ID`, `CLIENT_SECRET` on line 115, 12, 121 with the proper Tenant ID, Client ID, and Secret from the Active Directory

## Variables Setup
* inside the directory `src/main/resources/application.properties` replace `DB_URL`, `DB_USER`, `DB_PASS` on line 1,2,3 with the database url, the database usernam, and the password.
* also in the same file, replace `EMAIL`, and `PASS` with a designated outlook email and password (used to send the calnder invites) 

## Maven Install 
https://phoenixnap.com/kb/install-maven-windows

## To install dependencies, run the following command in the root directory of the FrontEnd Folder
```
mvn install
```

## To Run Project
```
mvn spring-boot:run
```
or urilize the builti in automatic setup of injilij to install and run the project
