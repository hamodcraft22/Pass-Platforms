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
import {Alert, Autocomplete, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import moment from "moment/moment";
import PublicRoundedIcon from "@mui/icons-material/PublicRounded";
import {useNavigate} from "react-router-dom";
import UserProfile from "../../components/auth/UserInfo";
import BackspaceRoundedIcon from '@mui/icons-material/BackspaceRounded';
import MenuItem from "@mui/material/MenuItem";


// ----------------------------------------------------------------------

export default function RevisionsTableRow({bookingID, subject, date, startTime, endTime, status, online, viewType, userType}) {

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };

    const [showEditDialog, setShowEditDialog] = useState(false);

    const [editStatus, setEditStatus] = useState(null);

    const handleEditClickOpen = () => {
        setShowEditDialog(true);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);
    };
    const handleEditSave = () => {
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
    const handleDeleteClickOpen = () => {
        setShowDeleteDialog(true);
    };
    const handleDeleteClose = () => {
        setShowDeleteDialog(false);
    };
    const handleDeleteSave = () => {
        setShowDeleteDialog(false);
        deleteRevision()
    };


    const [showDeregisterDialog, setShowDeregisterDialog] = useState(false);
    const handleDeregisterClickOpen = () => {
        setShowDeregisterDialog(true);
    };
    const handleDeregisterClose = () => {
        setShowDeregisterDialog(false);
    };
    const handleDeregisterSave = () => {
        setShowDeregisterDialog(false);
        deregisterRevision();
    };

    // submit edit
    function editSubmit()
    {
        const revisionToSubmit = {"bookingid":bookingID,  "bookingStatus":{"statusid":editStatus}};

        submitEditRevision(revisionToSubmit);
    }

    // add offered courses api
    async function submitEditRevision(revisionToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(revisionToSubmit)};

            await fetch(`http://localhost:8080/api/revision`, requestOptions)
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

    // delete api - add
    async function deleteRevision() {
        try {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`http://localhost:8080/api/revision/${bookingID}`, requestOptions)
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

    async function deregisterRevision() {
        try {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`http://localhost:8080/api/revision/${bookingID}/member`, requestOptions)
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

    // go to courses
    let navigate = useNavigate();
    const goToRevision = () => {
        let path = `/viewRevision?revisionID=${bookingID}`;
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

                <TableCell>{date}</TableCell>

                <TableCell>{moment(startTime).format("hh:mm A")}</TableCell>
                <TableCell>{moment(endTime).format("hh:mm A")}</TableCell>

                <TableCell><Label color={(status === 'cancelled' && 'warning') || (status === 'finished' && 'success') || 'primary'}>{status}</Label></TableCell>

                {/*type and online things */}
                <TableCell>
                    {
                        online && <PublicRoundedIcon/>
                    }
                </TableCell>

                <TableCell align="right">
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={() => {goToRevision()}}><InfoIcon fontSize={"small"}/></Button>
                    {
                        (userType === 'admin' || userType === 'manager' || viewType === 'leaderRevisions') &&
                        <>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                        </>
                    }

                    {/* derigster button - only for normal students */}
                    {
                        (viewType === 'myRevisions' && (userType === 'student' || userType === 'leader')) &&
                        <>
                            <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeregisterClickOpen}><BackspaceRoundedIcon fontSize={"small"}/></Button>
                        </>
                    }
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

                        {
                            errorShow &&

                            <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                                {errorMsg}
                            </Alert>
                        }

                        <TextField select label="Status" sx={{width: '100%', mt: 1}} value={editStatus} onChange={(event, newValue) => {setEditStatus(event.target.value)}}>
                            <MenuItem value={'F'}>Finished</MenuItem>
                            <MenuItem value={'C'}>Cancelled</MenuItem>
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

            {/* Delete dialog - only for managers */}
            <Dialog
                open={showDeleteDialog}
                onClose={handleDeleteClose}
            >
                <DialogTitle>
                    Delete Recommendation
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete the Revision for <b>{subject}</b> on <b>{moment(date).format("DD/MM/YYY")}</b>, this process is irreversible?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteClose}>Cancel</Button>
                    <Button onClick={handleDeleteSave} autoFocus color={"error"}>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>

            {/* deregister dialog - only for students */}
            <Dialog
                open={showDeregisterDialog}
                onClose={handleDeregisterClose}
            >
                <DialogTitle>
                    Deregister from Revision
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to Deregister from Revision for <b>{subject}</b> on <b>{moment(date).format("DD/MM/YYY")}</b>.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeregisterClose}>Cancel</Button>
                    <Button onClick={handleDeregisterSave} autoFocus color={"error"}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}

RevisionsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
