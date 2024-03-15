import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ListAltIcon from '@mui/icons-material/ListAlt';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Alert, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import UserProfile from "../../components/auth/UserInfo";
import SupervisedUserCircleIcon from '@mui/icons-material/SupervisedUserCircle';

// ----------------------------------------------------------------------

export default function SchoolsTableRow({schoolID, schoolName, role})
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
    const [editSchoolName, setEditSchoolName] = useState(null);
    const handleEditClickOpen = () =>
    {
        setEditSchoolName(schoolName);

        setShowEditDialog(true);
    };
    const handleEditClose = () =>
    {
        setShowEditDialog(false);

        setEditSchoolName(null);
    };
    const handleEditSave = () =>
    {
        if (editSchoolName !== null && editSchoolName !== undefined && Object.keys(editSchoolName).length !== 0)
        {
            editSchool();
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
        deleteSchool();
    };

    // go to courses
    let navigate = useNavigate();
    const goToCourse = () =>
    {
        let path = `/courses?schoolID=${schoolID}`;
        navigate(path);
    }

    const goToUsers = () =>
    {
        let path = `/user?schoolID=${schoolID}`;
        navigate(path);
    }

    // edit schools api
    async function editSchool()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "PUT",
                    headers: {'Content-Type': 'application/json', 'Authorization': token},
                    body: JSON.stringify({"schoolid": schoolID, "schoolname": editSchoolName})
                };

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/school`, requestOptions)
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
    async function deleteSchool()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/school/${schoolID}`, requestOptions)
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

                <TableCell>{schoolName}</TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"primary"}
                            onClick={goToUsers}><SupervisedUserCircleIcon fontSize={"small"}/></Button>

                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"success"}
                            onClick={goToCourse}><ListAltIcon fontSize={"small"}/></Button>

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
                    {schoolName}
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <TextField sx={{width: '100%', mt: 1}} label="School Name" variant="outlined" value={editSchoolName}
                               onChange={(newValue) => setEditSchoolName(newValue.target.value)}/>
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
