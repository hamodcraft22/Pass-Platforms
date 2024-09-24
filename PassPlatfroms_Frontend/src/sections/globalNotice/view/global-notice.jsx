import Container from '@mui/material/Container';
import Grid from '@mui/material/Unstable_Grid2';
import Typography from '@mui/material/Typography';
import Card from "@mui/material/Card";
import React, {useEffect, useState} from "react";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import GradingRoundedIcon from '@mui/icons-material/GradingRounded';
import PendingActionsRoundedIcon from '@mui/icons-material/PendingActionsRounded';
import {TextField} from "@mui/material";
import UserProfile from "../../../components/auth/UserInfo";


// ----------------------------------------------------------------------


export default function GlobalNotice()
{
    const [globalMsg, setGlobalMsg] = useState("");
    const [userRole, setUserRole] = useState("");

    async function getUserInfo()
    {
        let userRole = await UserProfile.getUserRole();
        await setUserRole(userRole);
        getGloablNote();
    }

    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        getUserInfo()
    }, []);

    // get the global message
    async function getGloablNote()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/notification/global`, requestOptions)
                .then(response => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                }).then(data => {
                    if (data !== null && data !== "")
                    {
                        setGlobalMsg(data);
                    }
                    else
                    {
                        setGlobalMsg("");
                    }
                })
        }
        catch (e)
        {
            console.log(e);
        }
    }

    async function updateGlobalNote()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', 'Authorization': token}, body: JSON.stringify(globalMsg)};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/notification/global`, requestOptions)
                .then(response => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                }).then(data => {
                    if (data !== null && data !== "")
                    {
                        alert("updated");
                        setGlobalMsg(data);
                    }
                    else
                    {
                        alert("an error happened");
                    }
                })
        }
        catch (e)
        {
            console.log(e);
        }
    }

    async function deleteGlobalNote()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "DELETE", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/notification/global`, requestOptions)
                .then(response =>
                    {
                        if (response.status === 200)
                        {
                            alert("OK deleted");
                            setGlobalMsg("");
                        }
                        else if (response.status === 204)
                        {
                            alert("mafe anything to delete");
                        }
                        else
                        {
                            alert("fee error");
                        }
                    }
                )
        }
        catch (e)
        {
            console.log(e);
        }
    }



    function submitUpdate()
    {
        if (globalMsg !== null && globalMsg !== undefined && globalMsg !== "")
        {
            updateGlobalNote();
        }
        else
        {
            alert("UPDATE WHAT CHILE?!");
        }
    }


    return (

        userRole !== null && userRole !== undefined && userRole === "admin" ? (
            <Container maxWidth="xl">


                {/* top bar */}
                <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                    <Typography variant="h4" sx={{mb: 5}}>
                        Global Notification
                    </Typography>

                    <div>
                        <Button variant="contained" color="warning" startIcon={<GradingRoundedIcon/>} sx={{m: 1}} onClick={submitUpdate}>
                            Update
                        </Button>

                        <Button variant="contained" color="error" startIcon={<PendingActionsRoundedIcon/>} sx={{m: 1}} onClick={deleteGlobalNote}>
                            Delete
                        </Button>
                    </div>


                </Stack>


                <Grid container spacing={3}>

                    <Grid xs={12} md={12} lg={12}>
                        <Card>
                            <div style={{padding: "15px"}}>
                                <TextField
                                    label="Global Message"
                                    placeholder="No Message"
                                    value={globalMsg}
                                    helperText="Insert the message here"
                                    onChange={(event) => {setGlobalMsg(event.target.value)}}
                                    fullWidth
                                />
                            </div>
                        </Card>
                    </Grid>

                </Grid>

            </Container>
        ) :
            (
                <p>Get the hell out of this page chile</p>
            )
    );
}
