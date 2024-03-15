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
import {Alert, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import moment from "moment";
import {useNavigate} from "react-router-dom";
import UserProfile from "../../components/auth/UserInfo";
import MenuItem from "@mui/material/MenuItem";


// ----------------------------------------------------------------------

export default function ApplicationsTableRow({aplicID, studentID, student, date, status})
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
            setShowEditDialog(false);
            editSubmit();
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
        deleteApplication();
    };


    // delete api - add
    async function deleteApplication()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/application/${aplicID}`, requestOptions)
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


    // submit new application
    function editSubmit()
    {
        const applicationToSubmit = {"applicationid": aplicID, "applicationStatus": {"statusid": editStatus}};

        submitEditApplication(applicationToSubmit);
    }

    // add offered courses api
    async function submitEditApplication(applicationToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(applicationToSubmit)};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/application`, requestOptions)
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
    const goToView = () =>
    {
        let path = `/viewApplication?studentID=${studentID}`;
        navigate(path);
    }

    return (
        <>
            <TableRow>

                <TableCell></TableCell>

                <TableCell component="th" scope="row" padding="none">
                    <Typography variant="subtitle2" noWrap>
                        {student}
                    </Typography>
                </TableCell>

                <TableCell>{date && moment(date).format("hh:mm A | DD/MM/YYYY")}</TableCell>

                <TableCell><Label color={(status === 'rejected' && 'error') || (status === 'accepted' && 'success') || (status === 'interview' && 'info') || (status === 'reviewd' && 'secondary') || 'primary'}>{status}</Label></TableCell>

                <TableCell align="right">
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={goToView}><InfoIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                </TableCell>
            </TableRow>


            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Application
                </DialogTitle>
                <DialogContent>
                    <div style={{margin: "5px"}}>

                        {
                            errorShow &&

                            <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                                {errorMsg}
                            </Alert>
                        }

                        <TextField select label="Status" sx={{width: '100%', mt: 1}} value={editStatus} onChange={(event, newValue) =>
                        {
                            setEditStatus(newValue.props.value)
                        }}>
                            <MenuItem value={'R'}>Reviewed</MenuItem>
                            <MenuItem value={'I'}>Interviewed</MenuItem>
                            <MenuItem value={'A'}>Accepted</MenuItem>
                            <MenuItem value={'N'}>Rejected</MenuItem>
                        </TextField>

                    </div>
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
                    Delete Application
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete the Application for <b>{student}</b>? it will be fully deleted from the database.
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

ApplicationsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
