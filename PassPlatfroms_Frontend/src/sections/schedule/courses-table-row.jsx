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
import {FormHelperText, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import MenuItem from "@mui/material/MenuItem";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import moment from "moment";


// ----------------------------------------------------------------------

export default function CoursesTableRow({slotID, day, startTime, endTime, note, isOnline}) {
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

    const [slotStartTime, setSlotStartTime] = useState(moment({h: 8, m: 0}));
    const [slotEndTime, setSlotEndTime] = useState(moment({h: 22, m: 0}));

    const [editSlotNote, setEditSlotNote] = useState(null);

    const [slotSelectedStartTime, setSlotSelectedStartTime] = useState();
    const [slotSelectedEndTime, setSlotSelectedEndTime] = useState();

    const handleEditClickOpen = () => {
        setShowEditDialog(true);

        setEditSlotDay(day);
        setEditSlotOnline(isOnline);
        //setSlotSelectedStartTime(startTime);
        //setSlotSelectedEndTime(endTime);
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
        setShowEditDialog(false);
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
    };


    return (
        <>
            <TableRow hover tabIndex={-1}>

                <TableCell></TableCell>

                <TableCell>{dayWord(day)}</TableCell>

                <TableCell align={"center"}>{startTime}</TableCell>

                <TableCell align={"center"}>{endTime}</TableCell>

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
                        <TextField label="Day" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={day}/>
                        <TextField label="Start Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={startTime}/>
                        <TextField label="End Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={endTime}/>
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
                    <FormHelperText>You can only change the note and the online rule.</FormHelperText>


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

                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker sx={{mt: 2, mr: 1}} label="Start Time" minTime={slotStartTime} maxTime={slotEndTime} value={slotSelectedStartTime} onChange={(newValue) => {
                            setSlotSelectedStartTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker sx={{mt: 2}} label="End Time" minTime={slotStartTime} maxTime={slotEndTime} value={slotSelectedEndTime} onChange={(newValue) => {
                            setSlotSelectedEndTime(newValue)
                        }}/>
                    </LocalizationProvider>


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
                    <DialogContentText>
                        Are you sure you want to delete the Schedule slot
                        on <b>{dayWord(day)}</b> from <b>{startTime}</b> till <b>{endTime}</b>?
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

CoursesTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
