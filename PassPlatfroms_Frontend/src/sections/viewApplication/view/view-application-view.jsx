import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {Alert, Backdrop, CardContent, CircularProgress, Divider, FormHelperText, Snackbar, TextField} from "@mui/material";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Unstable_Grid2";
import EmailIcon from '@mui/icons-material/Email';
import Link from "@mui/material/Link";
import SendRoundedIcon from '@mui/icons-material/SendRounded';
import Iconify from "../../../components/iconify";
import LinearProgress from "@mui/material/LinearProgress";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import extractTextFromPdf from "../extractTextFromPdf";
import extractCoursesFromText from "../extractCoursesFromText";
import {styled} from "@mui/material/styles";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import UserProfile from "../../../components/auth/UserInfo";
import moment from "moment";


// ----------------------------------------------------------------------

export default function ViewApplicationPage()
{

    const queryParameters = new URLSearchParams(window.location.search)
    const studentIDParm = queryParameters.get("studentID");

    const [loadingShow, setLoadingShow] = useState(false);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) =>
    {
        setErrorShow(false);
    };

    const [successShow, setSuccessShow] = useState(false);
    const [successMsg, setSuccessMsg] = useState("");
    const handleSuccessAlertClose = (event, reason) =>
    {
        if (reason === 'clickaway')
        {
            return;
        }
        setSuccessShow(false);
    };


    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() =>
    {
        (setProgPercent(((shownSection - 1) / 3) * 100))
    }, [shownSection]);


    // which view to show
    const [viewApplication, setVeiwApplication] = useState(false);
    const [newApplication, setNewApplication] = useState(false);


    // user elements
    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");


    // application view elements
    const [application, setApplication] = useState([]);

    async function getUserApplication(studentID, userRole)
    {
        try
        {
            let isok = false;
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/application/student/${studentID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        isok = true;
                        return response.json()
                    }
                    else if (response.status === 204)
                    {
                        if (userRole === "student")
                        {
                            setNewApplication(true);
                        }
                    }
                    else
                    {
                        console.log(response);
                        setErrorMsg("unknown error, check console");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    if (isok)
                    {
                        setApplication(data.transObject);
                        setVeiwApplication(true);
                    }
                })
                .then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No application Found");
            setErrorShow(true);
            console.log(error);
        }
    }


    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);

        // if admin / manager and there is student paramteter
        if (userRole === "manager" || userRole === "admin")
        {
            //call admin function
            if (studentIDParm !== null)
            {
                getUserApplication(studentIDParm);
            }
            else
            {
                setErrorMsg("No student id supplied!");
                setErrorShow(true);
            }
        }
        else if (userRole === "student")
        {
            if (studentIDParm !== null)
            {
                setErrorMsg("you are not allowed to access others applications");
                setErrorShow(true);
            }
            else
            {
                // call student function
                getUserApplication(userID, userRole);
            }
        }
        else
        {
            setErrorMsg("you are not allowed to access application");
            setErrorShow(true);
        }
    }


    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        getUserInfo()
    }, []);


    // application add elements
    const [courses, setCourses] = useState([]);
    const [applicationNote, setApplicationNote] = useState("");

    const CustomPaper = (props) =>
    {
        return <Paper elevation={8} {...props} />;
    };


    // submit function
    async function createSubmit()
    {
        const applicationDto = {"note": applicationNote, "transcripts": courses};

        console.log(applicationDto);
        submitApplication(applicationDto)
    }

    async function submitApplication(applicationDto)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(applicationDto)};

            await fetch(`http://localhost:8080/api/application`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        setProgPercent(100);
                        // return response.json();
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
                        window.location.reload();
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


    const [userNote, setUserNote] = useState("");

    async function submitUserNote()
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: userNote};

            await fetch(`http://localhost:8080/api/applicationNote/${application.applicationid}`, requestOptions)
                .then(response =>
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
                .then(() =>
                {
                    setLoadingShow(false);
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
            if (courses !== null && courses !== undefined && Object.keys(courses).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Please Upload your transcript.");
                setErrorShow(true);
            }
        }

        if (shownSection === 2)
        {
            if (applicationNote !== null && applicationNote !== undefined && Object.keys(applicationNote).length !== 0)
            {
                setShownSection((shownSection) + 1);
            }
            else
            {
                setErrorMsg("Please type your note.");
                setErrorShow(true);
            }
        }


        if (shownSection === 3)
        {
            createSubmit();
        }

    }

    function prevSection()
    {
        setShownSection((shownSection) - 1);
    }

    const handleFileChange = async (e) =>
    {
        const file = e.target.files[0];

        if (file)
        {
            try
            {
                const pdfText = await extractTextFromPdf(file);

                // Define your regular expression for course extraction
                const courseRegex = /([A-Z]{2})\s+(\d+)\s+([A-Z0-9]+)\s+([A-Za-z0-9\s\-]+?)\s+(\S+)\s+(\d+\.\d+)\s+(\d+\.\d+)/gm;

                const extractedCourses = extractCoursesFromText(pdfText, courseRegex);

                let correctedCourses = [];

                extractedCourses.forEach((course) =>
                {

                    let courseCode = course.code + course.number;
                    let courseName = course.title

                    let courseGrade = '';

                    if (course.grade.toLowerCase() === "comp")
                    {
                        courseGrade = 'A';
                    }
                    else if (['a', 'b', 'c', 'd', 'f'].includes(course.grade.toLowerCase().charAt(0)))
                    {
                        courseGrade = course.grade
                    }
                    else
                    {
                        courseGrade = 'E';
                    }

                    correctedCourses.push({"courseid": courseCode, "title": courseName, "grade": courseGrade, "student": {"userid": userID}});
                });

                setCourses(correctedCourses);

            }
            catch (error)
            {
                console.error(error.message);
            }
        }
    };

    const VisuallyHiddenInput = styled('input')({
        clip: 'rect(0 0 0 0)',
        clipPath: 'inset(50%)',
        height: 1,
        overflow: 'hidden',
        position: 'absolute',
        bottom: 0,
        left: 0,
        whiteSpace: 'nowrap',
        width: 1,
    });

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
                <Typography variant="h4">{application !== null && <>view</>}{application === null && <>New</>} Application</Typography>

                {/* only show when a new application */}
                {
                    newApplication && <div>
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:arrow-ios-back-fill"/>}
                                disabled={shownSection === 1} onClick={prevSection} sx={{m: 1}}>
                            Back
                        </Button>
                        <Button variant="contained" color="inherit" endIcon={<Iconify icon="eva:arrow-ios-forward-fill"/>}
                                onClick={nextSection}>
                            {shownSection === 3 ? (
                                <>Submit</>
                            ) : (
                                <>Next</>
                            )}
                        </Button>
                    </div>
                }
            </Stack>

            {/* view application elements - if student with application or a manager / admin */}
            {
                viewApplication && <Grid container spacing={3}>
                    {/* Left Card */}
                    <Grid xs={12} md={6} lg={4}>
                        <Card sx={{backgroundColor: '#f5f5f5'}}>
                            <CardContent>
                                <Typography variant="h6">Application Information</Typography>

                                <TextField label="Student" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={application !== null && application.user.userid + " | " + application.user.userName}/>
                                <TextField label="Application Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={moment(application.datetime).format("hh:mm A | DD/MM/YYYY")}/>
                                <TextField label="Application Note" variant="standard" fullWidth sx={{mt: 2}} multiline InputProps={{readOnly: true}} defaultValue={application.note}/>

                                {/* if manager */}
                                {
                                    (userRole === "admin" || userRole === "manager") &&
                                    <Button variant="contained" startIcon={<EmailIcon/>} href={`mailto:${application.user.userid}@student.polytechnic.com`} sx={{mt: 2}}>
                                        Email Student
                                    </Button>
                                }

                                {/* if student */}
                                {
                                    userRole === "student" &&
                                    <Button variant="contained" startIcon={<EmailIcon/>} href={"mailto:test@example.com"} sx={{mt: 2}}>
                                        Email Manager
                                    </Button>
                                }

                            </CardContent>
                        </Card>
                    </Grid>

                    {/* Right Card */}
                    <Grid xs={12} md={6} lg={8}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6" sx={{mb: 2}}>Application Notes</Typography>

                                {/* loop to display all notes */}
                                {
                                    application &&
                                    <>
                                        {
                                            application.applicationNotes && application.applicationNotes.map((note) =>
                                            {

                                                const bkColor = note.user.role.rolename === 'student' ? '#fafff8' : '#fafff8';

                                                return (
                                                    <Card sx={{mb: 2, backgroundColor: bkColor}}>
                                                        <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                                            <Box sx={{minWidth: 240, flexGrow: 1}}>
                                                                <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                                    {note.user.userName}
                                                                </Link>

                                                                <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                                                    {moment(note.datetime).format("hh:mm A | DD/MM/YYYY")}
                                                                </Typography>

                                                                <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                                                    {note.notebody}
                                                                </Typography>
                                                            </Box>

                                                            <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                                                {note.user.role.rolename}
                                                            </Typography>
                                                        </Stack>
                                                    </Card>
                                                )
                                            })
                                        }
                                    </>
                                }


                                <Divider/>

                                <Typography variant="h6" sx={{mt: 2, mb: 2}}>Post a Note:</Typography>
                                <TextField fullWidth label="Note" variant="outlined" multiline minRows={2} value={userNote} onChange={(event) =>
                                {
                                    setUserNote(event.target.value)
                                }}/>
                                <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                                    <Button variant="contained" endIcon={<SendRoundedIcon/>} sx={{mt: 2}} onClick={() =>
                                    {
                                        submitUserNote()
                                    }} disabled={userNote === null || userNote === "" || userNote === undefined}>
                                        Submit
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            }

            {/* new application - show if student that doesn't have application */}
            {
                newApplication &&
                <>
                    <Box sx={{width: '100%', mb: 2}}>
                        <LinearProgress variant="determinate" value={progPercent} style={{borderRadius: 5, height: 10}}/>
                    </Box>

                    {/* upload transcript card */}
                    {
                        shownSection === 1 && <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Uplaod Transcript:</Typography>
                                <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth sx={{mt: 2}}>
                                    Upload file
                                    <VisuallyHiddenInput type="file" onChange={handleFileChange} accept=".pdf"/>
                                </Button>

                                <FormHelperText>COMP coursees are marked as A</FormHelperText>
                                <FormHelperText>EXMP courses will not be accounted</FormHelperText>

                                {
                                    Object.keys(courses).length !== 0 &&
                                    <TableContainer component={CustomPaper} sx={{mt: 3}}>
                                        <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                            <TableHead>
                                                <TableRow>
                                                    <TableCell>Course</TableCell>
                                                    <TableCell align="center">Grade</TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {courses.map((course, index) => (
                                                    <TableRow
                                                        key={index}
                                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                    >
                                                        <TableCell component="th" scope="row">
                                                            {course.code} {course.title}
                                                        </TableCell>
                                                        <TableCell align="center">{course.grade}</TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </TableContainer>
                                }
                            </div>
                        </Card>
                    }

                    {/* application note card */}
                    {
                        shownSection === 2 && <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Application Note:</Typography>
                                <TextField sx={{mt: 2}} fullWidth multiline minRows={4} label={"Application Note"} variant={"outlined"} value={applicationNote} onChange={(event) => setApplicationNote(event.target.value)}/>
                            </div>
                        </Card>
                    }

                    {/* overview and submit card */}
                    {
                        shownSection === 3 && <Card>
                            <div style={{padding: "15px"}}>
                                <Typography variant="h6">Overview:</Typography>
                                <FormHelperText>Please validate your Application and transcript before submitting.</FormHelperText>

                                <TextField label="Application Note" variant="standard" fullWidth sx={{mb: 1, mt: 3}} InputProps={{readOnly: true}} defaultValue={applicationNote} multiline/>

                                <FormHelperText sx={{mt: 3, mb: 1}}>Transcript Courses</FormHelperText>
                                {
                                    Object.keys(courses).length !== 0 &&
                                    <TableContainer component={CustomPaper}>
                                        <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                            <TableHead>
                                                <TableRow>
                                                    <TableCell>Course</TableCell>
                                                    <TableCell align="center">Grade</TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {courses.map((course, index) => (
                                                    <TableRow
                                                        key={index}
                                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                    >
                                                        <TableCell component="th" scope="row">
                                                            {course.code}{course.number} {course.title}
                                                        </TableCell>
                                                        <TableCell align="center">{course.grade}</TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </TableContainer>
                                }

                            </div>
                        </Card>
                    }
                </>
            }

        </Container>
    );
}
