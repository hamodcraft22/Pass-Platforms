import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import WeekCalendar from 'react-week-calendar';
import moment from "moment";
import bookingSlot from "../bookingSlot";
import Toolbar from "@mui/material/Toolbar";
import MultiSelect from "../MultiSelect";
import React, {useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, Snackbar, TextField} from "@mui/material";


// ----------------------------------------------------------------------

export default function BookingPage() {

    const [errorShow, setErrorShow] = useState(false);
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setErrorShow(false);
    };
    const [errorMsg, setErrorMsg] = useState("");



    const mockSchools = [{"schoolID":"zift1", "schoolName":"ziftSchool1"}, {"schoolID":"zift2", "schoolName":"ziftSchool2"}];
    const mockCourses = [{"courseID":"zift1", "courseName":"ziftcourse1"}, {"courseID":"zift2", "courseName":"asdsd"}];

    const [schools, setSchools] = useState(mockSchools);
    const [selectedSchool, setSelectedSchool] = useState();


    const [courses, setCourses] = useState(mockCourses);
    const [selectedCourse, setSelectedCourse] = useState();



    const selectedIntervals = [
        {
            uid: 1,
            start: moment({h: 10, m: 0}),
            end: moment({h: 11, m: 0}),
            color: "#94E387FF"
        },
        {
            uid: 2,
            start: moment({h: 12, m: 0}).add(2, 'd'),
            end: moment({h: 13, m: 0}).add(2, 'd'),
            online: true,
            color: "#E494EEFF"
        },
        {
            uid: 3,
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


        }
    }

    function prevSection()
    {
        setShownSection((shownSection)-1);
    }




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
                            options={schools}
                            value={selectedSchool}
                            onChange={(event, newValue) => {setSelectedSchool(newValue)}}
                            sx={{ width: '100%', mt:1}}
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



                        <Typography variant="h6" sx={{mt:3}}>Select Course:</Typography>
                        <Autocomplete
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
                    </div>
                </Card>
            }

            {/*select slot card */}
            {
                shownSection===2 && <Card>
                    <div style={{padding: "15px"}}>
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
                            onEventClick={(event) => {
                                alert(event.uid)
                            }}
                            eventComponent={bookingSlot}
                            eventSpacing={0}
                        />
                    </div>
                </Card>
            }

            {/* confirm slot selection */}
            {

            }

            {/* group formation & information card */}
            {

            }

            {/* overview and submit card */}
            {

            }
        </Container>
    );
}
