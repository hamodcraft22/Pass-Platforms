import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";


import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ListAltIcon from '@mui/icons-material/ListAlt';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";

// ----------------------------------------------------------------------

export default function SchoolsTableRow({schoolID, schoolName, schoolDesc}) {
    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = () => {
        setShowViewDialog(true);
    };
    const handleViewClose = () => {
        setShowViewDialog(false);
    };


    const [showEditDialog, setShowEditDialog] = useState(false);
    const [editSchoolName, setEditSchoolName] = useState(null);
    const [editSchoolDesc, setEditSchoolDesc] = useState(null);
    const handleEditClickOpen = () => {
        setEditSchoolName(schoolName);
        setEditSchoolDesc(schoolDesc);

        setShowEditDialog(true);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);

        setEditSchoolName(null);
        setEditSchoolDesc(null);
    };
    const handleEditSave = () => {
        // add validation TODO
        editSchool();
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

    // go to courses
    let navigate = useNavigate();
    const goToCourse = () => {
        let path = `/courses?schoolID=${schoolID}`;
        navigate(path);
    }

    // edit schools api
    async function editSchool() {
        try {
            const requestOptions =
                {
                    method: "PUT",
                    headers: {'Content-Type': 'application/json', 'Authorization': '27afc256-2734-4a04-8c8e-49997bb0f638'},
                    body: JSON.stringify({"schoolid": schoolID, "schoolname": editSchoolName, "schooldesc": editSchoolDesc})
                };

            await fetch(`http://localhost:8080/api/school`, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then(() => {
                    window.location.reload(false)
                });
        } catch (error) {
            console.log(error)
        } finally {
            setShowEditDialog(false);

            setEditSchoolName(null);
            setEditSchoolDesc(null);
        }
    }

    // delete api - add

    return (
        <>
            <TableRow hover tabIndex={-1}>

                <TableCell></TableCell>

                <TableCell>{schoolName}</TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={handleViewClickOpen}><InfoIcon
                        fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"}
                            onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"success"}
                            onClick={goToCourse}><ListAltIcon fontSize={"small"}/></Button>
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
                    {schoolName}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {schoolDesc}
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    {schoolName}
                </DialogTitle>
                <DialogContent>
                    <TextField sx={{width: '100%', mt: 1}} label="School Name" variant="outlined" value={editSchoolName}
                               onChange={(newValue) => setEditSchoolName(newValue.target.value)}/>
                    <TextField sx={{width: '100%', mt: 1}} label="School Description" variant="outlined" multiline
                               rows={2} value={editSchoolDesc}
                               onChange={(newValue) => setEditSchoolDesc(newValue.target.value)}/>
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
                    {schoolName}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete <b>{schoolName}</b>? this will delete all courses, bookings, and
                        revisions within this school.
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

SchoolsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    schoolName: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
