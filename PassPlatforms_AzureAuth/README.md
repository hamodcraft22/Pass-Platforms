# Setting up Microsoft Entra ID (Azure AD)

* Create an account (or utilize an existing account) which can manage directories (is not a directory account)
* Select the Entra ID menu
![image](https://github.com/hamodcraft22/Pass-Platforms/assets/67144555/7a985137-fc7b-4b95-8fed-fa154d1204c4)
* Create a new active directory
![image](https://github.com/hamodcraft22/Pass-Platforms/assets/67144555/72be8d71-ac75-47fc-b75e-9c83861ff33a)
* Register a new application 
![image](https://github.com/hamodcraft22/Pass-Platforms/assets/67144555/cf98243c-7752-4220-9d33-aa95449545b5)
* setup redirect URL 
![image](https://github.com/hamodcraft22/Pass-Platforms/assets/67144555/4d8ce38f-738e-498e-8fea-e8e8c6f5c59d)
this url should link to the frontend URLs, if hosted it has to be SSL encrypted (HTTPS enabled)
```
http://localhost:3000
```
* keep track of the following IDs:
![image](https://github.com/hamodcraft22/Pass-Platforms/assets/67144555/2d58b5eb-3aa3-45b9-af2d-0e02e966b070)

* setup client secret 
https://o365reports.com/2023/07/20/an-overview-of-client-secret-management-in-azure-ad/
