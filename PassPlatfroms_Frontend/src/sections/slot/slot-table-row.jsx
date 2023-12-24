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
import {Alert, FormHelperText, Snackbar, TextField, ToggleButton} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import MenuItem from "@mui/material/MenuItem";
import PublicIcon from '@mui/icons-material/Public';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import DeskRoundedIcon from "@mui/icons-material/DeskRounded";
import moment from "moment";
import UserProfile from "../../components/auth/UserInfo";


// ----------------------------------------------------------------------

export default function SlotTableRow({slotID, day, startTime, endTime, note, type}) {

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };

    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = () => {
        setShowViewDialog(true);
    };
    const handleViewClose = () => {
        setShowViewDialog(false);
    };

    const dayWord = (dayChar) => {
        switch (dayChar) {
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

    const [editSlotDay, setEditSlotDay] = useState(null);
    const [editSlotOnline, setEditSlotOnline] = useState(false);
    const [editSlotPhysical, setEditSlotPhysical] = useState(false);


    const [editSlotNote, setEditSlotNote] = useState(null);

    const [slotSelectedStartTime, setSlotSelectedStartTime] = useState(null);
    const [slotSelectedEndTime, setSlotSelectedEndTime] = useState(null);

    const handleEditClickOpen = () => {
        setShowEditDialog(true);

        setEditSlotDay(day);
        setSlotSelectedStartTime(startTime);
        setSlotSelectedEndTime(endTime);

        if (type === 'online')
        {
            setEditSlotOnline(true);
        }
        else if (type === 'physical')
        {
            setEditSlotPhysical(true);
        }
        else if (type === 'both')
        {
            setEditSlotOnline(true);
            setEditSlotPhysical(true);
        }

        setEditSlotNote(note);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);

        setEditSlotDay(null);
        setEditSlotOnline(null);
        setSlotSelectedStartTime(null);
        setSlotSelectedEndTime(null);
        setEditSlotNote(null);
    };
    const handleEditSave = () => {
        if ( editSlotOnline !== false || editSlotPhysical !== false )
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
    const handleDeleteClickOpen = () => {
        setShowDeleteDialog(true);
    };
    const handleDeleteClose = () => {
        setShowDeleteDialog(false);
    };
    const handleDeleteSave = () => {
        setShowDeleteDialog(false);
        deleteSlot();
    };


    // delete api
    async function deleteSlot()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`http://localhost:8080/api/slot/${slotID}`, requestOptions)
                .then(response => {if (response.status === 201 || response.status === 200){window.location.reload()}else{setErrorMsg("an unknown error occurred, please check console");setErrorShow(true);}})
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
    // submit new slot
    function editSubmit()
    {
        let slotType = '';

        if (editSlotOnline && editSlotPhysical)
        {
            slotType = 'B';
        }
        else if (editSlotOnline)
        {
            slotType = 'O'
        }
        else if (editSlotPhysical)
        {
            slotType = 'P'
        }

        const slotToSubmit = {"slotid":slotID, "note":editSlotNote, "slotType":{"typeid":slotType}};

        submitEditSlot(slotToSubmit);
    }

    // add offered courses api
    async function submitEditSlot(slotToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(slotToSubmit)};

            await fetch(`http://localhost:8080/api/slot`, requestOptions)
                .then((response) => {
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

                <TableCell align={"center"}>{type === 'online' && <PublicIcon/>} {type === 'physical' && <DeskRoundedIcon/>} {type === 'both' && <><PublicIcon/> <DeskRoundedIcon/> </>}  </TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={handleViewClickOpen}><InfoIcon
                        fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"}
                            onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"}
                            onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                </TableCell>

            </TableRow>

            {/* view dialog */}
            <Dialog
                open={showViewDialog}
                onClose={handleViewClose}
            >
                <DialogTitle>
                    Slot Information
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="Day" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={dayWord(day)}/>
                        <TextField label="Start Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(startTime).format("hh:mm A")}/>
                        <TextField label="End Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(endTime).format("hh:mm A")}/>
                        <TextField label="Note" variant="standard" fullWidth sx={{mb: 2}} InputProps={{readOnly: true}} multiline maxRows={2} defaultValue={note}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Slot
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <TextField InputProps={{readOnly: true}} select label="Day" sx={{mt: 1}} value={editSlotDay}
                               fullWidth>
                        <MenuItem value={'U'}>Sunday</MenuItem>
                        <MenuItem value={'M'}>Monday</MenuItem>
                        <MenuItem value={'T'}>Tuesday</MenuItem>
                        <MenuItem value={'W'}>Wednesday</MenuItem>
                        <MenuItem value={'R'}>Thursday</MenuItem>
                        <MenuItem value={'F'}>Friday</MenuItem>
                        <MenuItem value={'S'}>Saturday</MenuItem>
                    </TextField>
                    <FormHelperText>Day is none-editable</FormHelperText>

                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker readOnly sx={{mt: 2, mr: 1}} label="Start Time" value={moment(slotSelectedStartTime)} onChange={(newValue) => {
                            setSlotSelectedStartTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker readOnly sx={{mt: 2}} label="End Time"  value={moment(slotSelectedEndTime)} onChange={(newValue) => {
                            setSlotSelectedEndTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <FormHelperText>Times are none-editable</FormHelperText>

                    <TextField sx={{width: '100%', mt: 2}} label="Slot Note" variant="outlined" multiline rows={2} value={editSlotNote} onChange={(newValue) => setEditSlotNote(newValue.target.value)}/>

                    <FormHelperText sx={{ml: 2}}>Online</FormHelperText>
                    <ToggleButton
                        value={editSlotOnline}
                        selected={editSlotOnline}
                        sx={{width: '100%'}}
                        color={"primary"}
                        onChange={() => {
                            setEditSlotOnline(!editSlotOnline)
                        }}
                    >
                        <PublicIcon/>
                    </ToggleButton>

                    <FormHelperText sx={{ml: 2}}>Physical</FormHelperText>
                    <ToggleButton
                        value={editSlotPhysical}
                        selected={editSlotPhysical}
                        sx={{width: '100%'}}
                        color={"primary"}
                        onChange={() => {
                            setEditSlotPhysical(!editSlotPhysical)
                        }}
                    >
                        <DeskRoundedIcon/>
                    </ToggleButton>

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
                    Delete Slot
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <DialogContentText>
                        Are you sure you want to delete the slot
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

SlotTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
