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
import {FormHelperText, TextField, ToggleButton} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import MenuItem from "@mui/material/MenuItem";
import PublicIcon from '@mui/icons-material/Public';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import DeskRoundedIcon from "@mui/icons-material/DeskRounded";


// ----------------------------------------------------------------------

export default function SlotTableRow({slotID, day, startTime, endTime, note, isOnline}) {
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

                <TableCell align={"center"}>{isOnline && <PublicIcon/>}</TableCell>

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
                        <TimePicker readOnly sx={{mt: 2, mr: 1}} label="Start Time" value={slotSelectedStartTime} onChange={(newValue) => {
                            setSlotSelectedStartTime(newValue)
                        }}/>
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <TimePicker readOnly sx={{mt: 2}} label="End Time" minTime={slotSelectedStartTime} value={slotSelectedEndTime} onChange={(newValue) => {
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
                    <DialogContentText>
                        Are you sure you want to delete the slot
                        on <b>{day}</b> from <b>{startTime}</b> till <b>{endTime}</b>?
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
