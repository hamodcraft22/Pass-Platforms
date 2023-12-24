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
import moment from "moment";
import UserProfile from "../../components/auth/UserInfo";
import MenuItem from "@mui/material/MenuItem";

// ----------------------------------------------------------------------

export default function RecommendationsTableRow({recID, dateTime, note, recStatus, studentID, studentName, tutorID, tutorName, userRole}) {

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


    const [showEditDialog, setShowEditDialog] = useState(false);

    const [editStatus, setEditStatus] = useState(null);

    const handleEditClickOpen = () => {
        setShowEditDialog(true);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);
    };
    const handleEditSave = () => {
        setShowEditDialog(false);
        editSubmit();
    };


    // edit api
    // submit new recommendation
    function editSubmit()
    {
        const recommendationToSubmit = {"recid":recID,  "recStatus":{"statusid":editStatus}};

        submitEditRecommendation(recommendationToSubmit);
    }

    // add offered courses api
    async function submitEditRecommendation(recommendationToSubmit)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "PUT", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(recommendationToSubmit)};

            await fetch(`http://localhost:8080/api/recommendation`, requestOptions)
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
            setShowEditDialog(false);
        }
    }


    const [showDeleteDialog, setShowDeleteDialog] = useState(false);
    const handleDeleteClickOpen = () => {
        setShowDeleteDialog(true);
    };
    const handleDeleteClose = () => {
        setShowDeleteDialog(false);
    };
    const handleDeleteSave = () => {
        setShowDeleteDialog(false);
        deleteRecm();
    };

    // delete api
    async function deleteRecm()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`http://localhost:8080/api/recommendation/${recID}`, requestOptions)
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


    return (
        <>
            <TableRow>

                <TableCell></TableCell>

                <TableCell component="th" scope="row" padding="none">
                    <Typography variant="subtitle2" noWrap>
                        {studentID + " " + studentName}
                    </Typography>
                </TableCell>

                <TableCell>{moment(dateTime).format("hh:mm A - DD/MM/YYYY")}</TableCell>

                <TableCell><Label color={(recStatus === 'banned' && 'error') || 'success'}>{recStatus}</Label></TableCell>

                <TableCell align="right">
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={handleViewClickOpen}><InfoIcon fontSize={"small"}/></Button>
                    {
                        (userRole === "manager" || userRole === "admin") &&
                        <Button variant="contained" sx={{ml: 1}} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    }
                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                </TableCell>
            </TableRow>

            {/* view dialog */}
            <Dialog
                open={showViewDialog}
                onClose={handleViewClose}
            >
                <DialogTitle>
                    Recommendation Information
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="Student" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={studentID + " " + studentName}/>


                        {
                            (userRole === "admin" || userRole === "manager") &&
                            <TextField label="Tutor" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={tutorID + " " + tutorName}/>
                        }

                        <TextField label="Status" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={recStatus}/>
                        <TextField label="Date" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={dateTime && moment(dateTime).format("hh:mm A - DD/MM/YYYY")}/>
                        <TextField label="Note" variant="standard" fullWidth sx={{mb: 1, mt: 2}} multiline rows={2} InputProps={{readOnly: true}} defaultValue={note}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle>
                    Edit Recommendation
                </DialogTitle>
                <DialogContent>
                    <div style={{margin: "5px"}}>

                        {
                            errorShow &&

                            <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                                {errorMsg}
                            </Alert>
                        }

                        <TextField select label="Status" sx={{width: '100%', mt: 1}} value={editStatus} onChange={(event, newValue) => {setEditStatus(newValue.props.value)}}>
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
                    Delete Recommendation
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete the Recommendation for <b>{studentID + " " + studentName}</b>?
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

RecommendationsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
