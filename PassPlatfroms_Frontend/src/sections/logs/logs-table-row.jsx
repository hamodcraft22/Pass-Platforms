import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";


import InfoIcon from '@mui/icons-material/Info';
import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Alert, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import moment from "moment";
import UserProfile from "../../components/auth/UserInfo";


// ----------------------------------------------------------------------

export default function LogsTableRow({logID, user, dateTime, logErrorMsg})
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

    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = () =>
    {
        setShowViewDialog(true);
    };
    const handleViewClose = () =>
    {
        setShowViewDialog(false);
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
        deleteLog();
    };


    // delete api
    async function deleteLog()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions =
                {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json', 'Authorization': token}
                };

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/log/${logID}`, requestOptions)
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

                <TableCell>{user}</TableCell>

                <TableCell align={"center"}>{moment(dateTime).format("hh:mm A | DD/MM/YYYY")}</TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ml: 1}} size={"small"} onClick={handleViewClickOpen}><InfoIcon
                        fontSize={"small"}/></Button>

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
                    Log Information
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="User" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={user}/>
                        <TextField label="Date & Time" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(dateTime).format("hh:mm A | DD/MM/YYYY")}/>
                        <TextField label="Error" variant="standard" fullWidth sx={{mb: 1, mt: 2}} InputProps={{readOnly: true}} defaultValue={logErrorMsg}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Delete dialog */}
            <Dialog
                open={showDeleteDialog}
                onClose={handleDeleteClose}
            >
                <DialogTitle>
                    Delete Log
                </DialogTitle>
                <DialogContent>

                    {
                        errorShow &&

                        <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                            {errorMsg}
                        </Alert>
                    }

                    <DialogContentText>
                        Are you sure you want to delete the log?
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

LogsTableRow.propTypes = {
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
