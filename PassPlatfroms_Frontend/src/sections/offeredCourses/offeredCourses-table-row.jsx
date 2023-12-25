import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";
import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import DialogActions from "@mui/material/DialogActions";
import {Alert, Snackbar} from "@mui/material";
import UserProfile from "../../components/auth/UserInfo";

// ----------------------------------------------------------------------

export default function OfferedCoursesTableRow({offerID, courseID, courseName, canDelete}) {

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };

    const [showDeleteDialog, setShowDeleteDialog] = useState(false);
    const handleDeleteClickOpen = () => {
        setShowDeleteDialog(true);
    };
    const handleDeleteClose = () => {
        setShowDeleteDialog(false);
    };
    const handleDeleteSave = () => {
        deleteCourse();
    };

    // delete api - add
    async function deleteCourse() {
        try {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`http://localhost:8080/api/offeredcourse/${offerID}`, requestOptions)
                .then(response => {if (response.status === 201 || response.status === 200){window.location.reload()}else{setErrorMsg("an unknown error occurred, please check console");setErrorShow(true);}})
        } catch (error)
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

    return (
        <>

            <TableRow hover tabIndex={-1}>

                <TableCell></TableCell>

                <TableCell>{courseID}</TableCell>

                <TableCell>{courseName}</TableCell>

                <TableCell align={"right"}>
                    {
                        canDelete &&
                        <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"}
                                onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                    }
                </TableCell>

            </TableRow>


            {/* Delete dialog */}
            <Dialog
                open={showDeleteDialog}
                onClose={handleDeleteClose}
            >
                <DialogTitle>
                    Delete Offered Course
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete <b>{courseName}</b>?
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

OfferedCoursesTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
};
