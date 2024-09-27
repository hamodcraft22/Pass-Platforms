// src/utils/authUtils.js
import { PublicClientApplication } from '@azure/msal-browser';
import { msalConfig } from './authConfig';

const msalInstance = new PublicClientApplication(msalConfig);

export async function getActiveUserToken()
{
    const accounts = msalInstance.getAllAccounts();

    if (accounts.length > 0)
    {
        const request = {scopes: [`${process.env.REACT_APP_CLIENT_ID}/.default`], account: accounts[0]};

        try
        {
            const response = await msalInstance.initialize().then(() => {return msalInstance.acquireTokenSilent(request)});
            // console.log(response.accessToken);
            return response.accessToken;
        }
        catch (error)
        {
            console.error("Silent token acquisition failed, acquiring token using popup", error); // LOGIN AGAIN - TODO TEST
            try
            {
                const response = await msalInstance.acquireTokenPopup(request);
                return response.accessToken;
            }
            catch (popupError) {
                console.error("Token acquisition using popup failed", popupError);
                return null;
            }
        }
    }
    else
    {
        console.error("No active accounts found");
        return null;
    }
}
