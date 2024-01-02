import React, {useState} from 'react';
import PropTypes from 'prop-types';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Typography from '@mui/material/Typography';

import Label from '../../components/label';
import Button from "@mui/material/Button";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import {Alert, FormHelperText, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import {DateTime} from 'luxon';
import moment from "moment";
import PublicRoundedIcon from '@mui/icons-material/PublicRounded';
import GroupRoundedIcon from '@mui/icons-material/GroupRounded';
import {useNavigate} from "react-router-dom";
import UserProfile from "../../components/auth/UserInfo";
import MenuItem from "@mui/material/MenuItem";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";


// ----------------------------------------------------------------------

export default function BookingsTableRow({bookingID, subject, date, startTime, endTime, slotStartTime, slotEndTime, status, bookingType, online, viewType, userType})
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

    const [showEditDialog, setShowEditDialog] = useState(false);

    const [editStatus, setEditStatus] = useState(null);
    const [bookingStartTime, setBookingStartTime] = useState(null);
    const [bookingEndTime, setBookingEndTime] = useState(null);

    const handleEditClickOpen = () =>
    {
        setShowEditDialog(true);
    };
    const handleEditClose = () =>
    {
        setShowEditDialog(false);
    };
    const handleEditSave = () =>
    {
        if (editStatus !== null)
        {
            if (editStatus === 'F')
            {
                if (bookingStartTime !== null && bookingEndTime !== null)
                {
                    setShowEditDialog(false);
                    editSubmit();
                }
                else
                {
                    setErrorMsg("please fill in real start and end time");
                    setErrorShow(true);
                }
            }
            else
            {
                setShowEditDialog(false);
                editSubmit();
            }
        }
        else
        {
            setErrorMsg("please select status");
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
        deleteBooking()
    };

    // delete api - add
    async function deleteBooking()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/booking/${bookingID}`, requestOptions)
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


    // submit edit
    function editSubmit()
    {
        const bookingToSubmit = {"bookingid": bookingID, "bookingStatus": {"statusid": editStatus}, "starttime": bookingStartTime, "endtime": bookingEndTime};

        submitEditBooking(bookingToSubmit);
    }

    // add offered courses api
    async function submitEditBooking(bookingToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(bookingToSubmit)};

            await fetch(`https://URL_CHANGE_PLACEHOLDER/api/booking`, requestOptions)
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
                        console.log(response);
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
            setShowEditDialog(false);
        }
    }

    // go to courses
    let navigate = useNavigate();
    const goToBooking = () =>
    {
        let path = `/viewBooking?bookingID=${bookingID}`;
        navigate(path);
    }

    return (
        <>
            <TableRow>

                <TableCell></TableCell>

                <TableCell component="th" scope="row" padding="none">
                    <Typography variant="subtitle2" noWrap>
                        {subject}
                    </Typography>
                </TableCell>

                <TableCell>{DateTime.fromISO(date, {zone: 'Asia/Bahrain'}).toFormat('dd/MM/yyyy')}</TableCell>
                <TableCell>{startTime ? (moment(startTime).format("hh:mm A")) : (moment(slotStartTime).format("hh:mm A"))}</TableCell>
                <TableCell>{endTime ? (moment(endTime).format("hh:mm A")) : (moment(slotEndTime).format("hh:mm A"))}</TableCell>

                <TableCell><Label color={(status === 'ignored' && 'error') || (status === 'student cancelled' && 'info') || (status === 'cancelled' && 'warning') || (status === 'finished' && 'success') || 'primary'}>{status}</Label></TableCell>

                {/*type and online things */}
                <TableCell>
                    {
                        online && <PublicRoundedIcon/>
                    }
                    {
                        (bookingType === 'group' || bookingType === 'group unscheduled') &&
                        <GroupRoundedIcon/>
                    }
                </TableCell>

                <TableCell align="right">
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={goToBooking}><InfoIcon fontSize={"small"}/></Button>

                    {/* edit is not allowed for manager / admins */}

                    {
                        (userType !== 'manager' || userType !== 'admin') && viewType !== 'memberBookings' && status !== 'F' && status !== 'S' &&
                        <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    }

                    {/* should only show delete button for manager / admin */}
                    {
                        (userType === 'manager' || userType === 'admin') &&
                        <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                    }

                </TableCell>
            </TableRow>


            {/* Edit dialog - only change status*/}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Booking
                </DialogTitle>
                <DialogContent>
                    <div style={{margin: "5px"}}>

                        {
                            errorShow &&

                            <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                                {errorMsg}
                            </Alert>
                        }

                        {
                            viewType === 'leaderBookings' &&
                            <TextField select label="Status" sx={{width: '100%', mt: 1}} value={editStatus} onChange={(event, newValue) =>
                            {
                                setEditStatus(event.target.value)
                            }}>
                                <MenuItem value={'F'}>Finished</MenuItem>
                                <MenuItem value={'C'}>Cancelled</MenuItem>
                                <MenuItem value={'I'}>Ignored</MenuItem>
                            </TextField>
                        }

                        {
                            viewType === 'myBookings' &&
                            <TextField select label="Status" sx={{width: '100%', mt: 1}} value={editStatus} onChange={(event, newValue) =>
                            {
                                setEditStatus(event.target.value)
                            }}>
                                <MenuItem value={'S'}>Cancelled</MenuItem>
                            </TextField>
                        }
                        <FormHelperText>Please select the status for the booking</FormHelperText>

                        {
                            editStatus === 'F' &&
                            <>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <TimePicker sx={{mt: 1, mr: 1}} label="Start Time" value={bookingStartTime} onChange={(newValue) =>
                                    {
                                        setBookingStartTime(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <TimePicker sx={{mt: 1,}} label="End Time" minTime={bookingStartTime} value={bookingEndTime} onChange={(newValue) =>
                                    {
                                        setBookingEndTime(newValue)
                                    }}/>
                                </LocalizationProvider>
                                <FormHelperText>Select start and end time for the conducted session.</FormHelperText>
                            </>
                        }

                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleEditClose}>Cancel</Button>
                    <Button onClick={handleEditSave} autoFocus>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Delete dialog - only for managers? */}
            <Dialog
                open={showDeleteDialog}
                onClose={handleDeleteClose}
            >
                <DialogTitle>
                    Delete Recommendation
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete the Booking for <b>{subject}</b> on <b>{moment(date).format("DD/MM/YYY")}</b>, this process is irreversible?
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

BookingsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
