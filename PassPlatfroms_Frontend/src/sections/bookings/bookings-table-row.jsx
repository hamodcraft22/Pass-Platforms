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
import {Autocomplete, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import { DateTime } from 'luxon';


// ----------------------------------------------------------------------

export default function BookingsTableRow({key, subject, date, startTime, endTime, status}) {
    const [showEditDialog, setShowEditDialog] = useState(false);

    const handleEditClickOpen = () => {
        setShowEditDialog(true);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);
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
            <TableRow>

                <TableCell></TableCell>

                <TableCell component="th" scope="row" padding="none">
                    <Typography variant="subtitle2" noWrap>
                        {subject}
                    </Typography>
                </TableCell>

                <TableCell>{DateTime.fromISO(date, { zone: 'Asia/Bahrain' }).toFormat('dd/MM/yyyy')}</TableCell>
                <TableCell>{startTime}</TableCell>
                <TableCell>{endTime}</TableCell>

                <TableCell><Label color={(status === 'banned' && 'error') || 'success'}>{status}</Label></TableCell>

                <TableCell align="right">
                    <Button variant="contained" sx={{ml: 1}} size={"small"}><InfoIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                </TableCell>
            </TableRow>


            {/* Edit dialog - only change status*/}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Application
                </DialogTitle>
                <DialogContent>
                    <div style={{margin: "5px"}}>
                        <Autocomplete
                            options={[]}
                            sx={{width: 300}}
                            renderInput={(params) => <TextField {...params} label="Status"/>}
                        />
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
                        Are you sure you want to delete the Booking for <b>ADD STUDENT</b>?
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
