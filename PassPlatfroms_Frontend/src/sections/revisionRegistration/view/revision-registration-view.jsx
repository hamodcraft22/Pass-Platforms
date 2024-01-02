import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import WeekCalendar from 'react-week-calendar';
import moment from "moment";
import Toolbar from "@mui/material/Toolbar";
import MultiSelect from "../MultiSelect";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, Backdrop, CircularProgress, FormHelperText, Snackbar, TextField} from "@mui/material";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import InputAdornment from "@mui/material/InputAdornment";
import {AccountCircle} from "@mui/icons-material";
import UserProfile from "../../../components/auth/UserInfo";
import bookingSlot from "../../newBooking/bookingSlot";
import {useNavigate} from "react-router-dom";


// ----------------------------------------------------------------------

export default function NewRegistrationPage()
{

    // this page is open for everyone but submitting is only allowed for students / leaders

    const [loadingShow, setLoadingShow] = useState(false);

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    const [progColor, setProgColor] = useState("primary");
    useEffect(() =>
    {
        (setProgPercent(((shownSection - 1) / 3) * 100))
    }, [shownSection]);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) =>
    {
        if (reason === 'clickaway')
        {
            return;
        }
        setErrorShow(false);
    };

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    const [schools, setSchools] = useState([]);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState();

    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);
    }

    // get schools and courses
    async function getRevSchools()
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/school/revisions`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("No Schools Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        setSchools(data.transObject);
                    }
                })
                .then(() =>
                {
                    setLoadingShow(false)
                });
        }
        catch (error)
        {
            console.log(error);
            setLoadingShow(false);
        }
    }

    function handleSelectedSchool(school)
    {
        setLoadingShow(true);
        setSelectedSchool(school);
        setCourses(school.courses);
        setLoadingShow(false);
    }


    useEffect(() =>
    {
        getRevSchools()
    }, [])


    // time slots elements

    const [bookingStartDate, setBookingStartDate] = useState(moment().weekday(0));

    const [recivedBookingsDto, setRecivedBookingDto] = useState([]);

    // slots to be shown
    const [selectedIntervals, setSelectedIntervals] = useState([]);

    const [leaders, setLeaders] = useState([]);

    // selcted leaders + their slots
    const [selectedLeaders, setSelectedLeaders] = useState([]);

    async function getAvlbRevisions()
    {
        try
        {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/revision/course/${selectedCourse.courseid}?weekStart=${bookingStartDate.format("MM/DD/YYYY")}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        return response.json()
                    }
                    else
                    {
                        setErrorMsg("No Slots found, try another week");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        setRecivedBookingDto(data.transObject);
                    }
                })
                .then(() =>
                {
                    setLoadingShow(false);
                })

        }
        catch (error)
        {
            console.log(error);
            setLoadingShow(false);
        }
    }

    function parseRevisions()
    {
        setLoadingShow(true);
        setLeaders([]);

        const pastelColors = [
            '#ff9496',
            '#f494ff',
            '#9496ff',
            '#94d6ff',
            '#94ffcf',
            '#94ff96',
            '#f8ff94',
            '#ffb994',
            '#793131',
            '#794731',
            '#797531',
            '#317932',
            '#317960',
            '#315b79',
            '#333179',
            '#793173',
            '#180a0a',
            '#0a180b',
            '#0d2820',
            '#3f0a27',
        ];

        let parsedLeaders = [];

        recivedBookingsDto.forEach((leader, index) =>
        {
            const leaderColor = pastelColors[index];

            const leaderID = leader.leaderID;
            const leaderName = leader.leaderName;

            let leaderSlots = [];

            leader.revisions.forEach((revision) =>
            {
                const bookingid = revision.bookingid;
                const online = revision.isonline;

                const startTime = moment(`${revision.bookingDate} ${moment(revision.starttime).format('HH:mm')}`);
                const endTime = moment(`${revision.bookingDate} ${moment(revision.endtime).format('HH:mm')}`);

                if (startTime > moment())
                {
                    leaderSlots.push({"uid": bookingid, "start": startTime, "end": endTime, "online": online, "color": leaderColor, "leaderName": leaderName});
                }
            });

            if (leaderSlots.length !== 0)
            {
                parsedLeaders.push({"leaderID": leaderID, "leaderName": leaderName, "revisions": leaderSlots, "color": leaderColor});
            }
        });

        if (parsedLeaders.length !== 0)
        {
            setLeaders(parsedLeaders);
        }
        else
        {
            setErrorMsg("No Revision Found, try another week");
            setErrorShow(true);
        }
        setLoadingShow(false);
    }

    function handleSlots()
    {
        setLoadingShow(true);
        let allSlots = [];

        selectedLeaders.forEach((leader) =>
        {
            leader.revisions.forEach((slot) =>
            {
                allSlots.push(slot);
            })
        })

        setSelectedIntervals(allSlots);
        setLoadingShow(false);
    }

    useEffect(() =>
    {
        if (shownSection === 2)
        {
            getAvlbRevisions()
        }
    }, [shownSection]);

    // get upoan date change
    useEffect(() =>
    {
        if (shownSection === 2)
        {
            getAvlbRevisions()
        }
    }, [bookingStartDate]);


    useEffect(() =>
    {
        if (Object.keys(recivedBookingsDto).length !== 0)
        {
            parseRevisions()
        }
    }, [recivedBookingsDto])

    useEffect(() =>
    {
        if (selectedLeaders.length !== 0)
        {
            handleSlots()
        }
    }, [selectedLeaders]);


    // slot confirmation elemnts
    const [selctedSlot, setSelctedSlot] = useState([]);
    const [slotConfirmShow, setSlotConfirmShow] = useState(false);
    const [slotToConfirm, setSlotToConfirm] = useState([]);
    const handleSlotConfirmClose = () =>
    {
        setSlotConfirmShow(false);
        setSlotToConfirm([]);
    };
    const handleSlotConfirm = () =>
    {
        setSlotConfirmShow(false);
        setSelctedSlot(slotToConfirm);
        setSlotToConfirm(null);
    };
    const handleSlotSelect = (slot) =>
    {
        setSlotToConfirm(slot);
    };
    useEffect(() =>
    {
        if (slotToConfirm !== null && slotToConfirm !== undefined && Object.keys(slotToConfirm).length !== 0)
        {
            setSlotConfirmShow(true)
        }
    }, [slotToConfirm]);
    useEffect(() =>
    {
        if (selctedSlot !== null && selctedSlot !== undefined && Object.keys(selctedSlot).length !== 0)
        {
            nextSection()
        }
    }, [selctedSlot]);

    const [showComplete, setShowComplete] = useState(false);

    async function submitBooking()
    {
        let isok = false;
        let isBad = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/revision/${selctedSlot.uid}/member`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        setProgPercent(100);
                        return response.json();
                    }
                    else if (response.status === 400)
                    {
                        isBad = true;
                        return response.json();
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
                .then((data) =>
                {
                    setLoadingShow(false);
                    if (isok)
                    {
                        // it is fine, go on
                        setShowComplete(true);
                    }
                    else if (isBad)
                    {
                        // errors for booking
                        let errorString = "";
                        data.error.forEach((errorItem) =>
                        {
                            if (errorString === "")
                            {
                                errorString = errorItem
                            }
                            else
                            {
                                errorString = errorItem + "\n" + errorString
                            }
                        });
                        setErrorMsg(errorString);
                        setErrorShow(true);
                        console.log(data);
                    }
                    else
                    {
                        console.log(data);
                    }
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


    function nextSection()
    {
        if (shownSection === 1)
        {
            if (selectedSchool !== null && selectedCourse !== null && selectedSchool !== undefined && selectedCourse !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Select a School and a Course Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 2)
        {
            if (selectedSchool !== null && selectedCourse !== null && selctedSlot !== null && selectedSchool !== undefined && selectedCourse !== undefined && selctedSlot !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0 && Object.keys(selctedSlot).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Select a Slot Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 3)
        {
            if (userRole === 'student' || userRole === 'leader')
            {
                submitBooking();
            }
            else
            {
                setErrorMsg("you are not allowed to book");
                setErrorShow(true);
            }
        }

    }

    function prevSection()
    {
        setShownSection((shownSection) - 1);
    }

    let navigate = useNavigate();
    const goToRevision = () =>
    {
        if (showComplete !== false)
        {
            let path = `/viewRevision?revisionID=${selctedSlot.uid}`;
            navigate(path);
        }
    }

    const CustomPaper = (props) =>
    {
        return <Paper elevation={8} {...props} />;
    };

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
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* complete dialog */}
            <Dialog
                open={showComplete}
            >
                <DialogTitle>
                    {"Success!!"}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        you are registerd!!, please click below to view it.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    {
                        <Button onClick={goToRevision} autoFocus> Go to Revision. </Button>
                    }
                </DialogActions>
            </Dialog>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Revision Registration</Typography>

                <div>
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                            disabled={shownSection === 1} onClick={prevSection} sx={{m: 1}}>
                        Back
                    </Button>
                    <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                            onClick={nextSection}>
                        {shownSection === 3 ? (
                            <>Submit</>
                        ) : (
                            <>Next</>
                        )}
                    </Button>
                </div>


            </Stack>

            <Box sx={{width: '100%', mb: 2}}>
                <LinearProgress variant="determinate" value={progPercent} style={{borderRadius: 5, height: 10}} color={progColor}/>
            </Box>

            {/* elements */}
            {/*select course and school card */}
            {
                shownSection === 1 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Select School:</Typography>
                        <Autocomplete
                            PaperComponent={CustomPaper}
                            options={schools}
                            value={selectedSchool}
                            onChange={(event, newValue) =>
                            {
                                handleSelectedSchool(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="School"/>}
                            getOptionLabel={(option) => option.schoolname}
                            renderOption={(props, option) =>
                            {
                                return (
                                    <li {...props}>
                                        {option.schoolname}
                                    </li>
                                );
                            }}
                        />
                        <FormHelperText>Select a School to show all of the courses available.</FormHelperText>


                        <Typography variant="h6" sx={{mt: 3}}>Select Course:</Typography>
                        <Autocomplete
                            PaperComponent={CustomPaper}
                            options={courses}
                            value={selectedCourse}
                            onChange={(event, newValue) =>
                            {
                                setSelectedCourse(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="Course"/>}
                            getOptionLabel={(option) => option.courseid + " " + option.coursename}
                            renderOption={(props, option) =>
                            {
                                return (
                                    <li {...props}>
                                        {option.courseid + " " + option.coursename}
                                    </li>
                                );
                            }}
                        />
                        <FormHelperText>After Selecting the course Please Proceed to the next step.</FormHelperText>
                    </div>
                </Card>
            }

            {/*select slot card */}
            {
                shownSection === 2 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6" sx={{mb: 1}}>Select Slot:</Typography>
                        <Toolbar
                            sx={{
                                minHeight: 96,
                                marginBottom: '10px',
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'space-between',
                                p: (theme) => theme.spacing(0, 1, 0, 3)
                            }}
                        >
                            {/* add list of leaders here*/}
                            <MultiSelect
                                items={leaders}
                                label="Leaders"
                                selectAllLabel="All"
                                leaders={(items) =>
                                {
                                    setSelectedLeaders(items)
                                }}
                            />

                            <Box sx={{mt: 1, width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                                        onClick={() =>
                                        {
                                            setBookingStartDate(bookingStartDate.clone().add(-7, 'day'))
                                        }}>
                                    Prev Week
                                </Button>

                                <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                                        onClick={() =>
                                        {
                                            setBookingStartDate(bookingStartDate.clone().add(7, 'day'))
                                        }}>
                                    Next Week
                                </Button>
                            </Box>

                        </Toolbar>

                        <WeekCalendar
                            dayFormat={"dd DD/MM"}
                            firstDay={bookingStartDate}
                            numberOfDays={7}
                            startTime={moment({h: 8, m: 0})}
                            endTime={moment({h: 22, m: 0})}
                            scaleUnit={30}
                            cellHeight={50}
                            scaleFormat={"h:mm a"}
                            useModal={false}
                            selectedIntervals={selectedIntervals}
                            onEventClick={handleSlotSelect}
                            eventComponent={bookingSlot}
                            eventSpacing={0}
                        />
                    </div>
                </Card>
            }

            {/* confirm slot selection */}
            {
                <Dialog
                    open={slotConfirmShow}
                    onClose={handleSlotConfirmClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Please Confirm Slot Selection"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            {
                                slotToConfirm !== null && slotToConfirm !== undefined && Object.keys(slotToConfirm).length !== 0 ?
                                    (<>Your Session will be
                                        on <b>{moment(slotToConfirm.start).format("DD/MM/YYYY")}</b> - <b>{moment(slotToConfirm.start).format("dddd")}</b>,
                                        and will be conducted
                                        by <b>{slotToConfirm.leader}</b> from <b>{moment(slotToConfirm.start).format("hh:mma")}</b> till <b>{moment(slotToConfirm.end).format("hh:mma")}</b>, {slotToConfirm.online ? (
                                            <b>Online</b>) : (<b>Physically</b>)}</>) : (
                                        <CircularProgress color="inherit"/>)
                            }
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleSlotConfirmClose}>Cancel</Button>
                        <Button onClick={handleSlotConfirm} autoFocus>
                            Confirm
                        </Button>
                    </DialogActions>
                </Dialog>
            }


            {/* overview and submit card */}
            {
                shownSection === 3 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Overview:</Typography>
                        <FormHelperText>Please validate your Registration before submitting.</FormHelperText>

                        <TextField label="School" variant="standard" fullWidth sx={{mb: 1, mt: 3}}
                                   InputProps={{readOnly: true}} defaultValue={selectedSchool.schoolname}/>
                        <TextField label="Course" variant="standard" fullWidth sx={{mb: 1, mt: 1}}
                                   InputProps={{readOnly: true}} defaultValue={selectedCourse.courseid + " " + selectedCourse.coursename}/>

                        <TextField label="Date" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}}
                                   defaultValue={moment(selctedSlot.start).format("DD/MM/YYYY")}/>

                        <TextField label="From" variant="standard" sx={{mb: 1, mt: 1, mr: 1}} InputProps={{readOnly: true}}
                                   defaultValue={moment(selctedSlot.start).format("hh:mma")}/>
                        <TextField label="To" variant="standard" sx={{mb: 1, mt: 1,}} InputProps={{readOnly: true}}
                                   defaultValue={moment(selctedSlot.end).format("hh:mma")}/>

                        <TextField label="Leader" variant="standard" fullWidth sx={{mb: 2, mt: 2}} InputProps={{
                            startAdornment: (<InputAdornment position="start"><AccountCircle/></InputAdornment>),
                            readOnly: true
                        }} defaultValue={selctedSlot.leaderName}/>


                    </div>
                </Card>
            }
        </Container>
    );
}
