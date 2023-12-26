import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, FormHelperText, ListItem, ListItemIcon, Snackbar, TextField, ToggleButton} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import {AccountCircle} from "@mui/icons-material";
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import PublicIcon from '@mui/icons-material/Public';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {DatePicker, TimePicker} from "@mui/x-date-pickers";


// ----------------------------------------------------------------------

export default function UnscheduledBookingPage()
{

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() =>
    {
        (setProgPercent(((shownSection - 1) / 4) * 100))
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

    // school and courses elements
    const mockSchools = [{"schoolID": "zift1", "schoolName": "ziftSchool1"}, {
        "schoolID": "zift2",
        "schoolName": "ziftSchool2"
    }];
    const mockCourses = [{"courseID": "zift1", "courseName": "ziftcourse1"}, {
        "courseID": "zift2",
        "courseName": "asdsd"
    }];

    const [schools, setSchools] = useState(mockSchools);
    const [selectedSchool, setSelectedSchool] = useState();
    const [courses, setCourses] = useState(mockCourses);
    const [selectedCourse, setSelectedCourse] = useState();

    const [sessionDate, setSessionDate] = useState();

    const [startTime, setStartTime] = useState();
    const [endTime, setEndTime] = useState();

    // group & information setup elements
    const [groupMembers, setGroupMembers] = useState([]);

    const [bookingOnline, setBookingOnline] = useState(false);

    const [helpInText, setHelpInText] = useState("");


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
            if (selectedSchool !== null && selectedCourse !== null && sessionDate !== null && startTime !== null && endTime !== null && selectedSchool !== undefined && selectedCourse !== undefined && sessionDate !== undefined && startTime !== undefined && endTime !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0 && Object.keys(sessionDate).length !== 0 && Object.keys(startTime).length !== 0 && Object.keys(endTime).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Please add in the session time details");
                setErrorShow(true);
            }
        }

        if (shownSection === 3)
        {
            if (selectedSchool !== null && selectedCourse !== null && sessionDate !== null && startTime !== null && endTime !== null && helpInText !== null && selectedSchool !== undefined && selectedCourse !== undefined && sessionDate !== undefined && startTime !== undefined && endTime !== undefined && helpInText !== undefined && helpInText !== "" && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0 && Object.keys(sessionDate).length !== 0 && Object.keys(startTime).length !== 0 && Object.keys(endTime).length !== 0 && Object.keys(helpInText).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Please input the help area necessary");
                setErrorShow(true);
            }
        }

        if (shownSection === 4)
        {
            alert("call api and show results based on api return");
            setProgPercent(100);
            // change color of progress to red if it is error etc
        }

    }

    function prevSection()
    {
        setShownSection((shownSection) - 1);
    }


    const CustomPaper = (props) =>
    {
        return <Paper elevation={8} {...props} />;
    };

    return (


        <Container>

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Unscheduled Booking</Typography>

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

            <Box sx={{width: '100%', mb: 2}}>
                <LinearProgress variant="determinate" value={progPercent} style={{borderRadius: 5, height: 10}}/>
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
                                setSelectedSchool(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="School"/>}
                            getOptionLabel={(option) => option.schoolName}
                            renderOption={(props, option) =>
                            {
                                return (
                                    <li {...props}>
                                        {option.schoolName}
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
                            getOptionLabel={(option) => option.courseName}
                            renderOption={(props, option) =>
                            {
                                return (
                                    <li {...props}>
                                        {option.courseName}
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
                        <Typography variant="h6" sx={{mb: 1}}>Select Session Time:</Typography>

                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Session Date" value={sessionDate} onChange={(value) =>
                            {
                                setSessionDate(value)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>What day was the session?</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>Select Time:</Typography>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1, width: "100%"}} label="Start Time" value={startTime} onChange={(value) =>
                            {
                                setStartTime(value)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1, width: "100%"}} label="End Time" value={endTime} onChange={(value) =>
                            {
                                setEndTime(value)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select start and end time for the unscheduled session.</FormHelperText>

                    </div>
                </Card>
            }

            {/* group formation & information card */}
            {
                shownSection === 3 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Group Session? Add Others:</Typography>

                        <Autocomplete
                            PaperComponent={CustomPaper}
                            multiple
                            freeSolo
                            sx={{width: '100%', mt: 1}}
                            options={[]}
                            value={groupMembers}
                            onChange={(event, newValue) =>
                            {
                                setGroupMembers(newValue)
                            }}
                            renderInput={(params) => <TextField {...params} label="Student ID/s - Optional"/>}
                        />
                        <FormHelperText>Please note that if other students did not upload their schedules before hand, they
                            might have clashes within your selected session.</FormHelperText>


                        <Typography variant="h6" sx={{mt: 3}}>Online Session? Toggle:</Typography>
                        <FormHelperText sx={{ml: 2}}>Online</FormHelperText>
                        <ToggleButton
                            value={bookingOnline}
                            selected={bookingOnline}
                            sx={{width: '100%'}}
                            color={"primary"}
                            onChange={() =>
                            {
                                setBookingOnline(!bookingOnline)
                            }}
                        >
                            <PublicIcon/>
                        </ToggleButton>

                        <Typography variant="h6" sx={{mt: 3}}>Help Area:</Typography>
                        <TextField sx={{width: '100%', mt: 1}} label="I healped the students in:"
                                   variant="outlined" multiline rows={4} value={helpInText}
                                   onChange={(newValue) => setHelpInText(newValue.target.value)}/>
                    </div>
                </Card>
            }

            {/* overview and submit card */}
            {
                shownSection === 4 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Overview:</Typography>
                        <FormHelperText>Please validate your booking before submitting.</FormHelperText>

                        <TextField label="School" variant="standard" fullWidth sx={{mb: 1, mt: 3}}
                                   InputProps={{readOnly: true}} defaultValue={selectedSchool.schoolName}/>
                        <TextField label="Course" variant="standard" fullWidth sx={{mb: 1, mt: 1}}
                                   InputProps={{readOnly: true}} defaultValue={selectedCourse.courseName}/>

                        {/* loop of members - if added - maybe add name get? */}
                        {
                            groupMembers !== null && groupMembers !== undefined && Object.keys(groupMembers).length !== 0 ?
                                (
                                    <>
                                        <FormHelperText>Members</FormHelperText>
                                        <List dense>
                                            {
                                                groupMembers && groupMembers.map((studentID) => (
                                                    <ListItem>
                                                        <ListItemIcon><AccountCircle/></ListItemIcon>
                                                        <ListItemText primary={studentID}/>
                                                    </ListItem>
                                                ))
                                            }
                                        </List>
                                    </>
                                ) : (<></>)

                        }

                        <TextField label="Area of help" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={helpInText} multiline maxRows={4}/>


                    </div>
                </Card>
            }
        </Container>
    );
}
