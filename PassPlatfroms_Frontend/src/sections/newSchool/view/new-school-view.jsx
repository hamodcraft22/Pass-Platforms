import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Backdrop, CircularProgress, FormHelperText, Snackbar, TextField} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import DeleteIcon from "@mui/icons-material/Delete";
import UserProfile from "../../../components/auth/UserInfo";
import {useNavigate} from "react-router-dom";


// ----------------------------------------------------------------------

export default function NewSchoolPage()
{
    // this page is for managers / admins only

    const [loadingShow, setLoadingShow] = useState(false);

    const [shownSection, setShownSection] = useState(0);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() =>
    {
        (setProgPercent(((shownSection - 1) / 3) * 100))
    }, [shownSection]);

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

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);

        if (userRole === 'manager' || userRole === 'admin')
        {
            setShownSection(1);
        }
        else
        {
            setErrorMsg("you are not allowed to access this page");
            setErrorShow(true);
        }
    }

    useEffect(() =>
    {
        getUserInfo()
    }, []);

    const [schoolID, setSchoolID] = useState("");
    const [schoolName, setSchoolName] = useState("");

    const [courses, setCourses] = useState([]);


    const [showAddDialog, setShowAddDialog] = useState(false);
    const [addCourseID, setAddCourseID] = useState(null);
    const [addCourseName, setAddCourseName] = useState(null);

    const handleAddClickOpen = () =>
    {
        setShowAddDialog(true);
    };
    const handleAddClose = () =>
    {
        setShowAddDialog(false);
    };

    function handleAddSave()
    {
        if (addCourseID !== null && addCourseName !== null)
        {
            courses.push({
                "courseid": addCourseID,
                "coursename": addCourseName,
                "school": {"schoolid": schoolID}
            });

            setAddCourseID(null);
            setAddCourseName(null);

            // close dialog etc
            setShowAddDialog(false);
        }
        else
        {
            setErrorMsg("Please Add all the course details");
            setErrorShow(true);
        }
    }


    async function createSubmit()
    {

        // do school dto
        const schoolDto = {"schoolid": schoolID, "schoolname": schoolName, "courses": courses};
        console.log(schoolDto);

        await submitSchool(schoolDto);
    }

    async function submitSchool(schoolDto)
    {
        let isok = false;
        let isBad = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(schoolDto)};

            await fetch(`http://localhost:8080/api/school`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        setProgPercent(100);
                        return response.json();
                    }
                    else if (response.status === 400)
                    {
                        isBad = true;
                    }
                    else if (response.status === 401)
                    {
                        setErrorMsg("you are not allowed to do this action");
                        setErrorShow(true);
                    }
                    else if (response.status === 404)
                    {
                        setErrorMsg("the request was not found on the server, double check your connection");
                        setErrorShow(true);
                    }
                    else
                    {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    setLoadingShow(false);
                    if (isok)
                    {
                        // it is fine, go on
                        goToSchools();
                        console.log(data);
                    }
                    else if (isBad)
                    {
                        setErrorMsg("the school code is already present");
                        setErrorShow(true);
                    }
                    else
                    {
                        console.log(data);
                    }
                })

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    function nextSection()
    {
        if (shownSection === 1)
        {
            if (schoolID !== null && schoolName !== null && schoolID !== undefined && schoolName !== undefined && Object.keys(schoolID).length !== 0 && Object.keys(schoolName).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Add all school Information Please");
                setErrorShow(true);
            }
        }

        if (shownSection === 2)
        {
            setShownSection((shownSection) + 1);
        }

        if (shownSection === 3)
        {
            createSubmit();
        }

    }

    let navigate = useNavigate();
    const goToSchools = () =>
    {
        let path = `/schools`;
        navigate(path);

    }

    function prevSection()
    {
        setShownSection((shownSection) - 1);
    }

    const CustomPaper = (props) =>
    {
        return <Paper elevation={8} {...props} />;
    };


    return (


        <Container>
            {/* loading */}
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={loadingShow}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>

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
                        <TextField fullWidth value={schoolID} onChange={(newValue) =>
                        {
                            setSchoolID(newValue.target.value)
                        }} label={"School Code"}> </TextField>
                        <FormHelperText>School Code (IT, WM, etc.).</FormHelperText>

                        <Typography variant="h6" sx={{mt: 3}}>School Name:</Typography>
                        <TextField fullWidth value={schoolName} onChange={(newValue) =>
                        {
                            setSchoolName(newValue.target.value)
                        }} label={"School Name"}> </TextField>
                        <FormHelperText>School Name (Web Media, Information Com Technology, etc.).</FormHelperText>
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
                                            <TableCell align="right">Course Code</TableCell>
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
                                                <TableCell align="right"> <Button variant="contained" sx={{ml: 1}} size={"small"} color={"error"} onClick={() =>
                                                {
                                                    setCourses([...courses.slice(0, index), ...courses.slice(index + 1)])
                                                }}><DeleteIcon fontSize={"small"}/></Button> </TableCell>
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

                        {
                            Object.keys(courses).length !== 0 &&
                            <>
                                <FormHelperText sx={{mt: 2}}>School Courses</FormHelperText>
                                <TableContainer component={CustomPaper}>
                                    <Table sx={{minWidth: 650}}>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Course Name</TableCell>
                                                <TableCell align="right">Course Code</TableCell>
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

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleAddClose}>Cancel</Button>
                    <Button onClick={handleAddSave} autoFocus>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

        </Container>
    );
}
