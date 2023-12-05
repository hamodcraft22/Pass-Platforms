import React, {useState} from 'react';
import PropTypes from 'prop-types';

import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";


import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ListAltIcon from '@mui/icons-material/ListAlt';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {FormHelperText, InputLabel, Select, TextField, ToggleButton} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import moment from "moment/moment";
import MenuItem from "@mui/material/MenuItem";

// ----------------------------------------------------------------------

export default function CoursesTableRow({courseID, name, desc, sem, avlb})
{
    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = () => {
        setShowViewDialog(true);
    };
    const handleViewClose = () => {
        setShowViewDialog(false);
    };


    const [showEditDialog, setShowEditDialog] = useState(false);
    const [editCourseName, setEditCourseName] = useState(null);
    const [editCourseDesc, setEditCourseDesc] = useState(null);
    const [editCourseSem, setEditCourseSem] = useState(null);
    const [editCourseAvalb, setEditCourseAvalb] = useState(true);
    const handleEditClickOpen = () => {
        setEditCourseName(name);
        setEditCourseDesc(desc);
        setEditCourseSem(sem);
        setEditCourseAvalb(avlb);

        setShowEditDialog(true);
    };
    const handleEditClose = () => {
        setShowEditDialog(false);

        setEditCourseName(null);
        setEditCourseDesc(null);
        setEditCourseSem(null);
        setEditCourseAvalb(null);
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

                <TableCell>{name}</TableCell>

                <TableCell align={"center"}>A</TableCell>

                <TableCell align={"center"}><CheckBoxIcon/></TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" sx={{ ml:1 }} size={"small"} onClick={handleViewClickOpen}><InfoIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ ml:1 }} size={"small"} color={"warning"} onClick={handleEditClickOpen}><EditIcon fontSize={"small"}/></Button>
                    <Button variant="contained" sx={{ ml:1 }} size={"small"} color={"error"} onClick={handleDeleteClickOpen}><DeleteIcon fontSize={"small"}/></Button>
                </TableCell>

            </TableRow>

            {/* view dialog */}
            <Dialog
                open={showViewDialog}
                onClose={handleViewClose}
            >
                <DialogTitle >
                    {name}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="Course Description" variant="standard" fullWidth sx={{ mb: 1 }} InputProps={{readOnly: true}} defaultValue={desc} multiline maxRows={4}/>
                        <TextField label="Semaster" variant="standard" fullWidth sx={{ mb: 1, mt:2 }} InputProps={{readOnly: true}} defaultValue={sem}/>
                        <TextField label="Avaliable" variant="standard" fullWidth sx={{ mb: 1, mt:2 }} InputProps={{readOnly: true}} defaultValue={avlb}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>

            {/* Edit dialog */}
            <Dialog
                open={showEditDialog}
                onClose={handleEditClose}
            >
                <DialogTitle >
                    {name}
                </DialogTitle>
                <DialogContent>
                    <TextField sx={{ width: '100%', mt: 1}} label="Course Name" variant="outlined" value={editCourseName} onChange={(newValue) => setEditCourseName(newValue.target.value)}/>



                    <TextField sx={{ width: '100%', mt: 1}} label="Course Description" variant="outlined" multiline rows={2} value={editCourseDesc} onChange={(newValue) => setEditCourseDesc(newValue.target.value)}/>

                    <TextField
                        select
                        label="Semester"
                        sx={{ width: '100%', mt: 1}}
                        value={editCourseSem}
                        onChange={(event, newValue) => {setEditCourseSem(newValue.props.value)}}
                    >
                        <MenuItem value={'A'}>A</MenuItem>
                        <MenuItem value={'B'}>B</MenuItem>
                        <MenuItem value={'S'}>Summer</MenuItem>
                    </TextField>

                    <FormHelperText sx={{ml: 2}}>Available</FormHelperText>
                    <ToggleButton
                        value={editCourseAvalb}
                        selected={editCourseAvalb}
                        sx={{ width: '100%'}}
                        color={"primary"}
                        onChange={() => {setEditCourseAvalb(!editCourseAvalb)}}
                    >
                        <CheckBoxIcon />
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
                <DialogTitle >
                    {name}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete <b>{name}</b>? this will delete all bookings, and revisions within this course.
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
    avatarUrl: PropTypes.any,
    handleClick: PropTypes.func,
    name: PropTypes.any,
    role: PropTypes.any,
    selected: PropTypes.any,
};
