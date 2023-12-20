import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import moment from "moment";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, CardContent, CircularProgress, FormHelperText, ListItem, ListItemIcon, Snackbar, TextField} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import {AccountCircle} from "@mui/icons-material";
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {DatePicker} from "@mui/x-date-pickers";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {styled} from "@mui/material/styles";
import {read, utils} from 'xlsx';
import Grid from "@mui/material/Unstable_Grid2";
import ApexChart from 'react-apexcharts';
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import DialogContentText from "@mui/material/DialogContentText";


// ----------------------------------------------------------------------

export default function ManagementPage() {

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() => {
        (setProgPercent(((shownSection - 1) / 5) * 100))
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

    function nextSection() {
        if (shownSection === 1) {
            if (midRevWeekStart !== null && midRevWeekEnd !== null && midWeekStart !== null && midWeekEnd !== null &&
                midRevWeekStart !== undefined && midRevWeekEnd !== undefined && midWeekStart !== undefined && midWeekEnd !== undefined &&
                Object.keys(midRevWeekStart).length !== 0 && Object.keys(midRevWeekEnd).length !== 0 && Object.keys(midWeekStart).length !== 0 && Object.keys(midWeekEnd).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please fill all date details");
                setErrorShow(true);
            }
        }

        if (shownSection === 2) {
            if (midRevWeekStart !== null && midRevWeekEnd !== null && midWeekStart !== null && midWeekEnd !== null &&
                midRevWeekStart !== undefined && midRevWeekEnd !== undefined && midWeekStart !== undefined && midWeekEnd !== undefined &&
                Object.keys(midRevWeekStart).length !== 0 && Object.keys(midRevWeekEnd).length !== 0 && Object.keys(midWeekStart).length !== 0 && Object.keys(midWeekEnd).length !== 0 &&
                finRevWeekStart !== null && finRevWeekEnd !== null && finWeekStart !== null && finWeekEnd !== null &&
                finRevWeekStart !== undefined && finRevWeekEnd !== undefined && finWeekStart !== undefined && finWeekEnd !== undefined &&
                Object.keys(finRevWeekStart).length !== 0 && Object.keys(finRevWeekEnd).length !== 0 && Object.keys(finWeekStart).length !== 0 && Object.keys(finWeekEnd).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please fill all date details");
                setErrorShow(true);
            }
        }

        if (shownSection === 3) {
            if (midRevWeekStart !== null && midRevWeekEnd !== null && midWeekStart !== null && midWeekEnd !== null &&
                midRevWeekStart !== undefined && midRevWeekEnd !== undefined && midWeekStart !== undefined && midWeekEnd !== undefined &&
                Object.keys(midRevWeekStart).length !== 0 && Object.keys(midRevWeekEnd).length !== 0 && Object.keys(midWeekStart).length !== 0 && Object.keys(midWeekEnd).length !== 0 &&
                finRevWeekStart !== null && finRevWeekEnd !== null && finWeekStart !== null && finWeekEnd !== null &&
                finRevWeekStart !== undefined && finRevWeekEnd !== undefined && finWeekStart !== undefined && finWeekEnd !== undefined &&
                Object.keys(finRevWeekStart).length !== 0 && Object.keys(finRevWeekEnd).length !== 0 && Object.keys(finWeekStart).length !== 0 && Object.keys(finWeekEnd).length !== 0 &&
                schoolsUpload !== null && schoolsUpload !== undefined && Object.keys(schoolsUpload).length !== 0
            ) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please upload course info");
                setErrorShow(true);
            }
        }

        if (shownSection === 4) {
            if (midRevWeekStart !== null && midRevWeekEnd !== null && midWeekStart !== null && midWeekEnd !== null &&
                midRevWeekStart !== undefined && midRevWeekEnd !== undefined && midWeekStart !== undefined && midWeekEnd !== undefined &&
                Object.keys(midRevWeekStart).length !== 0 && Object.keys(midRevWeekEnd).length !== 0 && Object.keys(midWeekStart).length !== 0 && Object.keys(midWeekEnd).length !== 0 &&
                finRevWeekStart !== null && finRevWeekEnd !== null && finWeekStart !== null && finWeekEnd !== null &&
                finRevWeekStart !== undefined && finRevWeekEnd !== undefined && finWeekStart !== undefined && finWeekEnd !== undefined &&
                Object.keys(finRevWeekStart).length !== 0 && Object.keys(finRevWeekEnd).length !== 0 && Object.keys(finWeekStart).length !== 0 && Object.keys(finWeekEnd).length !== 0 &&
                schoolsUpload !== null && schoolsUpload !== undefined && Object.keys(schoolsUpload).length !== 0 &&
                leadersUpload !== null && leadersUpload !== undefined && Object.keys(leadersUpload).length !== 0
            ) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please upload leaders info");
                setErrorShow(true);
            }
        }

        if (shownSection === 5) {
            alert("call api and show results based on api return");
            setProgPercent(100);
            // change color of progress to red if it is error etc
        }
    }

    function prevSection() {
        setShownSection((shownSection) - 1);
    }

    // change to false, both items are false, and they are changed based on return fom api

    const [setupMode, setSetupMode] = useState(false);
    const [viewEditMode, setViewEditMode] = useState(true);


    // dates
    const [midRevWeekStart, setMidRevWeekStart] = useState(null);
    const [midRevWeekEnd, setMidRevWeekEnd] = useState(null);

    const [midWeekStart, setMidWeekStart] = useState(null);
    const [midWeekEnd, setMidWeekEnd] = useState(null);

    // mid edits - in view mode
    const [midRevWeekStartEdit, setMidRevWeekStartEdit] = useState(null);
    const [midRevWeekEndEdit, setMidRevWeekEndEdit] = useState(null);

    const [midWeekStartEdit, setMidWeekStartEdit] = useState(null);
    const [midWeekEndEdit, setMidWeekEndEdit] = useState(null);


    const [finRevWeekStart, setFinRevWeekStart] = useState(null);
    const [finRevWeekEnd, setFinRevWeekEnd] = useState(null);

    const [finWeekStart, setFinWeekStart] = useState(null);
    const [finWeekEnd, setFinWeekEnd] = useState(null);

    // final dates - in edit modes

    const [finRevWeekStartEdit, setFinRevWeekStartEdit] = useState(null);
    const [finRevWeekEndEdit, setFinRevWeekEndEdit] = useState(null);

    const [finWeekStartEdit, setFinWeekStartEdit] = useState(null);
    const [finWeekEndEdit, setFinWeekEndEdit] = useState(null);


    const [systemEnable, setSystemEnable] = useState(false);
    const [bookingEnable, setBookingEnable] = useState(false);


    // excel extract
    const [schoolsUpload, setSchoolsUpload] = useState([]);

    const handleSchoolsFileChange = async (event) => {
        const file = event.target.files[0];

        const workbook = await readFile(file);
        const sheetNames = workbook.SheetNames;
        let sheetsData = [];

        sheetNames.forEach((sheetName) => {
            const sheet = workbook.Sheets[sheetName];
            const data = utils.sheet_to_json(sheet, {header: 1});
            const courses = data.slice(1);

            let formattedCourses = [];

            courses.forEach((course) => {
                formattedCourses.push({"courseCode": sheetName + course[0], "courseName": course[1]})
            });

            sheetsData.push({"schoolCode": sheetName, "schoolName": data[0][0], "courses": formattedCourses});
        });

        setSchoolsUpload(sheetsData);
    };

    const [leadersUpload, setLeadersUpload] = useState([]);

    const handleLeadersFileChange = async (event) => {
        const file = event.target.files[0];

        const workbook = await readFile(file);
        const sheetNames = workbook.SheetNames;
        let sheetsData = [];

        sheetNames.forEach((sheetName) => {
            const sheet = workbook.Sheets[sheetName];
            const data = utils.sheet_to_json(sheet, {header: 1});

            data.forEach((leader) => {
                sheetsData.push(leader[0])
            });
        });

        if (setupMode === true)
        {
            setLeadersUpload(sheetsData);
        }

        if (viewEditMode === true)
        {
            console.log(sheetsData);
            setLeaderIDsToAdd([leaderIDsToAdd, ...sheetsData]);
        }
    };

    const readFile = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();

            reader.onload = (e) => {
                const data = new Uint8Array(e.target.result);
                const workbook = read(data, {type: 'array'});
                resolve(workbook);
            };

            reader.onerror = (error) => {
                reject(error);
            };

            reader.readAsArrayBuffer(file);
        });
    };

    const [getWidth, setGetWidth] = useState("xl");

    useEffect(() => {
        if (setupMode) {
            setGetWidth("lg")
        }
        if (viewEditMode) {
            setGetWidth("xl")
        }
    }, [setupMode, viewEditMode])



    // update dates dialog
    const [updateDateShow, setUpdateDateShow] = useState(false);

    function handleUpdateDateConfirm()
    {
        alert("call api");

        setUpdateDateShow(false);
        // show errors etc based on return
    }


    const [leaderIDsToAdd, setLeaderIDsToAdd] = useState([]);

    const [assignLeadersShow, setAssignLeadersShow] = useState(false);

    function handleAssignLeadersConfirm()
    {
        alert("call api");

        setAssignLeadersShow(false);
        // show errors etc based on return
    }

    const [enableSysShow, setEnableSysShow] = useState(false);

    function handleEnableSysConfirm()
    {
        alert("call api");

        setEnableSysShow(false);
    }

    const [disableSysShow, setDisableSysShow] = useState(false);

    function handleDisableSysConfirm()
    {
        alert("call api");

        setDisableSysShow(false);
    }

    const [enableBookingShow, setEnableBookingShow] = useState(false);

    function handleEnableBookingConfirm()
    {
        alert("call api");

        setEnableBookingShow(false);
    }

    const [disableBookingShow, setDisableBookingShow] = useState(false);

    function handleDisableBookingConfirm()
    {
        alert("call api");

        setDisableBookingShow(false);
    }

    const [resetSysShow, setResetSysShow] = useState(false);

    function handleResetSysConfirm()
    {
        alert("call api");

        setResetSysShow(false);
    }


    const VisuallyHiddenInput = styled('input')({
        clip: 'rect(0 0 0 0)',
        clipPath: 'inset(50%)',
        height: 1,
        overflow: 'hidden',
        position: 'absolute',
        bottom: 0,
        left: 0,
        whiteSpace: 'nowrap',
        width: 1,
    });


    const getChartOptions = (color) => {
        return {
            chart: {
                toolbar: {
                    show: false
                },
                sparkline: {
                    enabled: true
                }
            },
            stroke: {
                width: 3,
                curve: 'smooth',
                lineCap: 'round',
                colors: [color] // Add your custom color here
            },
            fill: {
                colors: [color],
                opacity: 1,
                gradient: {
                    type: 'vertical',
                    shadeIntensity: 0.5,
                    opacityFrom: 0.7,
                    opacityTo: 0,
                    stops: [0, 100],
                }
            },
            tooltip: {
                enabled: false
            },
            markers: {
                size: 0
            },
            xaxis: {
                labels: {
                    show: false
                },
                axisBorder: {
                    show: false
                }
            },
            yaxis: {
                labels: {
                    show: false
                },
                axisBorder: {
                    show: false
                }
            },
            grid: {
                show: false
            }
        };
    };


    const chartSeries = [
        {
            name: 'Series',
            data: [30, 40, 35, 50, 49, 60, 70, 91, 125, 68]
        }
    ];


    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (


        <Container maxWidth={getWidth}>

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>


            {/* setup elements */}
            {
                setupMode &&
                <>

                    {/* top bar */}
                    <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                        <Typography variant="h4">Management Setup </Typography>
                        <div>
                            <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                                    disabled={shownSection === 1} onClick={prevSection} sx={{m: 1}}>
                                Back
                            </Button>
                            <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                                    onClick={nextSection}>
                                {shownSection === 5 ? (
                                    <>Submit</>
                                ) : (
                                    <>Next</>
                                )}
                            </Button>
                        </div>
                    </Stack>

                    {/* progress bar */}
                    <Box sx={{width: '100%', mb: 2}}>
                        <LinearProgress variant="determinate" value={progPercent} style={{borderRadius: 5, height: 10}}/>
                    </Box>


                    {/* setup midterm dates card */}
                    {
                        shownSection === 1 &&
                        <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Midterm Exams Information:</Typography>
                                <FormHelperText sx={{mb: 3}}>Dates for midterm break & exam week, students will not be allowed to book session during this time.</FormHelperText>

                                <Typography variant="button">Revision Week:</Typography>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Midterm Revision Week Start" value={midRevWeekStart} onChange={(newValue) => {
                                        setMidRevWeekStart(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the midterm revision Week Starts.</FormHelperText>

                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Midterm Revision Week End" minDate={midRevWeekStart} value={midRevWeekEnd} onChange={(newValue) => {
                                        setMidRevWeekEnd(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText sx={{mb: 3}}>When does the midterm revision Week Ends.</FormHelperText>

                                <Typography variant="button">Exam Week:</Typography>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Midterm Week Start" minDate={midRevWeekEnd} value={midWeekStart} onChange={(newValue) => {
                                        setMidWeekStart(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the midterm exam Week Start.</FormHelperText>

                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Midterm Week End" minDate={midWeekStart} value={midWeekEnd} onChange={(newValue) => {
                                        setMidWeekEnd(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the midterm exam Week End.</FormHelperText>
                            </div>
                        </Card>
                    }

                    {/* setup final dates card */}
                    {
                        shownSection === 2 &&
                        <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Final Exams Information:</Typography>
                                <FormHelperText sx={{mb: 3}}>Dates for final break & exam week, students will not be allowed to book session during this time. break time should be configured for at least a week before exam week if no break is
                                    present</FormHelperText>

                                <Typography variant="button">Revision / Break Week:</Typography>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Final Break Week Start" minDate={midWeekEnd} value={finRevWeekStart} onChange={(newValue) => {
                                        setFinRevWeekStart(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the final revision / break Week Starts.</FormHelperText>

                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Final Break Week End" minDate={finRevWeekStart} value={finRevWeekEnd} onChange={(newValue) => {
                                        setFinRevWeekEnd(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText sx={{mb: 3}}>When does the final revision / break Week Ends.</FormHelperText>

                                <Typography variant="button">Exam Week:</Typography>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Final Week Start" minDate={finRevWeekEnd} value={finWeekStart} onChange={(newValue) => {
                                        setFinWeekStart(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the midterm exam Week Start.</FormHelperText>

                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker sx={{width: "100%", mt: 1}} format={"DD/MM/YYYY"} label="Final Week End" minDate={finWeekStart} value={finWeekEnd} onChange={(newValue) => {
                                        setFinWeekEnd(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>When does the midterm exam Week End.</FormHelperText>
                            </div>
                        </Card>
                    }

                    {/* upload courses card */}
                    {
                        shownSection === 3 &&
                        <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Upload Schools File:</Typography>
                                <FormHelperText sx={{mb: 3}}>Use template to upload schools and courses information, overview will be shown below.</FormHelperText>

                                <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
                                    Upload file
                                    <VisuallyHiddenInput type="file" onChange={handleSchoolsFileChange} accept=".xlsx"/>
                                </Button>

                                {
                                    Object.keys(schoolsUpload).length !== 0 &&
                                    <>
                                        {schoolsUpload.map((school) => (
                                            <TableContainer component={CustomPaper} sx={{mt: 3}}>
                                                <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                                    <TableHead>
                                                        <TableRow>
                                                            <TableCell colspan="2" style={{"text-align": "center"}}><b>{school.schoolName}</b></TableCell>
                                                        </TableRow>
                                                        <TableRow>
                                                            <TableCell align="center">Course Code</TableCell>
                                                            <TableCell align="center">Course Name</TableCell>
                                                        </TableRow>
                                                    </TableHead>
                                                    <TableBody>
                                                        {
                                                            school.courses.map((course, index) => (
                                                                <TableRow
                                                                    key={index}
                                                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                                >
                                                                    <TableCell align="center" component="th" scope="row">
                                                                        {course.courseCode}
                                                                    </TableCell>
                                                                    <TableCell>{course.courseName}</TableCell>
                                                                </TableRow>
                                                            ))
                                                        }
                                                    </TableBody>
                                                </Table>
                                            </TableContainer>
                                        ))}
                                    </>
                                }

                            </div>
                        </Card>
                    }

                    {/* upload leaders card */}
                    {
                        shownSection === 4 &&
                        <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Upload Leaders File:</Typography>
                                <FormHelperText sx={{mb: 3}}>Use template to upload students to be made into leaders, overview will be shown below.</FormHelperText>

                                <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
                                    Upload file
                                    <VisuallyHiddenInput type="file" onChange={handleLeadersFileChange} accept=".xlsx"/>
                                </Button>

                                {
                                    leadersUpload.length > 0 ? (
                                        <>
                                            <FormHelperText sx={{mt: 2}}>Leaders</FormHelperText>
                                            <List dense>
                                                {
                                                    leadersUpload && leadersUpload.map((studentID) => (
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

                            </div>
                        </Card>
                    }

                    {/* overview card */}
                    {
                        shownSection === 5 &&
                        <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Overview:</Typography>
                                <FormHelperText>Please validate the setup before submitting.</FormHelperText>

                                <TextField label="Mid Rev Week Start" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(midRevWeekStart).format("DD/MM/YYYY")}/>
                                <TextField label="Mid Rev Week End" variant="standard" fullWidth sx={{mb: 1, mt: 1}} InputProps={{readOnly: true}} defaultValue={moment(midRevWeekEnd).format("DD/MM/YYYY")}/>

                                <TextField label="Mid Week Start" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(midWeekStart).format("DD/MM/YYYY")}/>
                                <TextField label="Mid Week End" variant="standard" fullWidth sx={{mb: 1, mt: 1}} InputProps={{readOnly: true}} defaultValue={moment(midWeekEnd).format("DD/MM/YYYY")}/>

                                <TextField label="Final Break Week Start" variant="standard" fullWidth sx={{mb: 1, mt: 3}} InputProps={{readOnly: true}} defaultValue={moment(finRevWeekStart).format("DD/MM/YYYY")}/>
                                <TextField label="Final Break Week End" variant="standard" fullWidth sx={{mb: 1, mt: 1}} InputProps={{readOnly: true}} defaultValue={moment(finRevWeekEnd).format("DD/MM/YYYY")}/>

                                <TextField label="Final Week Start" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(finWeekStart).format("DD/MM/YYYY")}/>
                                <TextField label="Final Week End" variant="standard" fullWidth sx={{mb: 1, mt: 1}} InputProps={{readOnly: true}} defaultValue={moment(finWeekEnd).format("DD/MM/YYYY")}/>


                                {
                                    Object.keys(schoolsUpload).length !== 0 &&
                                    <>
                                        <FormHelperText sx={{mt: 2}}>Schools & Courses</FormHelperText>
                                        {schoolsUpload.map((school) => (
                                            <TableContainer component={CustomPaper} sx={{mb: 3}}>
                                                <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                                    <TableHead>
                                                        <TableRow>
                                                            <TableCell colspan="2" style={{"text-align": "center"}}><b>{school.schoolName}</b></TableCell>
                                                        </TableRow>
                                                        <TableRow>
                                                            <TableCell align="center">Course Code</TableCell>
                                                            <TableCell align="center">Course Name</TableCell>
                                                        </TableRow>
                                                    </TableHead>
                                                    <TableBody>
                                                        {
                                                            school.courses.map((course, index) => (
                                                                <TableRow
                                                                    key={index}
                                                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                                >
                                                                    <TableCell align="center" component="th" scope="row">
                                                                        {course.courseCode}
                                                                    </TableCell>
                                                                    <TableCell>{course.courseName}</TableCell>
                                                                </TableRow>
                                                            ))
                                                        }
                                                    </TableBody>
                                                </Table>
                                            </TableContainer>
                                        ))}
                                    </>
                                }

                                {
                                    leadersUpload.length > 0 ? (
                                        <>
                                            <FormHelperText sx={{mt: 2}}>Leaders</FormHelperText>
                                            <List dense>
                                                {
                                                    leadersUpload && leadersUpload.map((studentID) => (
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

                            </div>
                        </Card>
                    }
                </>
            }

            {/* view edit elements  */}
            {
                viewEditMode &&
                <>
                    <Typography variant="h4" sx={{mb: 5}}>
                        Management
                    </Typography>


                    <Grid container spacing={3}>


                        {/* top stats */}
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>

                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Card spacing={3} direction="row" sx={{borderRadius: 2, position: 'relative'}}>
                                <ApexChart
                                    options={getChartOptions("#c03e3e")}
                                    series={chartSeries}
                                    type="area"
                                    height={150}
                                    width="100%"
                                />
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: 0,
                                        left: 0,
                                        width: '100%',
                                        height: '100%',
                                        background: 'rgba(0, 0, 0, 0.1)',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        zIndex: 1
                                    }}
                                >
                                    <Box sx={{width: 64, height: 64}}>sdf</Box>

                                    <Stack spacing={0.5}>
                                        <Typography variant="h4">num</Typography>

                                        <Typography variant="subtitle2" sx={{color: 'text.disabled'}}>
                                            title
                                        </Typography>
                                    </Stack>
                                </div>
                            </Card>
                        </Grid>

                        {/* mid dates card */}
                        <Grid xs={12} md={6} lg={4}>
                            <Card
                                component={Stack}
                                spacing={3}
                                direction="row"
                                sx={{borderRadius: 2}}
                            >

                                <CardContent>
                                    <Typography variant="h6" sx={{mb: 2}}>
                                        Midterm Dates:
                                    </Typography>

                                    <Typography variant="button">Revision Week:</Typography>
                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Midterm Revision Week Start" value={midRevWeekStartEdit} onChange={(newValue) => {
                                            setMidRevWeekStartEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2, mb: 3}} format={"DD/MM/YYYY"} label="Midterm Revision Week End" minDate={midRevWeekStartEdit} value={midRevWeekEndEdit} onChange={(newValue) => {
                                            setMidRevWeekEndEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <Typography variant="button">Exam Week:</Typography>
                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Midterm Week Start" minDate={midRevWeekEndEdit} value={midWeekStartEdit} onChange={(newValue) => {
                                            setMidWeekStartEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Midterm Week End" minDate={midWeekStartEdit} value={midWeekEndEdit} onChange={(newValue) => {
                                            setMidWeekEndEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <Button sx={{mt: 2}} variant={"contained"}
                                            disabled={midRevWeekStartEdit === midRevWeekStart && midRevWeekEndEdit === midRevWeekEnd && midWeekStart === midWeekStartEdit && midWeekEnd === midWeekEndEdit} onClick={() => {setUpdateDateShow(true)}}>Update</Button>

                                    <Button sx={{mt: 2, ml:1}} variant={"contained"} color={"warning"}
                                            disabled={midRevWeekStartEdit === midRevWeekStart && midRevWeekEndEdit === midRevWeekEnd && midWeekStart === midWeekStartEdit && midWeekEnd === midWeekEndEdit}
                                            onClick={() => {setMidRevWeekStartEdit(midRevWeekStart); setMidRevWeekEndEdit(midRevWeekEnd); setMidWeekStartEdit(midWeekStart); setMidWeekEndEdit(midWeekEnd)}}>Rest</Button>
                                </CardContent>

                            </Card>
                        </Grid>

                        {/* final dates card */}
                        <Grid xs={12} md={6} lg={4}>
                            <Card
                                component={Stack}
                                spacing={3}
                                direction="row"
                                sx={{borderRadius: 2}}
                            >

                                <CardContent>
                                    <Typography variant="h6" sx={{mb: 2}}>
                                        Finals Dates:
                                    </Typography>

                                    <Typography variant="button">Break Week:</Typography>
                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Final Break Week Start" value={finRevWeekStartEdit} onChange={(newValue) => {
                                            setFinRevWeekStartEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2, mb: 3}} format={"DD/MM/YYYY"} label="Final Break  Week End" minDate={finRevWeekStartEdit} value={finRevWeekEndEdit} onChange={(newValue) => {
                                            setFinRevWeekEndEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <Typography variant="button">Exam Week:</Typography>
                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Final Week Start" minDate={finRevWeekEndEdit} value={finWeekStartEdit} onChange={(newValue) => {
                                            setFinWeekStartEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <LocalizationProvider dateAdapter={AdapterMoment}>
                                        <DatePicker sx={{width: "100%", mt: 2}} format={"DD/MM/YYYY"} label="Final Week End" minDate={finWeekStartEdit} value={finWeekEndEdit} onChange={(newValue) => {
                                            setFinWeekEndEdit(newValue)
                                        }}/>
                                    </LocalizationProvider>

                                    <Button sx={{mt: 2}} variant={"contained"}
                                            disabled={finRevWeekStartEdit === finRevWeekStart && finRevWeekEndEdit === finRevWeekEnd && finWeekStart === finWeekStartEdit && finWeekEnd === finWeekEndEdit} onClick={() => {setUpdateDateShow(true)}}>Update</Button>

                                    <Button sx={{mt: 2, ml:1}} variant={"contained"} color={"warning"}
                                            disabled={finRevWeekStartEdit === finRevWeekStart && finRevWeekEndEdit === finRevWeekEnd && finWeekStart === finWeekStartEdit && finWeekEnd === finWeekEndEdit}
                                            onClick={() => {setFinRevWeekStartEdit(finRevWeekStart); setFinRevWeekEndEdit(finRevWeekEnd); setFinWeekStartEdit(finWeekStart); setFinWeekEndEdit(finWeekEnd)}}>Rest</Button>

                                </CardContent>

                            </Card>
                        </Grid>

                        {/* buttons card */}
                        <Grid xs={12} md={6} lg={4}>
                            <Card
                                component={Stack}
                                spacing={3}
                                direction="row"
                                sx={{borderRadius: 2}}
                            >

                                <CardContent sx={{width: "100%"}}>
                                    <Typography variant="h6" sx={{mb: 2}}>
                                        System Settings:
                                    </Typography>

                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth color={"success"} onClick={() => {setAssignLeadersShow(true)}}>Assign Leader/s</Button>
                                    <FormHelperText>Assign student id's as pass leaders.</FormHelperText>

                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth onClick={() => {setEnableSysShow(true)}}>Enable System</Button>
                                    <FormHelperText>Make the system enabled for leaders and tutors.</FormHelperText>

                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth color={"warning"} onClick={() => {setDisableSysShow(true)}}>Disable System</Button>
                                    <FormHelperText>Disable all users (other than you).</FormHelperText>

                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth onClick={() => {setEnableBookingShow(true)}}>Enable Booking</Button>
                                    <FormHelperText>Allow Bookings for students.</FormHelperText>

                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth color={"warning"} onClick={() => {setDisableBookingShow(true)}}>Disable Booking</Button>
                                    <FormHelperText>Stop new Bookings.</FormHelperText>


                                    <Button sx={{mt: 2}} variant={"contained"} fullWidth color={"error"} onClick={() => {setResetSysShow(true)}}>Reset System</Button>
                                    <FormHelperText>Clear all information and data (start new semester).</FormHelperText>
                                </CardContent>

                            </Card>
                        </Grid>

                    </Grid>
                </>
            }

            {/* confirm dialog - dates */}
            <Dialog
                open={updateDateShow}
                onClose={() => {setUpdateDateShow(false)}}
            >
                <DialogTitle>
                    Update Dates
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Are you sure you want to update the dates?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setUpdateDateShow(false)}}>Cancel</Button>
                    <Button onClick={handleUpdateDateConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>


            {/* add dialog - leaders */}
            <Dialog
                open={assignLeadersShow}
                onClose={() => {setAssignLeadersShow(false)}}
            >
                <DialogTitle>
                    Assign Leaders
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Add student IDs to make leader accounts for them
                    </DialogContentText>

                    <Autocomplete
                        PaperComponent={CustomPaper}
                        multiple
                        freeSolo
                        sx={{width: '100%', mt: 3}}
                        options={[]}
                        value={leaderIDsToAdd}
                        onChange={(event, newValue) => {setLeaderIDsToAdd(newValue)}}
                        renderInput={(params) => <TextField {...params} label="Student ID/s"/>}
                        />
                    <FormHelperText sx={{ mb: 2 }}>Type student ID and press enter.</FormHelperText>

                    <DialogContentText>
                        Or upload leaders sheet:
                    </DialogContentText>

                    <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth sx={{ mt:2 }}>
                        Upload file
                        <VisuallyHiddenInput type="file" onChange={handleLeadersFileChange} accept=".xlsx"/>
                    </Button>

                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setAssignLeadersShow(false)}}>Cancel</Button>
                    <Button onClick={handleAssignLeadersConfirm} autoFocus color={"error"}>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>


            {/* confirm dialog - enable system */}
            <Dialog
                open={enableSysShow}
                onClose={() => {setEnableSysShow(false)}}
            >
                <DialogTitle>
                    Enable System
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        This will allow Tutors, and leaders to setup and use the system, are you sure you want to continue?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setEnableSysShow(false)}}>Cancel</Button>
                    <Button onClick={handleEnableSysConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>

            {/* confirm dialog - disable system */}
            <Dialog
                open={disableSysShow}
                onClose={() => {setDisableSysShow(false)}}
            >
                <DialogTitle>
                    Disable System
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        This will stop all users from being able to modify any aspect of the system, sure to continue?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setDisableSysShow(false)}}>Cancel</Button>
                    <Button onClick={handleDisableSysConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>


            {/* confirm dialog - enable booking */}
            <Dialog
                open={enableBookingShow}
                onClose={() => {setEnableBookingShow(false)}}
            >
                <DialogTitle>
                    Enable Booking
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        This will allow student to access and book sessions, sure to confirm?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setEnableBookingShow(false)}}>Cancel</Button>
                    <Button onClick={handleEnableBookingConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>

            {/* confirm dialog - disable booking */}
            <Dialog
                open={disableBookingShow}
                onClose={() => {setDisableBookingShow(false)}}
            >
                <DialogTitle>
                    disable Booking
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        This will stop all users from being able to create new bookings, sure to continue?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setDisableBookingShow(false)}}>Cancel</Button>
                    <Button onClick={handleDisableBookingConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>


            {/* confirm dialog - reset system */}
            <Dialog
                open={resetSysShow}
                onClose={() => {setResetSysShow(false)}}
            >
                <DialogTitle>
                    Reset System
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        This will delete all records, including bookings, leaders, revisions, statistics, and courses.
                        after this step you would be required to setup the new courses, leaders, and information for the system before enabling it.
                    </DialogContentText>
                    <FormHelperText>** this process may take some time **</FormHelperText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {setResetSysShow(false)}}>Cancel</Button>
                    <Button onClick={handleResetSysConfirm} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>

        </Container>
    );
}
