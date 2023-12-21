import './global.css';

import { MsalProvider, AuthenticatedTemplate, useMsal, UnauthenticatedTemplate } from '@azure/msal-react';

import {useEffect, useState} from "react";


import {useScrollToTop} from './hooks/use-scroll-to-top';

import Router from './routes/sections';
import ThemeProvider from './theme';
import { loginRequest } from './components/auth/authConfig';
import Button from "@mui/material/Button";
import UserProfile from "./components/auth/UserInfo";
import {Backdrop, CircularProgress} from "@mui/material";


// ----------------------------------------------------------------------


const MainContent = () => {
    /**
     * useMsal is a hook that returns the PublicClientApplication instance.
     * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-react/docs/hooks.md
     */
    const { instance } = useMsal();
    const activeAccount = instance.getActiveAccount();
    const [authToken, setAuthToken] = useState("");

    const [allowLoad, setAllowLoad] = useState(false)

    async function getToken() {
        const accessTokenRequest = {
            scopes: ['87cabde3-68c8-4d9a-ba69-e0b05316e1f2/.default'],
            account: activeAccount,
        };

        if (activeAccount)
        {
            await instance.initialize();
            const accessTokenResponse = await instance.acquireTokenSilent(accessTokenRequest);
            return `Bearer ${accessTokenResponse.accessToken}`;
        }
    }

    async function cacheInfo()
    {
        if (activeAccount)
        {
            await instance.initialize();

            await UserProfile.setUserID(activeAccount.idTokenClaims.emails);
            await UserProfile.setUserName(activeAccount.idTokenClaims.name);
            await UserProfile.setUserRole("student");
            await UserProfile.setAuthToken(authToken);
            await UserProfile.setExpTime(activeAccount.idTokenClaims.exp);
        }

    }

    useEffect(() => {getToken().then(async (token) => setAuthToken(token))}, [activeAccount])

    useEffect(() => {if(authToken!==null && authToken !== "" && activeAccount){cacheInfo().then(setAllowLoad(true))}}, [authToken])

    const handleLoginRedirect = () => {
        instance.loginRedirect(loginRequest).catch((error) => console.log(error));
    };

    useScrollToTop();


    /**
     * Most applications will need to conditionally render certain components based on whether a user is signed in or not.
     * msal-react provides 2 easy ways to do this. AuthenticatedTemplate and UnauthenticatedTemplate components will
     * only render their children if a user is authenticated or unauthenticated, respectively. For more, visit:
     * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-react/docs/getting-started.md
     */
    return (
        <>
            <AuthenticatedTemplate>
                {allowLoad ? (<Router/>) : <Backdrop sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }} open={true}><CircularProgress color="inherit" /></Backdrop>}
            </AuthenticatedTemplate>
            <UnauthenticatedTemplate>
                <Button onClick={handleLoginRedirect} >Login</Button>
            </UnauthenticatedTemplate>
        </>
    );
};

const App = ({ instance }) => {
    return (
        <MsalProvider instance={instance}>
            <ThemeProvider>
                <MainContent />
            </ThemeProvider>
        </MsalProvider>
    );
};

export default App;
