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
import {Alert, Autocomplete, CircularProgress, FormHelperText, ListItem, ListItemIcon, Snackbar, TextField, ToggleButton} from "@mui/material";
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
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import PublicIcon from '@mui/icons-material/Public';


// ----------------------------------------------------------------------

export default function NewBookingPage() {

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



    const [schools, setSchools] = useState();
    const [selectedSchool, setSelectedSchool] = useState();
    const [courses, setCourses] = useState();
    const [selectedCourse, setSelectedCourse] = useState();


    // get schools and courses
    async function getAvlbSchools()
    {
        try
        {
            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json'}};

            await fetch(`http://localhost:8080/api/school/schools`, requestOptions)
                .then(response => {return response.json()})
                .then((data) => {setSchools(data.transObject)})
        }
        catch (error)
        {
            console.log(error)
        }
    }

    function handleSelectedSchool(school)
    {
        setSelectedSchool(school);
        setCourses(school.courses);
    }

    useEffect(() => {getAvlbSchools()}, [])




    // time slots elements

    const [bookingStartDate, setBookingStartDate] = useState(moment().weekday(0));

    const [recivedSlotsDto, setRecivedSlotsDto] = useState([]);

    // getting slots api
    async function getAvlbSlots()
    {
        try
        {
            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": "sda"}};

            await fetch(`http://localhost:8080/api/slot/course/IT6008?weekStart=${bookingStartDate.format("MM/DD/YYYY")}`, requestOptions)
                .then(response => {return response.json()})
                .then((data) => {setRecivedSlotsDto(data.transObject)})

        }
        catch (error)
        {
            console.log(error)
        }
    }

    useEffect(() => {if(shownSection === 2){getAvlbSlots()}}, [shownSection]);

    // parsing slots
    function parseSlots()
    {
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

        recivedSlotsDto.forEach((leader, index) =>
        {
            const leaderColor = pastelColors[index];

            const leaderID = leader.leaderID;
            const leaderName = leader.leaderName;

            let leaderSlos = [];

            // loop throght leader slots and assign them the color
            leader.slots.forEach((slot) => {
                const slotid = slot.slotid;

                const slotType = slot.slotType.typename;

                let daysToAdd = 0;

                if (slot.day.dayid === 'M')
                {
                    daysToAdd = 1;
                }
                else if (slot.day.dayid === 'T')
                {
                    daysToAdd = 2;
                }
                else if (slot.day.dayid === 'W')
                {
                    daysToAdd = 3;
                }
                else if (slot.day.dayid === 'R')
                {
                    daysToAdd = 4;
                }
                else if (slot.day.dayid === 'F')
                {
                    daysToAdd = 5;
                }
                else if (slot.day.dayid === 'S')
                {
                    daysToAdd = 6;
                }

                const startTime = moment(bookingStartDate.clone().add(daysToAdd, 'day').format('YYYY-MM-DD') + 'T' + moment(slot.starttime).format('HH:mm:ss'));
                const endTime = moment(bookingStartDate.clone().add(daysToAdd, 'day').format('YYYY-MM-DD') + 'T' + moment(slot.endtime).format('HH:mm:ss'));

                // create new slot object with color
                leaderSlos.push({"uid":slotid,"start":startTime, "end":endTime, "slotType":slotType, "color":leaderColor});
            })


            // add parsed leaders
            parsedLeaders.push({"leaderID":leaderID, "leaderName":leaderName, "slots":leaderSlos, "color":leaderColor});
        });

        setLeaders(parsedLeaders);
    }

    // leaders + their slots
    const [leaders, setLeaders] = useState([]);

    // selcted leaders + their slots
    const [selectedLeaders, setSelectedLeaders] = useState([]);

    // add selcted leader slots to intrivals
    function handleSlots()
    {
        let allSlots = [];

        selectedLeaders.forEach((leader) => {
            leader.slots.forEach((slot) => {
                allSlots.push(slot);
            })
        })

        setSelectedIntervals(allSlots);
    }

    useEffect(() => {if (selectedLeaders.length !== 0){handleSlots()}}, [selectedLeaders]);

    // slots to be shown
    const [selectedIntervals, setSelectedIntervals] = useState([]);

    const selectedzIntervals = [
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



    useEffect(() => {if (Object.keys(recivedSlotsDto).length !== 0) {parseSlots()}}, [recivedSlotsDto])

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
    useEffect(() => {
        if (slotToConfirm !== null && slotToConfirm !== undefined && Object.keys(slotToConfirm).length !== 0) {
            setSlotConfirmShow(true)
        }
    }, [slotToConfirm]);
    useEffect(() => {
        if (selctedSlot !== null && selctedSlot !== undefined && Object.keys(selctedSlot).length !== 0) {
            nextSection()
        }
    }, [selctedSlot]);


    // group & information setup elements
    const [groupMembers, setGroupMembers] = useState([]);

    const [bookingOnline, setBookingOnline] = useState(false);

    const [helpInText, setHelpInText] = useState("");


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
            if (selectedSchool !== null && selectedCourse !== null && selctedSlot !== null && selectedSchool !== undefined && selectedCourse !== undefined && selctedSlot !== undefined && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0 && Object.keys(selctedSlot).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Select a Slot Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 3) {
            if (selectedSchool !== null && selectedCourse !== null && selctedSlot !== null && helpInText !== null && selectedSchool !== undefined && selectedCourse !== undefined && selctedSlot !== undefined && helpInText !== undefined && helpInText !== "" && Object.keys(selectedSchool).length !== 0 && Object.keys(selectedCourse).length !== 0 && Object.keys(selctedSlot).length !== 0 && Object.keys(helpInText).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please input the help area necessary");
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
                <Typography variant="h4">New Booking</Typography>

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
                                leaders={(items) => {setSelectedLeaders(items)}}
                            />

                            <Box sx={{mt: 1, width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                                        onClick={prevSection}>
                                    Prev Week
                                </Button>

                                <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                                        onClick={nextSection}>
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
                            onChange={(event, newValue) => {
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
                            onChange={() => {
                                setBookingOnline(!bookingOnline)
                            }}
                        >
                            <PublicIcon/>
                        </ToggleButton>

                        <Typography variant="h6" sx={{mt: 3}}>Help Area:</Typography>
                        <TextField sx={{width: '100%', mt: 1}} label="I want the Pass Leader to Help Me In:"
                                   variant="outlined" multiline rows={4} value={helpInText}
                                   onChange={(newValue) => setHelpInText(newValue.target.value)}/>
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
                        <FormHelperText>Please validate your booking before submitting.</FormHelperText>

                        <TextField label="School" variant="standard" fullWidth sx={{mb: 1, mt: 3}}
                                   InputProps={{readOnly: true}} defaultValue={selectedSchool.schoolName}/>
                        <TextField label="Course" variant="standard" fullWidth sx={{mb: 1, mt: 1}}
                                   InputProps={{readOnly: true}} defaultValue={selectedCourse.courseName}/>

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
                        }} defaultValue={selctedSlot.leader}/>

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
