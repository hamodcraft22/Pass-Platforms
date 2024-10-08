import './global.css';

import {AuthenticatedTemplate, MsalProvider, UnauthenticatedTemplate, useMsal} from '@azure/msal-react';

import React, {useEffect, useState} from "react";


import {useScrollToTop} from './hooks/use-scroll-to-top';

import Router from './routes/sections';
import ThemeProvider from './theme';
import {loginRequest} from './components/auth/authConfig';
import Button from "@mui/material/Button";
import UserProfile from "./components/auth/UserInfo";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";
import Box from "@mui/material/Box";
import LoginIcon from '@mui/icons-material/Login';
import Paper from "@mui/material/Paper";
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';

// ----------------------------------------------------------------------


const MainContent = () =>
{
    const {instance} = useMsal();
    const activeAccount = instance.getActiveAccount();

    const [allowLoad, setAllowLoad] = useState(false); // boolean to save loading state - after first logan
    const [sysDisable, setSysDisable] = useState(false); // boolean to save if the platform is disabled

    // acquire token and process user information (login only)
    async function getToken()
    {
        try
        {
            if (activeAccount)
            {
                console.log("getting user token");

                let token = await UserProfile.getAuthToken()
                    .then((barerToken) =>
                    {
                        UserProfile.setUserID(activeAccount.idTokenClaims.preferred_username.split('@')[0]);
                        UserProfile.setUserName(activeAccount.idTokenClaims.name);
                        return barerToken;
                    });

                const sysEnabled = await isDisabled();

                await logUser(token)
                    .then((role) =>
                    {
                        UserProfile.setUserRole(role).then(() =>
                        {
                            if ((role === 'admin' || role === 'manager') && !sysEnabled)
                            {
                                setAllowLoad(true);
                            }
                            else
                            {
                                if (sysEnabled)
                                {
                                    setAllowLoad(true);
                                }
                                else
                                {
                                    setSysDisable(true);
                                }
                            }
                        })
                    });
            }
        }
        catch (e)
        {
            instance.loginRedirect(loginRequest).catch((error) => console.log(error));
        }
    }

    // get user from database and retrieve role
    async function logUser(barerToken)
    {
        try
        {
            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': barerToken}};

            let role = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/user/userlog`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        return data.transObject.role.rolename
                    }
                    else
                    {
                        return null;
                    }
                });

            return role;

        }
        catch (error)
        {
            console.log(error)
        }
    }

    // fetch if the system is disabled
    async function isDisabled()
    {
        try
        {
            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json'}};

            return await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/metadata/disabled`, requestOptions)
                .then(response => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                })
                .then((data) =>
                {
                    return data;
                });

        }
        catch (error)
        {
            console.log(error);
        }
    }

    useEffect(() =>
    {
        if (activeAccount !== null && activeAccount !== undefined && !allowLoad)
        {
            getToken();
        }
    }, [activeAccount]);


    const handleLoginRedirect = () =>
    {
        instance.loginRedirect(loginRequest).catch((error) => console.log(error));
    };

    const handleLogoutRedirect = () =>
    {
        instance.logoutRedirect({account: activeAccount}).catch((error) => console.log(error));
    };

    // // auto logout after 10 minutes -- //comment out for demo
    // useEffect(() =>
    // {
    //     if (allowLoad)
    //     {
    //         const timer = setTimeout(() => {
    //             // Call your function here
    //             handleLogoutRedirect();
    //         }, 10 * 60 * 1000); // 10 minutes in milliseconds
    //
    //         return () => {
    //             clearTimeout(timer); // Clean up the timer on unmounting the component
    //         };
    //     }
    // }, [allowLoad]);

    useScrollToTop();

    const [showGlobalMsg, setShowGlobalMsg] = useState(false);
    const [globalMsg, setGlobalMsg] = useState("");

    // get global message
    async function getGlobalNote()
    {
        try
        {
            const token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/notification/global`, requestOptions)
                .then(response => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setShowGlobalMsg(false);
                        return null;
                    }
                }).then(data => {
                    if (data !== null)
                    {
                        setGlobalMsg(data);
                        setShowGlobalMsg(true);
                    }
                })
        }
        catch (e)
        {
            console.log(e);
            setShowGlobalMsg(false);
        }
    }

    // check the messages everytime smm changes
    useEffect(() => {
        if (activeAccount !== null && activeAccount !== undefined && allowLoad)
        {
            getGlobalNote();
        }
    }, [activeAccount]);

    // get global notification first time app loads
    useEffect(() => {if (allowLoad) {getGlobalNote()}}, [allowLoad])

    return (
        <>
            {/* global notification */}
            <Snackbar open={showGlobalMsg} anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}>
                <Alert severity="info" sx={{width: '100%', whiteSpace: 'pre-line', boxShadow: 3}} >
                    {globalMsg}
                </Alert>
            </Snackbar>

            <AuthenticatedTemplate>
                {allowLoad ? (<Router/>) :
                    <Backdrop sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}} open={true}>{sysDisable ? (<><p>The system is being setup and is disabled, check back later</p> <Button sx={{ml: 2}} onClick={handleLogoutRedirect}
                                                                                                                                                                                                       variant={"contained"} endIcon={
                        <LogoutRoundedIcon/>}>Logout</Button> </>) : (<CircularProgress color="inherit"/>)}</Backdrop>}
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

                    <Paper elevation={24} sx={{p: 2, zIndex: 5, display: "flex", alignItems: "center", justifyContent: "center", flexDirection: "column"}}>

                        <h1>Pass Platforms</h1>
                        <p>Please login using your university account 👇🏼</p>

                        <Button onClick={handleLoginRedirect} size="large" variant={"contained"} endIcon={<LoginIcon/>}>Login</Button>

                    </Paper>


                </Box>
            </UnauthenticatedTemplate>
        </>
    );
};

const App = ({instance, onLogin}) =>
{
    return (
        <MsalProvider instance={instance}>
            <ThemeProvider>
                <MainContent onLogin={onLogin}/>
            </ThemeProvider>
        </MsalProvider>
    );
};

export default App;
