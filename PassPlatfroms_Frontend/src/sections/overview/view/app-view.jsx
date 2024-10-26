import Container from '@mui/material/Container';
import Grid from '@mui/material/Unstable_Grid2';
import Typography from '@mui/material/Typography';
import AppCurrentVisits from '../app-current-visits';
import AppWidgetSummary from '../app-widget-summary';

import UserProfile from "../../../components/auth/UserInfo";
import React, {useEffect, useState} from "react";
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import ImageGallery from "react-image-gallery";
import {Backdrop, CircularProgress} from "@mui/material";


// ----------------------------------------------------------------------


export default function AppView()
{
    const [loadingShow, setLoadingShow] = useState(false);

    const [userName, setUserName] = useState("");

    async function getUserName()
    {
        await UserProfile.getUserName().then((data) =>
        {
            const words = data.trim().split(/\s+/);
            const firstName = words[0];
            const lastName = words[words.length - 1];
            setUserName(firstName + " " + lastName);
        })
    }

    const [activeUsers, setActiveUsers] = useState(0);
    const [leaders, setLeaders] = useState(0);
    const [sessions, setSessions] = useState(0);
    const [hours, setHours] = useState(0);

    const [ict, setICT] = useState(0);
    const [bus, setBus] = useState(0);
    const [eng, setEng] = useState(0);
    const [webM, setWebM] = useState(0);
    const [visD, setVisD] = useState(0);
    const [logt, setLogt] = useState(0);
    const [other, setOther] = useState(0);

    function parsetats(statsDTO)
    {
        if (Object.keys(statsDTO).length !== 0)
        {
            setActiveUsers(statsDTO.studentno);
            setLeaders(statsDTO.leaderno);
            setSessions(statsDTO.bkdsessionsno);
            setHours(statsDTO.hoursno);

            setICT(statsDTO.ict);
            setBus(statsDTO.business);
            setEng(statsDTO.engineering);
            setWebM(statsDTO.webmedia);
            setVisD(statsDTO.visualdesign);
            setLogt(statsDTO.logistics);
            setOther(statsDTO.other);
        }
    }

    async function getStats()
    {
        try
        {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/Stat`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        return response.json()
                    }
                    else
                    {
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        parsetats(data.transObject)
                    }
                });

        }
        catch (error)
        {
            console.log(error);
        }
        finally
        {
            setLoadingShow(false);
        }
    }

    useEffect(() =>
    {
        getUserName();
        getStats();

    }, []);

    const images = [
        {
            original: "/assets/images/Business 1.png",
        },
        {
            original: "/assets/images/Business 2.png",
        },
        {
            original: "/assets/images/Creative Media.png",
        },
        {
            original: "/assets/images/Engineering 1.png",
        },
        {
            original: "/assets/images/Engineering 2.png",
        },
        {
            original: "/assets/images/Foundation.png",
        },
        {
            original: "/assets/images/ICT 1.png",
        },
        {
            original: "/assets/images/ICT 2.png",
        },
        {
            original: "/assets/images/ICT 3.png",
        },
        {
            original: "/assets/images/Logistics 1.png",
        },
        {
            original: "/assets/images/Logistics 2.png",
        },
    ];

    return (

        <Container maxWidth="xl">

            {/* loading */}
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={loadingShow}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>

            <Typography variant="h4" sx={{mb: 5}}>
                Hi, Welcome Back {userName} 👋🏼
            </Typography>


            <Grid container spacing={3}>
                <Grid xs={12} sm={6} md={3}>
                    <AppWidgetSummary
                        title="Active Users"
                        total={activeUsers}
                        color="primary"
                        icon={<img alt="icon" src="/assets/icons/glass/users.png"/>}
                    />
                </Grid>

                <Grid xs={12} sm={6} md={3}>
                    <AppWidgetSummary
                        title="Leaders No."
                        total={leaders}
                        color="info"
                        icon={<img alt="icon" src="/assets/icons/glass/leadrs.png"/>}
                    />
                </Grid>

                <Grid xs={12} sm={6} md={3}>
                    <AppWidgetSummary
                        title="Sessions No."
                        total={sessions}
                        color="warning"
                        icon={<img alt="icon" src="/assets/icons/glass/bookings.png"/>}
                    />
                </Grid>

                <Grid xs={12} sm={6} md={3}>
                    <AppWidgetSummary
                        title="Hours Taught"
                        total={hours}
                        color="error"
                        icon={<img alt="icon" src="/assets/icons/glass/hours.png"/>}
                    />
                </Grid>

                <Grid xs={12} md={6} lg={8}>
                    <Card>
                        <Box>
                            <ImageGallery items={images} autoPlay={true} showPlayButton={false} slideInterval={10000} showFullscreenButton={false}/>
                        </Box>
                    </Card>
                </Grid>

                <Grid xs={12} md={6} lg={4}>
                    <AppCurrentVisits
                        title="Bookings %"
                        chart={{
                            series: [
                                {label: 'ICT', value: ict},
                                {label: 'Business', value: bus},
                                {label: 'Engineering', value: eng},
                                {label: 'Web Media', value: webM},
                                {label: 'Visual Design', value: visD},
                                {label: 'Logistics', value: logt},
                                {label: 'Others', value: other},
                            ],
                        }}
                    />
                </Grid>

            </Grid>
        </Container>
    );
}
