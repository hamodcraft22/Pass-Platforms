import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Alert, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import UserProfile from "../../components/auth/UserInfo";

// ----------------------------------------------------------------------

export default function CoursesTableRow({courseID, courseName, role})
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
    const [editCourseName, setEditCourseName] = useState(null);

    const handleEditClickOpen = () =>
    {
        setEditCourseName(courseName);

        setShowEditDialog(true);
    };
    const handleEditClose = () =>
    {
        setShowEditDialog(false);

        setEditCourseName(null);
    };
    const handleEditSave = () =>
    {
        if (editCourseName !== null && editCourseName !== undefined && Object.keys(editCourseName).length !== 0)
        {
            editCourse();
        }
        else
        {
            setErrorMsg("Please add school name");
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
        deleteCourse();
    };

    // edit schools api
    async function editCourse()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "PUT",
                    headers: {'Content-Type': 'application/json', 'Authorization': token},
                    body: JSON.stringify({"courseid": courseID, "coursename": editCourseName})
                };

            await fetch(`https://backend.zift.ddnsfree.com/api/course`, requestOptions)
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
                });
        }
        catch (error)
        {
            console.log(error)
        }
        finally
        {
            setShowEditDialog(false);
        }
    }

    // delete api - add
    async function deleteCourse()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`https://backend.zift.ddnsfree.com/api/course/${courseID}`, requestOptions)
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


    return (
        <>

            <TableRow hover tabIndex={-1}>

                <TableCell></TableCell>

                <TableCell>{courseID}</TableCell>

                <TableCell>{courseName}</TableCell>

                <TableCell align={"right"}>
                    {
                        (role === 'admin' || role === 'manager') &&
                        <>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"}
                                    onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"}
                                    onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                        </>
                    }
                </TableCell>

            </TableRow>


            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    {courseName}
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <TextField sx={{width: '100%', mt: 1}} label="Course Name" variant="outlined" value={editCourseName}
                               onChange={(newValue) => setEditCourseName(newValue.target.value)}/>

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
                    {courseName}
                </DialogTitle>
                <DialogContent>

                    <DialogContentText>
                        Are you sure you want to delete <b>{courseName}</b>? this will delete all bookings, and revisions
                        within this course.
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
    handleClick: PropTypes.func,
    courseName: PropTypes.any,
};
