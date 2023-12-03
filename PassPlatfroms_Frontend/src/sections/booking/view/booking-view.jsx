import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import WeekCalendar from 'react-week-calendar';
import moment from "moment";
import bookingSlot from "../bookingSlot";
import Toolbar from "@mui/material/Toolbar";
import MultiSelect from "../MultiSelect";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, CircularProgress, FormHelperText, Snackbar, TextField} from "@mui/material";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from "@mui/material/Paper";


// ----------------------------------------------------------------------

export default function BookingPage() {

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway')
        {
            return;
        }
        setErrorShow(false);
    };

    // school and courses elements
    const mockSchools = [{"schoolID":"zift1", "schoolName":"ziftSchool1"}, {"schoolID":"zift2", "schoolName":"ziftSchool2"}];
    const mockCourses = [{"courseID":"zift1", "courseName":"ziftcourse1"}, {"courseID":"zift2", "courseName":"asdsd"}];

    const [schools, setSchools] = useState(mockSchools);
    const [selectedSchool, setSelectedSchool] = useState();
    const [courses, setCourses] = useState(mockCourses);
    const [selectedCourse, setSelectedCourse] = useState();

    // time slots elements
    const [courseLeaders, setCourseLeaders] = useState([]);
    const [leaderSlots, setLeadersSlot] = useState([]);
    const selectedIntervals = [
        {
            uid: 1,
            day: 'mockDay',
            leader: 'Mock Leader',
            start: moment({h: 10, m: 0}),
            end: moment({h: 11, m: 0}),
            color: "#94E387FF"
        },
        {
            uid: 2,
            day: 'mockDay',
            leader: 'Mock Leader',
            start: moment({h: 12, m: 0}).add(2, 'd'),
            end: moment({h: 13, m: 0}).add(2, 'd'),
            online: true,
            color: "#E494EEFF"
        },
        {
            uid: 3,
            day: 'mockDay',
            leader: 'Mock Leader',
            start: moment({h: 10, m: 0}).add(-1, 'd').add(-1, 'h'),
            end: moment({h: 11, m: 0}).add(-1, 'd').add(-1, 'h'),
            online: true,
            color: "#94E387FF"
        }

    ]

    const leaders = [
        { id: 202002789, name: "Mohamed Hasan", color: "#94E387FF" },
        { id: 202001478, name: "Sara Alshamari", color: "#E494EEFF" },
    ];

    let selectedLeaders = [];


    // slot confirmation elemnts
    const [selctedSlot, setSelctedSlot] = useState([]);
    const [slotConfirmShow, setSlotConfirmShow] = useState(false);
    const [slotToConfirm, setSlotToConfirm] = useState([]);
    const handleSlotConfirmClose = () => {
        setSlotConfirmShow(false);
        setSlotToConfirm([]);
    };
    const handleSlotConfirm = () => {
        setSlotConfirmShow(false);
        setSelctedSlot(slotToConfirm);
        setSlotToConfirm(null);
    };
    const handleSlotSelect = (slot) => {
        setSlotToConfirm(slot);
    };
    useEffect(() => {if(slotToConfirm!==null && slotToConfirm!==undefined && Object.keys(slotToConfirm).length !== 0){setSlotConfirmShow(true)}}, [slotToConfirm]);
    useEffect(() => {if(selctedSlot!==null && selctedSlot!==undefined && Object.keys(selctedSlot).length !== 0){nextSection()}}, [selctedSlot]);


    const [shownSection, setShownSection] = useState(1);

    function nextSection()
    {
        if (shownSection !== 4)
        {
            if (shownSection === 1)
            {
                if (selectedSchool !== null && selectedCourse !== null && selectedSchool !== undefined && selectedCourse !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0)
                {
                    setShownSection((shownSection)+1);
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
                    setShownSection((shownSection)+1);
                }
                else
                {
                    setErrorMsg("Select a Slot Please");
                    setErrorShow(true);
                }
            }

        }
    }

    function prevSection()
    {
        setShownSection((shownSection)-1);
    }


    const [value, setValue] = useState([]);

    useEffect(() => {console.log(value)}, [value]);

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (


        <Container>

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose} anchorOrigin={{ vertical: 'top', horizontal: 'right' }} >
                <Alert onClose={handleAlertClose} severity="error" sx={{ width: '100%' }}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">New Booking</Typography>

                <div >
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>} disabled={shownSection===1} onClick={prevSection} sx={{ m: 1 }}>
                        Back
                    </Button>
                    <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>} onClick={nextSection}>
                        {shownSection===4 ? (
                            <>Submit</>
                        ) : (
                            <>Next</>
                        )}
                    </Button>
                </div>

            </Stack>

            {/* elements */}
            {/*select course and school card */}
            {
                shownSection===1 && <Card>
                    <div style={{padding: "15px"}} >
                        <Typography variant="h6">Select School:</Typography>
                        <Autocomplete
                            PaperComponent={CustomPaper}
                            options={schools}
                            value={selectedSchool}
                            onChange={(event, newValue) => {setSelectedSchool(newValue)}}
                            sx={{ width: '100%', mt:1 }}
                            renderInput={(params) => <TextField {...params} label="School" />}
                            getOptionLabel={(option) => option.schoolName}
                            renderOption={(props, option, {selected}) => {
                                return (
                                    <li {...props}>
                                        {option.schoolName}
                                    </li>
                                );
                            }}
                        />
                        <FormHelperText>Select a School to show all of the courses available.</FormHelperText>



                        <Typography variant="h6" sx={{mt:3}}>Select Course:</Typography>
                        <Autocomplete
                            PaperComponent={CustomPaper}
                            options={courses}
                            value={selectedCourse}
                            onChange={(event, newValue) => {setSelectedCourse(newValue)}}
                            sx={{ width: '100%', mt:1}}
                            renderInput={(params) => <TextField {...params} label="Course" />}
                            getOptionLabel={(option) => option.courseName}
                            renderOption={(props, option, {selected}) => {
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
                shownSection===2 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6" sx={{mb:1}}>Select Slot:</Typography>
                        <Toolbar
                            sx={{
                                minHeight: 96,
                                marginBottom: '10px',
                                display: 'flex',
                                justifyContent: 'space-between',
                                p: (theme) => theme.spacing(0, 1, 0, 3)
                            }}
                        >
                            {/* add list of leaders here*/}
                            <MultiSelect
                                items={leaders}
                                label="Leaders"
                                selectAllLabel="All"
                                leaders={(items) => {selectedLeaders = items; console.log(leaders)}}
                            />


                        </Toolbar>

                        <WeekCalendar
                            dayFormat={"dd DD/MM"}
                            firstDay={moment().weekday(0)}
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
                                (<>Your Session will be on <b>{moment(slotToConfirm.start).format("DD/MM/YYYY")}</b> - <b>{moment(slotToConfirm.start).format("dddd")}</b>, and will be conducted by <b>{slotToConfirm.leader}</b> from <b>{moment(slotToConfirm.start).format("hh:mma")}</b> till <b>{moment(slotToConfirm.end).format("hh:mma")}</b>, {slotToConfirm.online ? (<b>Online</b>) : (<b>Physically</b>)}</>) : (<CircularProgress color="inherit" />)
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

            {/* group formation & information card */}
            {
                shownSection===3 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Group Session? Add Others:</Typography>

                        <Autocomplete
                            PaperComponent={CustomPaper}
                            multiple
                            freeSolo
                            sx={{ width: '100%', mt:1}}
                            options={[]}
                            value={value}
                            onChange={(event,newValue) => {setValue(newValue)}}
                            renderInput={(params) => <TextField {...params} label="Student ID" />}
                        />

                        <FormHelperText>Please note that if other students did not upload their schedules before hand, they might have clashes within your selected session.</FormHelperText>

                        <Typography variant="h6" sx={{mt:3}}>Help Area:</Typography>
                        <TextField sx={{ width: '100%', mt:1}} label="I want the Pass Leader to Help Me In:" variant="outlined" multiline rows={4}/>
                        <FormHelperText>Please email the Pass Leader any material that you would be discussing during the session.</FormHelperText>
                    </div>
                </Card>
            }

            {/* overview and submit card */}
            {

            }
        </Container>
    );
}
