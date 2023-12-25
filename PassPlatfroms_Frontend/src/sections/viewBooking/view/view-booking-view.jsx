import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {Alert, Backdrop, CardActions, CardContent, CircularProgress, Divider, FormHelperText, ListItem, ListItemIcon, Snackbar, TextField} from "@mui/material";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Unstable_Grid2";
import EmailIcon from '@mui/icons-material/Email';
import Link from "@mui/material/Link";
import SendRoundedIcon from '@mui/icons-material/SendRounded';
import List from "@mui/material/List";
import {AccountCircle} from "@mui/icons-material";
import ListItemText from "@mui/material/ListItemText";
import {useNavigate} from "react-router-dom";
import UserProfile from "../../../components/auth/UserInfo";
import moment from "moment";


// ----------------------------------------------------------------------

export default function ViewBookingPage() {

    const [loadingShow, setLoadingShow] = useState(false);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };

    const queryParameters = new URLSearchParams(window.location.search);
    const bookingIDParm = queryParameters.get("bookingID");

    const [bookingInfo, setBookingInfo] = useState(null);

    const [groupMembers, setGroupMembers] = useState([]);

    const [bookingLeader, setBookingLeader] = useState("");
    const [bookingStudent, setBookingStudent] = useState("");

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    function parseBooking(bookingDto)
    {
        setBookingInfo(bookingDto);
        setGroupMembers(bookingDto.bookingMembers);
        setBookingLeader(bookingDto.slot.leader.userid);
        setBookingStudent(bookingDto.student.userid);

        console.log(bookingDto);
    }

    async function getBooking()
    {
        try
        {
            let isok = false;
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/booking/${bookingIDParm}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        isok = true;
                        return response.json()
                    }
                    else if (response.status === 204)
                    {
                        setErrorMsg("the booking was not found");
                        setErrorShow(true);
                    }
                    else if (response.status === 401)
                    {
                        setErrorMsg("you do not have access to this booking");
                        setErrorShow(true);
                    }
                    else
                    {
                        console.log(response);
                        setErrorMsg("Unknown error, check console");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    if (isok)
                    {
                        parseBooking(data.transObject);
                    }
                })
                .then(() => {setLoadingShow(false);})
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("Unknown error, check console");
            setErrorShow(true);
            console.log(error);
        }
    }

    async function getUserInfo()
    {
        const userID = await UserProfile.getUserID();
        const userRole = await UserProfile.getUserRole();

        setUserID(userID);
        setUserRole(userRole);

        getBooking();
    }

    useEffect(() => {if(bookingIDParm !== null && bookingIDParm !== undefined && bookingIDParm !== ""){getUserInfo()}}, []);

    const [userNote, setUserNote] = useState("");

    async function submitUserNote()
    {
        try {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: userNote};

            await fetch(`http://localhost:8080/api/bookingnote/${bookingInfo.bookingid}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        window.location.reload();
                    }
                    else if (response.status === 401)
                    {
                        setErrorMsg("you are not allowed to do this action");
                        setErrorShow(true);
                    }
                    else if (response.status === 404)
                    {
                        setErrorMsg("the request was not found on the server, double check your connection");
                        setErrorShow(true);
                    }
                    else
                    {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then(() =>
                {
                    setLoadingShow(false);
                })

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    let navigate = useNavigate();
    const goToBookings = () => {
        let path = `/bookings`;
        navigate(path);
    }

    const goToHome = () => {
        let path = `/`;
        navigate(path);
    }


    return (
        <Container>

            {/* loading */}
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={loadingShow}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>


            {
                bookingIDParm !== null && bookingIDParm !== undefined && bookingIDParm !== "" && bookingInfo !== null &&
                <>
                    {/* top bar */}
                    <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                        <Typography variant="h4">Student - Booking</Typography>
                    </Stack>

                    {/* view booking elements */}

                    <Grid container spacing={3}>
                        {/* Left Card */}
                        <Grid xs={12} md={6} lg={4}>
                            <Card sx={{backgroundColor: '#f5f5f5'}}>
                                <CardContent>
                                    <Typography variant="h6">Booking Information</Typography>

                                    <TextField label="Student" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={bookingInfo.student.userid+" "+bookingInfo.student.userName}/>

                                    <TextField label="Leader" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={bookingInfo.slot.leader.userid+" "+bookingInfo.slot.leader.userName}/>

                                    <TextField label="Booked Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(bookingInfo.datebooked).format("hh:mm A | DD/MM/YYYY")}/>

                                    <TextField label="Booking Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={bookingInfo.bookingDate}/>

                                    <TextField label="Scheduled Time" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={`${moment(bookingInfo.slot.starttime).format("hh:mm A")} - ${moment(bookingInfo.slot.endtime).format("hh:mm A")}`}/>

                                    {
                                        bookingInfo.starttime &&
                                        <TextField label="Conducted Time" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={`${moment(bookingInfo.starttime).format("hh:mm A")} - ${moment(bookingInfo.endtime).format("hh:mm A")}`}/>
                                    }

                                    <TextField label="Booked Status" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={bookingInfo.bookingStatus.statusname}/>

                                    <TextField label="Booking Note" variant="standard" fullWidth sx={{mt: 2}} multiline InputProps={{readOnly: true}} defaultValue={bookingInfo.note}/>

                                    {/* show icon if online */}

                                    {/* show icon if group */}

                                    {/* group members - only when group */}
                                    {/* loop of members - if added - maybe add name get? */}
                                    {
                                        groupMembers !== null && groupMembers !== undefined && Object.keys(groupMembers).length !== 0 ?
                                            (
                                                <>
                                                    <FormHelperText>Members</FormHelperText>
                                                    <List dense>
                                                        {
                                                            groupMembers && groupMembers.map((member) => (
                                                                <ListItem>
                                                                    <ListItemIcon><AccountCircle/></ListItemIcon>
                                                                    <ListItemText primary={`${member.student.userid} ${member.student.userName}`}/>
                                                                </ListItem>
                                                            ))
                                                        }
                                                    </List>
                                                </>
                                            ) : (<></>)

                                    }


                                    {/* if student */}
                                    {
                                        userID === bookingStudent &&
                                        <>
                                            <Button variant="contained" startIcon={<EmailIcon/>} href={`mailto:${bookingLeader}@student.polytechnic.bh`} sx={{m: 1}}>
                                                Email Leader
                                            </Button>


                                            {/* extra work */}
                                            {/*{*/}
                                            {/*    bookingInfo.bookingStatus.statusid === 'A' &&*/}
                                            {/*    <Button variant="contained" color={"secondary"} sx={{m: 1}}>*/}
                                            {/*        Add Members*/}
                                            {/*    </Button>*/}
                                            {/*}*/}

                                        </>
                                    }

                                    {
                                        userID === bookingLeader &&
                                        <Button variant="contained" startIcon={<EmailIcon/>} href={`mailto:${bookingStudent}@student.polytechnic.bh`} sx={{m: 1}}>
                                            Email Student
                                        </Button>
                                    }




                                </CardContent>
                            </Card>
                        </Grid>

                        {/* Right Card */}
                        <Grid xs={12} md={6} lg={8}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6" sx={{mb: 2}}>Booking Notes</Typography>

                                    {/* loop to display all notes */}
                                    {
                                        bookingInfo &&
                                        <>
                                            {
                                                bookingInfo.bookingNotes && bookingInfo.bookingNotes.map((note) => {

                                                    const bkColor = note.user.role.rolename === 'student' ? '#fafff8' : '#fafff8';

                                                    return (
                                                        <Card sx={{mb: 2, backgroundColor: bkColor}}>
                                                            <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                                                <Box sx={{minWidth: 240, flexGrow: 1}}>
                                                                    <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                                        {note.user.userName}
                                                                    </Link>

                                                                    <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                                                        {moment(note.datetime).format("hh:mm A | DD/MM/YYYY")}
                                                                    </Typography>

                                                                    <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                                                        {note.notebody}
                                                                    </Typography>
                                                                </Box>

                                                                <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                                                    {note.user.role.rolename}
                                                                </Typography>
                                                            </Stack>
                                                        </Card>
                                                    )
                                                })
                                            }
                                        </>
                                    }

                                    <Divider/>

                                    <Typography variant="h6" sx={{mt: 2, mb: 2}}>Post a Note:</Typography>
                                    <TextField fullWidth label="Note" variant="outlined" multiline minRows={2} value={userNote} onChange={(event) => {setUserNote(event.target.value)}}/>
                                    <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                                        <Button variant="contained" endIcon={<SendRoundedIcon/>} sx={{mt: 2}} onClick={() => {submitUserNote()}} disabled={userNote === null || userNote === "" || userNote === undefined}>
                                            Submit
                                        </Button>
                                    </Box>
                                </CardContent>
                            </Card>
                        </Grid>
                    </Grid>
                </>
            }

            {/* if there is no booking id */}
            {
                (bookingIDParm === null || bookingIDParm === undefined || bookingIDParm === "") &&
                <Box width={"100%"} height={"100%"} display="flex" justifyContent="center" alignItems="center">
                    <Card sx={{maxWidth: 345}}>
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                Uh oh!
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                it seems like you have gotten here somehow with selecting a Booking! please select one from the bookings list
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button size="small" onClick={goToHome}>Home</Button>
                            <Button size="small" onClick={goToBookings}>Bookings</Button>
                        </CardActions>
                    </Card>
                </Box>
            }

        </Container>
    );
}
