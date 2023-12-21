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
import Box from "@mui/material/Box";
import LoginIcon from '@mui/icons-material/Login';
import Paper from "@mui/material/Paper";


// ----------------------------------------------------------------------


const MainContent = () => {



    const { instance } = useMsal();
    const activeAccount = instance.getActiveAccount();

    const [allowLoad, setAllowLoad] = useState(false)

    async function getToken() {
        const accessTokenRequest = {
            scopes: ['87cabde3-68c8-4d9a-ba69-e0b05316e1f2/.default'],
            account: activeAccount,
        };

        if (activeAccount)
        {
            await instance.initialize()
                .then(() => {return instance.acquireTokenSilent(accessTokenRequest)})
                .then((token) => {return `Bearer ${token.accessToken}`})
                .then((barerToken) => {
                    UserProfile.setUserID(activeAccount.idTokenClaims.emails);
                    UserProfile.setUserName(activeAccount.idTokenClaims.name);
                    UserProfile.setUserRole("student");
                    UserProfile.setAuthToken(barerToken);
                    UserProfile.setExpTime(activeAccount.idTokenClaims.exp);
                })
                .then(() => {setAllowLoad(true)});
        }
    }

    useEffect(() => {getToken()}, [activeAccount])


    const handleLoginRedirect = () => {
        instance.loginRedirect(loginRequest).catch((error) => console.log(error));
    };

    useScrollToTop();

    return (
        <>
            <AuthenticatedTemplate>
                {allowLoad ? (<Router/>) : <Backdrop sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }} open={true}><CircularProgress color="inherit" /></Backdrop>}
            </AuthenticatedTemplate>
            <UnauthenticatedTemplate>
                <Box sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        height: "100vh",
                        "&::before":
                            {
                                content: '""',
                                position: "fixed",
                                top: 0,
                                left: 0,
                                bottom: 0,
                                right: 0,
                                backgroundImage: "url('/assets/login_backdrop.png')",
                                backgroundSize: "cover",
                                backgroundPosition: "center",
                                backgroundRepeat: "no-repeat",
                                filter: "blur(5px)",
                                margin: "-10px"
                            }
                    }}>

                    <Paper elevation={24} sx={{p:2, zIndex:5, display: "flex", alignItems: "center", justifyContent: "center", flexDirection: "column"}}>

                        <h1>Pass Platforms</h1>
                        <p>Please login using your university account 👇🏼</p>

                        <Button onClick={handleLoginRedirect} size="large" variant={"contained"} endIcon={<LoginIcon/>}>Login</Button>

                    </Paper>


                </Box>
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
