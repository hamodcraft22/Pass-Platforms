import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";


import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Alert, FormHelperText, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import MenuItem from "@mui/material/MenuItem";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import moment from "moment/moment";
import UserProfile from "../../components/auth/UserInfo";


// ----------------------------------------------------------------------

export default function ScheduleTableRow({scheduleID, day, startTime, endTime, canEdit})
{
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


    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = () =>
    {
        setShowViewDialog(true);
    };
    const handleViewClose = () =>
    {
        setShowViewDialog(false);
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

    const [showEditDialog, setShowEditDialog] = useState(false);

    const [editScheduleDay, setEditScheduleDay] = useState(null);


    const [scheduleSelectedStartTime, setScheduleSelectedStartTime] = useState(null);
    const [scheduleSelectedEndTime, setScheduleSelectedEndTime] = useState(null);

    const handleEditClickOpen = () =>
    {
        setShowEditDialog(true);

        setEditScheduleDay(day);
        setScheduleSelectedStartTime(startTime);
        setScheduleSelectedEndTime(endTime);
    };
    const handleEditClose = () =>
    {
        setShowEditDialog(false);

        setEditScheduleDay(null);
        setScheduleSelectedStartTime(null);
        setScheduleSelectedEndTime(null);
    };
    const handleEditSave = () =>
    {
        if (editScheduleDay !== null && scheduleSelectedStartTime !== null && scheduleSelectedEndTime !== null)
        {
            setShowEditDialog(false);
            editSubmit();
        }
        else
        {
            setErrorMsg("please fill in all data");
            setErrorShow(true);
        }
    };


    const [showDeleteDialog, setShowDeleteDialog] = useState(false);
    const handleDeleteClickOpen = () =>
    {
        setShowDeleteDialog(true);
    };
    const handleDeleteClose = () =>
    {
        setShowDeleteDialog(false);
    };
    const handleDeleteSave = () =>
    {
        setShowDeleteDialog(false);
        deleteSchedule();
    };


    // delete api
    async function deleteSchedule()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/schedule/${scheduleID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        window.location.reload()
                    }
                    else
                    {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error)
        }
        finally
        {
            setShowDeleteDialog(false);
        }
    }

    // edit api
    // submit new schedule
    function editSubmit()
    {
        const scheduleToSubmit = {"scheduleid": scheduleID, "starttime": scheduleSelectedStartTime, "endtime": scheduleSelectedEndTime, "day": {"dayid": editScheduleDay}};

        submitEditSchedule(scheduleToSubmit);
    }

    // add offered courses api
    async function submitEditSchedule(scheduleToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(scheduleToSubmit)};

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/schedule`, requestOptions)
                .then((response) =>
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
                    else
                    {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                });

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
        }
        finally
        {
            setShowDeleteDialog(false);
        }
    }


    return (
        <>
            <TableRow hover tabIndex={-1}>

                <TableCell></TableCell>

                <TableCell>{dayWord(day)}</TableCell>

                <TableCell align={"center"}>{moment(startTime).format("hh:mm A")}</TableCell>

                <TableCell align={"center"}>{moment(endTime).format("hh:mm A")}</TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={handleViewClickOpen}><InfoIcon
                        fontSize={"small"}/></Button>
                    {
                        canEdit &&
                        <>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"}
                                    onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"}
                                    onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                        </>
                    }
                </TableCell>

            </TableRow>

            {/* view dialog */}
            <Dialog
                open={showViewDialog}
                onClose={handleViewClose}
            >
                <DialogTitle>
                    Schedule Information
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="Day" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={dayWord(day)}/>
                        <TextField label="Start Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(startTime).format("hh:mm A")}/>
                        <TextField label="End Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(endTime).format("hh:mm A")}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Schedule
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <TextField select label="Day" sx={{width: '100%', mt: 1}} value={editScheduleDay} onChange={(event, newValue) =>
                    {
                        setEditScheduleDay(newValue.props.value)
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
                        <TimePicker sx={{mt: 2, mr: 1}} label="Start Time" value={moment(scheduleSelectedStartTime)} onChange={(newValue) =>
                        {
                            setScheduleSelectedStartTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker sx={{mt: 2}} label="End Time" minTime={moment(scheduleSelectedStartTime)} value={moment(scheduleSelectedEndTime)} onChange={(newValue) =>
                        {
                            setScheduleSelectedEndTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <FormHelperText>Select start and end time for the scheduled session.</FormHelperText>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleEditClose}>Cancel</Button>
                    <Button onClick={handleEditSave} autoFocus>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Delete dialog */}
            <Dialog
                open={showDeleteDialog}
                onClose={handleDeleteClose}
            >
                <DialogTitle>
                    Delete Schedule
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete the Schedule schedule
                        on <b>{dayWord(day)}</b> from <b>{startTime && moment(startTime).format("hh:mm a")}</b> till <b>{endTime && moment(endTime).format("hh:mm a")}</b>?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteClose}>Cancel</Button>
                    <Button onClick={handleDeleteSave} autoFocus color={"error"}>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}

ScheduleTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
