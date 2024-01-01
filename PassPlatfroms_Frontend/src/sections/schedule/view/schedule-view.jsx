import React, {useEffect, useState} from 'react';

import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import Container from '@mui/material/Container';
import TableBody from '@mui/material/TableBody';
import Typography from '@mui/material/Typography';
import TableContainer from '@mui/material/TableContainer';
import TablePagination from '@mui/material/TablePagination';

import Scrollbar from '../../../components/scrollbar';
import TableMainHead from '../../../components/table/table-head';
import TableEmptyRows from '../../../components/table/table-empty-rows';
import {emptyRows} from '../../../components/table/utils';

import ScheduleTableRow from '../schedule-table-row';
import ScheduleTableToolbar from '../schedule-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {Alert, Backdrop, CircularProgress, FormHelperText, Snackbar, TextField} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import DialogActions from "@mui/material/DialogActions";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import UserProfile from "../../../components/auth/UserInfo";
import ExportToExcel from "../../../utils/exportExcel";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import extractTextFromPdf from "../extractTextFromPdf";
import extractScheduleFromText from "../extractScheduleFromText";
import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";
import moment from "moment";

// ----------------------------------------------------------------------

export default function SchedulePage()
{

    // this page is open for everyone, editing is only for the manager / user

    const queryParameters = new URLSearchParams(window.location.search)
    const studentIDParm = queryParameters.get("studentID");

    const [loadingShow, setLoadingShow] = useState(false);

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

    const [successShow, setSuccessShow] = useState(false);
    const [successMsg, setSuccessMsg] = useState("");
    const handleSuccessAlertClose = (event, reason) =>
    {
        if (reason === 'clickaway')
        {
            return;
        }
        setSuccessShow(false);
    };


    // api related items

    const [userSchedules, setUserSchedules] = useState([]);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");


    async function getSchedules(studentID)
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`https://backend.zift.ddnsfree.com/api/schedule/student/${studentID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("No Schedules Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        parseUserSchedules(data.transObject);
                    }
                }).then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Schedules Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    function parseUserSchedules(retrivedUserSchedules)
    {
        let correctedUserSchedules = [];

        retrivedUserSchedules.forEach((schedule) =>
        {
            correctedUserSchedules.push({"scheduleid": schedule.scheduleid, "starttime": schedule.starttime, "endtime": schedule.endtime, "day": schedule.day.dayid, "userName": schedule.user.userName});
        });

        setUserSchedules(correctedUserSchedules);
    }

    // get user info (get the schedules of the logged in user)
    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        setUserID(userID);
        setUserRole(userRole);

        setCanEdit(true);

        getSchedules(userID);
    }


    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        if (studentIDParm !== null && studentIDParm !== undefined && Object.keys(studentIDParm).length !== 0)
        {
            getSchedules(studentIDParm);
            setCanEdit(false);
        }
        else
        {
            getUserInfo()
        }
    }, []);


    const [showAddDialog, setShowAddDialog] = useState(false);


    const [addScheduleDay, setAddScheduleDay] = useState(null);


    const [scheduleSelectedStartTime, setScheduleSelectedStartTime] = useState(null);
    const [scheduleSelectedEndTime, setScheduleSelectedEndTime] = useState(null);

    const [canEdit, setCanEdit] = useState(false);

    const handleAddClickOpen = () =>
    {
        setShowAddDialog(true);
    };
    const handleAddClose = () =>
    {
        setShowAddDialog(false);

        setScheduleSelectedStartTime(null);
        setScheduleSelectedEndTime(null);
        setAddScheduleDay(null);
    };
    const handleAddSave = () =>
    {
        if (addScheduleDay !== null && scheduleSelectedStartTime !== null && scheduleSelectedEndTime !== null)
        {
            setShowAddDialog(false);
            createSubmit();
        }
        else
        {
            setErrorMsg("please fill in all data");
            setErrorShow(true);
        }
    };

    const [showUploadDialog, setShowUploadDialog] = useState(false);
    const [uploadSchedules, setUploadSchedules] = useState([]);


    // submit multi schedule
    function createMultiSubmit()
    {
        let scheduleToSubmit = [];

        uploadSchedules.forEach((schedule) => {
            scheduleToSubmit.push({"starttime": getTimeMoment(schedule.start), "endtime": getTimeMoment(schedule.end), "day": {"dayid": schedule.day}, "user": {"userid": userID}});
        });

        submitMultiSchedule(scheduleToSubmit);
    }

    // add schedule api
    async function submitMultiSchedule(scheduleToSubmit)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(scheduleToSubmit)};

            await fetch(`https://backend.zift.ddnsfree.com/api/schedule/multi`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 400)
                    {
                        setErrorMsg("Schedule clashes with another schedule time");
                        setErrorShow(true);
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
                        // refresh transcript
                        setSuccessMsg("Schedules added, clashes (if any) ignored");
                        setSuccessShow(true);
                        getSchedules(userID);

                        // clear all the things in add
                        setUploadSchedules([]);
                    }
                });

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    const handleUploadClickOpen = () =>
    {
        setShowUploadDialog(true);
    };
    const handleUploadClose = () =>
    {
        setShowUploadDialog(false);

        setUploadSchedules([]);
    };
    const handleUplaodSave = () =>
    {
        if (uploadSchedules !== null && Object.keys(uploadSchedules).length !== 0)
        {
            setShowUploadDialog(false);
            createMultiSubmit();
        }
        else
        {
            setErrorMsg("no Schedules found");
            setErrorShow(true);
        }
    };

    function getTimeMoment(theTime)
    {
        // Get today's date
        const today = moment().format('YYYY-MM-DD');

        // Combine date and time into a Moment object
        // Log the resulting Moment object
        return moment(`${today} ${theTime}`, 'YYYY-MM-DD h:mm A')
    }


    // extract schedules
    const handleFileChange = async (e) => {
        const file = e.target.files[0];

        if (file) {
            try {
                const pdfText = await extractTextFromPdf(file);

                // Define your regular expression for course extraction
                const courseRegex = /\b([MTWRFSU])\s+(\d{1,2}:\d{2}\s+[AP]M)\s+-\s+(\d{1,2}:\d{2}\s+[AP]M)/gm;

                const extractedSchedules = extractScheduleFromText(pdfText, courseRegex);
                setUploadSchedules(extractedSchedules);
            } catch (error) {
                console.error(error.message);
            }
        }
    };


    // submit new schedule
    function createSubmit()
    {
        const scheduleToSubmit = {"starttime": scheduleSelectedStartTime, "endtime": scheduleSelectedEndTime, "day": {"dayid": addScheduleDay}, "user": {"userid": userID}};

        submitSchedule(scheduleToSubmit);
    }

    // add schedule api
    async function submitSchedule(scheduleToSubmit)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(scheduleToSubmit)};

            await fetch(`https://backend.zift.ddnsfree.com/api/schedule`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 400)
                    {
                        setErrorMsg("Schedule clashes with another schedule time");
                        setErrorShow(true);
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
                        // refresh transcript
                        setSuccessMsg("Schedules added");
                        setSuccessShow(true);
                        getSchedules(userID);

                        // clear all the things in add
                        setScheduleSelectedStartTime(null);
                        setScheduleSelectedEndTime(null);
                        setAddScheduleDay(null);
                    }
                });

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    const handleSort = (event, id) =>
    {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '')
        {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
    };

    const handleChangePage = (event, newPage) =>
    {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) =>
    {
        setPage(0);
        setRowsPerPage(parseInt(event.target.value, 10));
    };

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

    const CustomPaper = (props) =>
    {
        return <Paper elevation={8} {...props} />;
    };

    const dayWord = (dayChar) =>
    {
        switch (dayChar)
        {
            case 'U':
                return "Sunday";
            case 'M':
                return "Monday";
            case 'T':
                return "Tuesday";
            case 'W':
                return "Wednesday";
            case 'R':
                return "Thursday";
            case 'F':
                return "Friday";
            case 'S':
                return "Saturday";
            default:
                return "huh?"
        }
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
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            <Snackbar open={successShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleSuccessAlertClose} severity="success" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {successMsg}
                </Alert>
            </Snackbar>

            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">{userSchedules && Object.keys(userSchedules).length !== 0 && <>{userSchedules[0].userName}</>} Schedule</Typography>

                <div>
                    {
                        studentIDParm === null && (userRole === "leader" || userRole === "student") &&
                        <>
                            <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                                    onClick={handleAddClickOpen}>
                                Add Schedule
                            </Button>

                            <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} sx={{m: 1}}
                                    onClick={handleUploadClickOpen}>
                                Upload Schedule
                            </Button>
                        </>

                    }

                    <ExportToExcel data={userSchedules} filename="Schedule List"/>
                </div>
            </Stack>

            <Card>
                <ScheduleTableToolbar/>

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={userSchedules.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'day', label: 'Day'},
                                    {id: 'starttime', label: 'Start Time', align: 'center'},
                                    {id: 'endtime', label: 'EndT Time', align: 'center'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {userSchedules
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <ScheduleTableRow
                                            scheduleID={row.scheduleid}
                                            day={row.day}
                                            startTime={row.starttime}
                                            endTime={row.endtime}
                                            canEdit={canEdit}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, userSchedules.length)}
                                />

                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={userSchedules.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />


                {/* Add dialog */}
                <Dialog
                    open={showAddDialog}
                    onClose={handleAddClose}
                >
                    <DialogTitle>
                        Add New Schedule
                    </DialogTitle>
                    <DialogContent>

                        <TextField select label="Day" sx={{width: '100%', mt: 1}} value={addScheduleDay}
                                   onChange={(event, newValue) =>
                                   {
                                       setAddScheduleDay(newValue.props.value)
                                   }}>
                            <MenuItem value={'U'}>Sunday</MenuItem>
                            <MenuItem value={'M'}>Monday</MenuItem>
                            <MenuItem value={'T'}>Tuesday</MenuItem>
                            <MenuItem value={'W'}>Wednesday</MenuItem>
                            <MenuItem value={'R'}>Thursday</MenuItem>
                            <MenuItem value={'F'}>Friday</MenuItem>
                            <MenuItem value={'S'}>Saturday</MenuItem>
                        </TextField>
                        <FormHelperText>Which day is your session.</FormHelperText>

                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 2, mr: 1}} label="Start Time"
                                        value={scheduleSelectedStartTime} onChange={(newValue) =>
                            {
                                setScheduleSelectedStartTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 2}} label="End Time" minTime={scheduleSelectedStartTime}
                                        value={scheduleSelectedEndTime} onChange={(newValue) =>
                            {
                                setScheduleSelectedEndTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select start and end time for the scheduled session.</FormHelperText>

                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleAddClose}>Cancel</Button>
                        <Button onClick={handleAddSave} autoFocus>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>


                {/* Upload dialog */}
                <Dialog
                    open={showUploadDialog}
                    onClose={handleUploadClose}
                >
                    <DialogTitle>
                        Upload Schedule File
                    </DialogTitle>
                    <DialogContent>
                        <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
                            Upload file
                            <VisuallyHiddenInput type="file" onChange={handleFileChange} accept=".pdf"/>
                        </Button>


                        {
                            Object.keys(uploadSchedules).length !== 0 &&
                            <TableContainer component={CustomPaper} sx={{mt: 3}}>
                                <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Day</TableCell>
                                            <TableCell align="center">Start Time</TableCell>
                                            <TableCell align="center">End Time</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {uploadSchedules.map((schedule, index) => (
                                            <TableRow
                                                key={index}
                                                sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                            >
                                                <TableCell component="th" scope="row">
                                                    {dayWord(schedule.day)}
                                                </TableCell>
                                                <TableCell align="center">{schedule.start}</TableCell>
                                                <TableCell align="center">{schedule.end}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        }
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleUploadClose}>Cancel</Button>
                        <Button onClick={handleUplaodSave} autoFocus>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>
            </Card>
        </Container>
    );
}
