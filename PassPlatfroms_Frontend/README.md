# Setting up & starting Frontend

* Complete all previous steps (azure, database, backend)

## Basic Setup
* replace `URL_CHANGE_PLACEHOLDER` with the Backend URL, if hosted, the URL must be SSL encrypted (HTTPS enabled)
* helps to use `ctrl + shift + f` in IntelliJ and replace all occurrences of the placeholder

## Auth Setup
inside the directory `src/components/auth/authConfig.js`, replace `CLIENT_ID` on line 15 with the client id from the app registration of Azure 
and also replace `TENANT_ID` on line 16 with the proper Tenant ID of the Active Directory


## To install dependencies, run the following command in the root directory of the FrontEnd Folder
```
npm install -force
```

## To Run Project
```
npm start
```
