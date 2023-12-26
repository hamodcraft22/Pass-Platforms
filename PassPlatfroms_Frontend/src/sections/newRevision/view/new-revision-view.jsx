import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import moment from "moment";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, Backdrop, CircularProgress, FormControl, FormHelperText, Radio, RadioGroup, Snackbar, TextField} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import {DatePicker, TimePicker} from "@mui/x-date-pickers";
import {AdapterMoment} from '@mui/x-date-pickers/AdapterMoment';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import FormControlLabel from "@mui/material/FormControlLabel";
import UserProfile from "../../../components/auth/UserInfo";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import {useNavigate} from "react-router-dom";


// ----------------------------------------------------------------------

export default function NewRevisionPage()
{
    // this page is only for leaders

    const [loadingShow, setLoadingShow] = useState(false);

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    const [progColor, setProgColor] = useState("primary");
    useEffect(() => {
        (setProgPercent(((shownSection - 1) / 4) * 100))
    }, [shownSection]);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };


    const [schools, setSchools] = useState([]);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    // get schools and courses
    async function getAllSchools() {
        try {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/school`, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then((data) => {
                    setSchools(data.transObject)
                })
                .then(() => {
                    setLoadingShow(false)
                });
        } catch (error) {
            console.log(error);
            setLoadingShow(false);
        }
    }

    function handleSelectedSchool(school) {
        setLoadingShow(true);
        setSelectedSchool(school);
        setCourses(school.courses);
        setLoadingShow(false);
    }


    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);

        if (userRole === "leader")
        {
            getAllSchools();
        }
        else
        {
            setErrorMsg("you are not allowed to access this page");
            setErrorShow(true);
        }

    }

    // get school and courses on load - if not leader and there is param
    useEffect(() => {getUserInfo()}, []);


    const [revWeekSelectedDate, setRevWeekSelectedDate] = useState(null);
    const [revWeekSelectedStartTime, setRevWeekSelectedStartTime] = useState(null);
    const [revWeekSelectedEndTime, setRevWeekSelectedEndTime] = useState(null);

    // revision type & notes elements
    const [revisionType, setRevisionType] = useState("physical");
    const [revisionNote, setRevisionNote] = useState("");


    // submit function
    async function createSubmit() {
        // booking elements
        const bookingDate = moment(revWeekSelectedDate).toDate();
        const bookingNote = revisionNote;

        const startTime = revWeekSelectedStartTime;
        const endTime = revWeekSelectedEndTime;

        let isOnline = false;
        let bookingLimit = 22;

        if (revisionType === "online")
        {
            isOnline = true;
            bookingLimit = 60;
        }

        const courseID = selectedCourse.courseid;

        // do booking dto
        const revisionDto = {"bookingDate": bookingDate, "note": bookingNote, "starttime":startTime, "endtime":endTime, "bookinglimit": bookingLimit, "isonline": isOnline, "course": {"courseid": courseID}};
        console.log(revisionDto);

        await submitBooking(revisionDto);
    }

    async function submitBooking(revisionDto)
    {
        let isok = false;
        let isBad = false;

        try {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(revisionDto)};

            await fetch(`http://localhost:8080/api/revision`, requestOptions)
                .then(response => {
                    if (response.status === 201 || response.status === 200) {
                        isok = true;
                        setProgPercent(100);
                        return response.json();
                    } else if (response.status === 400) {
                        isBad = true;
                        return response.json();
                    } else if (response.status === 401) {
                        setErrorMsg("you are not allowed to do this action");
                        setErrorShow(true);
                    } else if (response.status === 404) {
                        setErrorMsg("the request was not found on the server, double check your connection");
                        setErrorShow(true);
                    } else {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then((data) => {
                    setLoadingShow(false);
                    if (isok) {
                        // it is fine, go on
                        setMadeRevision(data.transObject);
                        console.log(data);
                    } else if (isBad) {
                        // errors for booking
                        let errorString = "";
                        data.error.forEach((errorItem) => {
                            if (errorString === "") {
                                errorString = errorItem
                            } else {
                                errorString = errorItem + "\n" + errorString
                            }
                        });
                        setErrorMsg(errorString);
                        setErrorShow(true);
                        console.log(data);
                    } else {
                        console.log(data);
                    }
                })

        } catch (error) {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    // complete dialog
    const [madeRevision, setMadeRevision] = useState(null);

    const [showComplete, setShowComplete] = useState(false);

    function showCompleation()
    {
        setProgColor("success");
        setProgPercent(100);
        setShowComplete(true);
    }

    useEffect(() => {
        if (madeRevision !== null) {showCompleation()}}, [madeRevision]);


    let navigate = useNavigate();
    const goToRevision = () => {
        if (madeRevision !== null) {
            let path = `/viewRevision?revisionID=${madeRevision.bookingid}`;
            navigate(path);
        }
    }


    function nextSection() {
        if (shownSection === 1) {
            if (selectedSchool !== null && selectedCourse !== null && selectedSchool !== undefined && selectedCourse !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Select a School and a Course Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 2) {
            if (selectedSchool !== null &&
                selectedCourse !== null &&
                revWeekSelectedStartTime !== null &&
                revWeekSelectedEndTime !== null &&
                revWeekSelectedDate !== null &&
                selectedSchool !== undefined &&
                selectedCourse !== undefined &&
                revWeekSelectedStartTime !== undefined &&
                revWeekSelectedEndTime !== undefined &&
                revWeekSelectedDate !== undefined &&
                Object.keys(selectedSchool).length !== 0 &&
                Object.keys(selectedCourse).length !== 0 &&
                Object.keys(revWeekSelectedStartTime).length !== 0 &&
                Object.keys(revWeekSelectedEndTime).length !== 0 &&
                Object.keys(revWeekSelectedDate).length !== 0
            ) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please specify a valid start time, end time, and date");
                setErrorShow(true);
            }
        }

        if (shownSection === 3) {
            if (selectedSchool !== null &&
                selectedCourse !== null &&
                revisionType !== null &&
                revWeekSelectedStartTime !== null &&
                revWeekSelectedEndTime !== null &&
                revWeekSelectedDate !== null &&
                revisionNote !== null &&
                selectedSchool !== undefined &&
                selectedCourse !== undefined &&
                revisionType !== undefined &&
                revWeekSelectedStartTime !== undefined &&
                revWeekSelectedEndTime !== undefined &&
                revWeekSelectedDate !== undefined &&
                revisionNote !== undefined &&
                Object.keys(selectedSchool).length !== 0 &&
                Object.keys(selectedCourse).length !== 0 &&
                Object.keys(revisionType).length !== 0 &&
                Object.keys(revWeekSelectedStartTime).length !== 0 &&
                Object.keys(revWeekSelectedEndTime).length !== 0 &&
                Object.keys(revWeekSelectedDate).length !== 0 &&
                Object.keys(revisionNote).length !== 0
            ) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please select the session type and add note");
                setErrorShow(true);
            }
        }

        if (shownSection === 4) {
            createSubmit();
        }

    }

    function prevSection() {
        setShownSection((shownSection) - 1);
    }

    const CustomPaper = (props) => {
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
                        The booking has been made!!, please click below to view it.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    {
                        madeRevision !== null && <Button onClick={goToRevision} autoFocus> Go to Revision. </Button>
                    }
                </DialogActions>
            </Dialog>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">New Revision</Typography>

                <div>
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                            disabled={shownSection === 1} onClick={prevSection} sx={{m: 1}}>
                        Back
                    </Button>
                    <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                            onClick={nextSection}>
                        {shownSection === 4 ? (
                            <>Submit</>
                        ) : (
                            <>Next</>
                        )}
                    </Button>
                </div>


            </Stack>

            {/* prog bar */}
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
                            onChange={(event, newValue) => {
                                handleSelectedSchool(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="School"/>}
                            getOptionLabel={(option) => option.schoolname}
                            renderOption={(props, option) => {
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
                            onChange={(event, newValue) => {
                                setSelectedCourse(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="Course"/>}
                            getOptionLabel={(option) => option.courseid + " " + option.coursename}
                            renderOption={(props, option) => {
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

            {/* date and time card */}
            {
                shownSection === 2 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Select Date:</Typography>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <DatePicker sx={{width: "100%", mt: 1}} label="Revision Date" format={"DD/MM/YYYY"}
                                        value={revWeekSelectedDate} onChange={(newValue) => {
                                setRevWeekSelectedDate(newValue)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select a day where you will offer the revision session.</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>Select Time:</Typography>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1, mr: 1}} label="Start Time" value={revWeekSelectedStartTime} onChange={(newValue) => {
                                setRevWeekSelectedStartTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1,}} label="End Time" minTime={revWeekSelectedStartTime}
                                        value={revWeekSelectedEndTime} onChange={(newValue) => {
                                setRevWeekSelectedEndTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select start and end time for the revision session.</FormHelperText>
                    </div>
                </Card>
            }

            {/* group formation & online/physical card */}
            {
                shownSection === 3 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Session Type:</Typography>
                        <FormControl>
                            <RadioGroup row value={revisionType} onChange={(event, newValue) => {
                                setRevisionType(newValue)
                            }}>
                                <FormControlLabel value="physical" control={<Radio/>} label="Physical"/>
                                <FormControlLabel value="online" control={<Radio/>} label="Online"/>
                            </RadioGroup>
                        </FormControl>
                        <FormHelperText>How will the session be conducted.</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>Revision Note:</Typography>
                        <TextField sx={{width: '100%', mt: 1}} label="This Session will focuse on:"
                                   variant="outlined" multiline rows={4} value={revisionNote}
                                   onChange={(newValue) => setRevisionNote(newValue.target.value)}/>
                        <FormHelperText>Mention the main aim and what will be covered.</FormHelperText>
                    </div>
                </Card>
            }

            {/* overview and submit card */}
            {
                shownSection === 4 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Overview:</Typography>
                        <FormHelperText>Please validate the revision session before submitting.</FormHelperText>

                        <TextField label="School" variant="standard" fullWidth sx={{mb: 1, mt: 3}}
                                   InputProps={{readOnly: true}} defaultValue={selectedSchool.schoolname}/>
                        <TextField label="Course" variant="standard" fullWidth sx={{mb: 1, mt: 1}}
                                   InputProps={{readOnly: true}} defaultValue={selectedCourse.courseid + " " + selectedCourse.coursename}/>

                        <TextField label="Date" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}}
                                   defaultValue={moment(revWeekSelectedDate).format("DD/MM/YYYY")}/>

                        <TextField label="From" variant="standard" sx={{mb: 1, mt: 1, mr: 1}} InputProps={{readOnly: true}}
                                   defaultValue={moment(revWeekSelectedStartTime).format("hh:mma")}/>
                        <TextField label="To" variant="standard" sx={{mb: 1, mt: 1,}} InputProps={{readOnly: true}}
                                   defaultValue={moment(revWeekSelectedEndTime).format("hh:mma")}/>

                        <TextField label="Session Type" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={revisionType}/>

                        <TextField label="Area of help" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={revisionNote} multiline maxRows={4}/>
                    </div>
                </Card>
            }
        </Container>
    );
}
