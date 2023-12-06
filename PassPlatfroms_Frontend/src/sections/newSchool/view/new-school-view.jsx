import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, FormHelperText, Snackbar, TextField, ToggleButton} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import MenuItem from "@mui/material/MenuItem";
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import DialogActions from "@mui/material/DialogActions";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import DisabledByDefaultRoundedIcon from '@mui/icons-material/DisabledByDefaultRounded';
import InfoIcon from "@mui/icons-material/Info";
import DeleteIcon from "@mui/icons-material/Delete";
import DialogContentText from "@mui/material/DialogContentText";


// ----------------------------------------------------------------------

export default function NewSchoolPage() {

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() => {
        (setProgPercent(((shownSection - 1) / 3) * 100))
    }, [shownSection]);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };


    const [schoolID, setSchoolID] = useState("");
    const [schoolName, setSchoolName] = useState("");
    const [schoolDesc, setScoolDesc] = useState("");

    const [courses, setCourses] = useState([]);


    const [showAddDialog, setShowAddDialog] = useState(false);
    const [addCourseID, setAddCourseID] = useState(null);
    const [addCourseName, setAddCourseName] = useState(null);
    const [addCourseDesc, setAddCourseDesc] = useState(null);
    const [addCourseSem, setAddCourseSem] = useState(null);
    const [addCourseAvalb, setAddCourseAvalb] = useState(false);
    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);
    };

    function handleAddSave() {
        if (addCourseID !== null && addCourseName !== null && addCourseDesc !== null && addCourseSem !== null && addCourseAvalb !== null) {
            courses.push({
                "courseid": addCourseID,
                "coursename": addCourseName,
                "coursedesc": addCourseDesc,
                "coursesem": addCourseSem,
                "courseavlb": addCourseAvalb
            });

            setAddCourseID(null);
            setAddCourseName(null);
            setAddCourseDesc(null);
            setAddCourseSem(null);
            setAddCourseAvalb(false);

            // close dialog etc
            setShowAddDialog(false);
        } else {
            setErrorMsg("Please Add all the course details");
            setErrorShow(true);
        }
    }

    const [viewCourseID, setViewCourseID] = useState(null);
    const [viewCourseName, setViewCourseName] = useState(null);
    const [viewCoursedesc, setViewCoursedesc] = useState(null);
    const [viewCoursesem, setViewCoursesem] = useState(null);
    const [viewCourseavlb, setViewCourseavlb] = useState(null);

    const [showViewDialog, setShowViewDialog] = useState(false);
    const handleViewClickOpen = (course) => {
        setShowViewDialog(true);

        setViewCourseID(course.courseid);
        setViewCourseName(course.coursename);
        setViewCoursedesc(course.coursedesc);
        setViewCoursesem(course.coursesem);
        setViewCourseavlb(course.courseavlb);
    };
    const handleViewClose = () => {
        setShowViewDialog(false);

        setViewCourseID(null);
        setViewCourseName(null);
        setViewCoursedesc(null);
        setViewCoursesem(null);
        setViewCourseavlb(null);
    };


    function nextSection() {
        if (shownSection === 1) {
            if (schoolID !== null && schoolName !== null && schoolDesc !== null && schoolID !== undefined && schoolName !== undefined && schoolDesc !== undefined && Object.keys(schoolID).length !== 0 && Object.keys(schoolName).length !== 0 && Object.keys(schoolDesc).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Add all school Information Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 2) {
            setShownSection((shownSection) + 1);
        }

        if (shownSection === 3) {
            alert("call api and show results based on api return");
            setProgPercent(100);
            // change color of progress to red if it is error etc
        }

    }

    function prevSection() {
        setShownSection((shownSection) - 1);
    }

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };


    return (


        <Container>

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">New School</Typography>

                <div>
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                            disabled={shownSection === 1} onClick={prevSection} sx={{m: 1}}>
                        Back
                    </Button>
                    <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                            onClick={nextSection}>
                        {shownSection === 3 ? "Submit" : (<>{shownSection === 2 && Object.keys(courses).length === 0 ? "Skip" : "Next"}</>)}
                    </Button>
                </div>


            </Stack>

            <Box sx={{width: '100%', mb: 2}}>
                <LinearProgress variant="determinate" value={progPercent} style={{borderRadius: 5, height: 10}}/>
            </Box>

            {/* elements */}
            {/* school information card */}
            {
                shownSection === 1 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">School ID:</Typography>
                        <TextField fullWidth value={schoolID} onChange={(newValue) => {
                            setSchoolID(newValue.target.value)
                        }} label={"School Code"}> </TextField>
                        <FormHelperText>School Code (IT, WM, etc.).</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>School Name:</Typography>
                        <TextField fullWidth value={schoolName} onChange={(newValue) => {
                            setSchoolName(newValue.target.value)
                        }} label={"School Name"}> </TextField>
                        <FormHelperText>School Name (Web Media, Information Com Technology, etc.).</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>School Description:</Typography>
                        <TextField fullWidth value={schoolDesc} onChange={(newValue) => {
                            setScoolDesc(newValue.target.value)
                        }} label={"School Description"}> </TextField>
                        <FormHelperText>Optional.</FormHelperText>
                    </div>
                </Card>
            }

            {/* courses information card */}
            {
                shownSection === 2 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">* Add Courses:</Typography>
                        <FormHelperText>This step is optional, press the add course button and add the course information,
                            all courses will be displayed below</FormHelperText>

                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-circle-fill"/>}
                                onClick={handleAddClickOpen} sx={{mt: 1}} fullWidth>
                            Add Course
                        </Button>

                        {
                            Object.keys(courses).length !== 0 &&
                            <TableContainer component={CustomPaper} sx={{mt: 2}}>
                                <Table sx={{minWidth: 650}}>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Course Name</TableCell>
                                            <TableCell align="right">ID</TableCell>
                                            <TableCell align="right">Semester</TableCell>
                                            <TableCell align="right">Available</TableCell>
                                            <TableCell align="right"></TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {courses.map((course, index) => (
                                            <TableRow
                                                key={index}
                                                sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                            >
                                                <TableCell component="th" scope="row">{course.coursename}</TableCell>
                                                <TableCell align="right">{course.courseid}</TableCell>
                                                <TableCell align="right">{course.coursesem}</TableCell>
                                                <TableCell align="right">{course.courseavlb ? (<CheckBoxIcon/>) : (
                                                    <DisabledByDefaultRoundedIcon/>)}</TableCell>
                                                <TableCell align="right">
                                                    <Button variant="contained" sx={{ml: 1}} size={"small"}
                                                            onClick={() => handleViewClickOpen(course)}><InfoIcon
                                                        fontSize={"small"}/></Button>
                                                    <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"}
                                                            onClick={() => {
                                                                setCourses([...courses.slice(0, index), ...courses.slice(index + 1)])
                                                            }}><DeleteIcon fontSize={"small"}/></Button>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        }
                    </div>
                </Card>
            }

            {/* overview card */}
            {
                shownSection === 3 && <Card>
                    <div style={{padding: "15px"}}>
                        <Typography variant="h6">Overview:</Typography>
                        <FormHelperText>Please validate School Details before submitting.</FormHelperText>

                        <TextField label="School Code" variant="standard" fullWidth sx={{mb: 1, mt: 3}}
                                   InputProps={{readOnly: true}} defaultValue={schoolID}/>
                        <TextField label="School Name" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={schoolName}/>
                        <TextField label="School Description" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={schoolDesc} multiline maxRows={4}/>

                        {
                            Object.keys(courses).length !== 0 &&
                            <>
                                <FormHelperText sx={{mt: 2}}>School Courses</FormHelperText>
                                <TableContainer component={CustomPaper}>
                                    <Table sx={{minWidth: 650}}>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Course Name</TableCell>
                                                <TableCell align="right">ID</TableCell>
                                                <TableCell align="right">Semester</TableCell>
                                                <TableCell align="right">Available</TableCell>
                                                <TableCell align="right"></TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {courses.map((course, index) => (
                                                <TableRow
                                                    key={index}
                                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                >
                                                    <TableCell component="th" scope="row">{course.coursename}</TableCell>
                                                    <TableCell align="right">{course.courseid}</TableCell>
                                                    <TableCell align="right">{course.coursesem}</TableCell>
                                                    <TableCell align="right">{course.courseavlb ? (<CheckBoxIcon/>) : (
                                                        <DisabledByDefaultRoundedIcon/>)}</TableCell>
                                                    <TableCell align="right">
                                                        <Button variant="contained" sx={{ml: 1}} size={"small"}
                                                                onClick={() => handleViewClickOpen(course)}><InfoIcon
                                                            fontSize={"small"}/></Button>
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </>
                        }

                    </div>
                </Card>
            }

            {/* Add dialog */}
            <Dialog
                open={showAddDialog}
                onClose={handleAddClose}
            >
                <DialogTitle>
                    Add New Course
                </DialogTitle>
                <DialogContent>
                    <TextField sx={{width: '100%', mt: 1}} label="Course Code" variant="outlined" value={addCourseID}
                               onChange={(newValue) => setAddCourseID(newValue.target.value)}/>

                    <TextField sx={{width: '100%', mt: 1}} label="Course Name" variant="outlined" value={addCourseName}
                               onChange={(newValue) => setAddCourseName(newValue.target.value)}/>

                    <TextField sx={{width: '100%', mt: 1}} label="Course Description" variant="outlined" multiline
                               rows={2} value={addCourseDesc}
                               onChange={(newValue) => setAddCourseDesc(newValue.target.value)}/>

                    <TextField
                        select
                        label="Semester"
                        sx={{width: '100%', mt: 1}}
                        value={addCourseSem}
                        onChange={(event, newValue) => {
                            setAddCourseSem(newValue.props.value)
                        }}
                    >
                        <MenuItem value={'A'}>A</MenuItem>
                        <MenuItem value={'B'}>B</MenuItem>
                        <MenuItem value={'S'}>Summer</MenuItem>
                    </TextField>

                    <FormHelperText sx={{ml: 2}}>Available</FormHelperText>
                    <ToggleButton
                        value={addCourseAvalb}
                        selected={addCourseAvalb}
                        sx={{width: '100%'}}
                        color={"primary"}
                        onChange={() => {
                            setAddCourseAvalb(!addCourseAvalb)
                        }}
                    >
                        <CheckBoxIcon/>
                    </ToggleButton>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleAddClose}>Cancel</Button>
                    <Button onClick={handleAddSave} autoFocus>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

            {/* view dialog */}
            <Dialog
                open={showViewDialog}
                onClose={handleViewClose}
            >
                <DialogTitle>
                    {viewCourseID}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <TextField label="Name" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={viewCourseName}/>
                        <TextField label="Course Description" variant="standard" fullWidth sx={{mb: 1}}
                                   InputProps={{readOnly: true}} defaultValue={viewCoursedesc} multiline maxRows={4}/>
                        <TextField label="Semaster" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={viewCoursesem}/>
                        <TextField label="Avaliable" variant="standard" fullWidth sx={{mb: 1, mt: 2}}
                                   InputProps={{readOnly: true}} defaultValue={viewCourseavlb}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>
        </Container>
    );
}
