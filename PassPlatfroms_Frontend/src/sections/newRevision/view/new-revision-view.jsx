import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import moment from "moment";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, FormControl, FormHelperText, Radio, RadioGroup, Snackbar, TextField} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import {DatePicker, TimePicker} from "@mui/x-date-pickers";
import {AdapterMoment} from '@mui/x-date-pickers/AdapterMoment';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import FormControlLabel from "@mui/material/FormControlLabel";


// ----------------------------------------------------------------------

export default function NewRevisionPage() {

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
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

    // date and time elements
    const [revWeekStartDate, setRevWeekStartDate] = useState();
    const [revWeekEndDate, setRevWeekEndDate] = useState();
    const [revWeekStartTime, setRevWeekStartTime] = useState(moment({h: 8, m: 0}));
    const [revWeekEndTime, setRevWeekEndTime] = useState(moment({h: 22, m: 0}));

    const [revWeekSelectedDate, setRevWeekSelectedDate] = useState();
    const [revWeekSelectedStartTime, setRevWeekSelectedStartTime] = useState();
    const [revWeekSelectedEndTime, setRevWeekSelectedEndTime] = useState();

    // revision type & notes elements
    const [revisionType, setRevisionType] = useState("physical");
    const [revisionNote, setRevisionNote] = useState("");


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
                selectedSchool !== undefined &&
                selectedCourse !== undefined &&
                revisionType !== undefined &&
                revWeekSelectedStartTime !== undefined &&
                revWeekSelectedEndTime !== undefined &&
                revWeekSelectedDate !== undefined &&
                Object.keys(selectedSchool).length !== 0 &&
                Object.keys(selectedCourse).length !== 0 &&
                Object.keys(revisionType).length !== 0 &&
                Object.keys(revWeekSelectedStartTime).length !== 0 &&
                Object.keys(revWeekSelectedEndTime).length !== 0 &&
                Object.keys(revWeekSelectedDate).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please select the session type");
                setErrorShow(true);
            }
        }

        if (shownSection === 4) {
            alert("call api and show results based on api return");
            setProgPercent(100);
            // change color of progress to red if it is error etc
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

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

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
                            onChange={(event, newValue) => {
                                setSelectedSchool(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="School"/>}
                            getOptionLabel={(option) => option.schoolName}
                            renderOption={(props, option) => {
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
                            onChange={(event, newValue) => {
                                setSelectedCourse(newValue)
                            }}
                            sx={{width: '100%', mt: 1}}
                            renderInput={(params) => <TextField {...params} label="Course"/>}
                            getOptionLabel={(option) => option.courseName}
                            renderOption={(props, option) => {
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

            {/* date and time card */}
            {
                shownSection === 2 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Select Date:</Typography>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <DatePicker sx={{width: "100%", mt: 1}} label="Revision Date" minDate={revWeekStartDate}
                                        maxDate={revWeekEndDate} value={revWeekSelectedDate} onChange={(newValue) => {
                                setRevWeekSelectedDate(newValue)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select a day where you will offer the revision session.</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>Select Time:</Typography>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1, mr: 1}} label="Start Time" minTime={revWeekStartTime}
                                        maxTime={revWeekEndTime} value={revWeekSelectedStartTime} onChange={(newValue) => {
                                setRevWeekSelectedStartTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 1,}} label="End Time" minTime={revWeekStartTime} maxTime={revWeekEndTime}
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
                        <TextField sx={{width: '100%', mt: 1}} label="Optinal - This Session will focuse on:"
                                   variant="outlined" multiline rows={4} value={revisionNote}
                                   onChange={(newValue) => setRevisionNote(newValue.target.value)}/>
                        <FormHelperText>Please email the Pass Leader any material that you would be discussing during the
                            session.</FormHelperText>
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
                                   InputProps={{readOnly: true}} defaultValue={selectedSchool.schoolName}/>
                        <TextField label="Course" variant="standard" fullWidth sx={{mb: 1, mt: 1}}
                                   InputProps={{readOnly: true}} defaultValue={selectedCourse.courseName}/>

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
