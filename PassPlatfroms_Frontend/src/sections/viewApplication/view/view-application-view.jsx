import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {Alert, CardContent, Divider, FormHelperText, Snackbar, TextField} from "@mui/material";
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


// ----------------------------------------------------------------------

export default function ViewApplicationPage() {

    const [shownSection, setShownSection] = useState(1);
    const [progPercent, setProgPercent] = useState(0);
    useEffect(() => {
        (setProgPercent(((shownSection - 1) / 3) * 100))
    }, [shownSection]);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        setErrorShow(false);
    };

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    const [text, setText] = useState('');
    const [courses, setCourses] = useState([]);
    const [applicationNote, setApplicationNote] = useState("");

    function nextSection() {
        if (shownSection === 1) {
            if (courses !== null && courses !== undefined && Object.keys(courses).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please Upload your transcript.");
                setErrorShow(true);
            }
        }

        if (shownSection === 2) {
            if (applicationNote !== null && applicationNote !== undefined && Object.keys(applicationNote).length !== 0) {
                setShownSection((shownSection) + 1);
            } else {
                setErrorMsg("Please type your note.");
                setErrorShow(true);
            }
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


    const handleFileChange = async (e) => {
        const file = e.target.files[0];

        if (file) {
            try {
                const pdfText = await extractTextFromPdf(file);
                setText(pdfText);

                // Define your regular expression for course extraction
                const courseRegex = /([A-Z]{2})\s+(\d+)\s+([A-Z0-9]+)\s+([A-Za-z0-9\s\-]+?)\s+(\S+)\s+(\d+\.\d+)\s+(\d+\.\d+)/gm;

                const extractedCourses = extractCoursesFromText(pdfText, courseRegex);

                let correctedCourses = [];

                extractedCourses.forEach((course) => {

                    let courseCode = course.code + course.number;
                    let courseName = course.title

                    let courseGrade = '';

                    if (course.grade.toLowerCase() === "comp") {
                        courseGrade = 'A';
                    } else if (['a', 'b', 'c', 'd', 'f'].includes(course.grade.toLowerCase().charAt(0))) {
                        courseGrade = course.grade.charAt(0);
                    } else {
                        courseGrade = 'E';
                    }

                    correctedCourses.push({"code": courseCode, "title": courseName, "grade": courseGrade});
                });

                setCourses(correctedCourses);

            } catch (error) {
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

            {/* alerts */}
            <Snackbar open={errorShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Student - Application</Typography>

                {/* only show when a new application */}
                {
                    true && <div>
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
                false && <Grid container spacing={3}>
                    {/* Left Card */}
                    <Grid xs={12} md={6} lg={4}>
                        <Card sx={{backgroundColor: '#f5f5f5'}}>
                            <CardContent>
                                <Typography variant="h6">Application Information</Typography>

                                <TextField label="Student" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>
                                <TextField label="Application Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>
                                <TextField label="Application Note" variant="standard" fullWidth sx={{mt: 2}} multiline InputProps={{readOnly: true}} defaultValue={"asd"}/>

                                {/* if manager */}
                                <Button variant="contained" startIcon={<EmailIcon/>} href={"mailto:test@example.com"} sx={{mt: 2}}>
                                    Email Student
                                </Button>

                                {/* if student */}
                                <Button variant="contained" startIcon={<EmailIcon/>} href={"mailto:test@example.com"} sx={{mt: 2}}>
                                    Email Manager
                                </Button>
                            </CardContent>
                        </Card>
                    </Grid>

                    {/* Right Card */}
                    <Grid xs={12} md={6} lg={8}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6" sx={{mb: 2}}>Application Notes</Typography>

                                {/* loop to display all notes */}
                                <Card sx={{mb: 2, backgroundColor: '#fafff8'}}>
                                    <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                        <Box sx={{minWidth: 240, flexGrow: 1}}>
                                            <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                {"User"}
                                            </Link>

                                            <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                                {"Date time"}
                                            </Typography>

                                            <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                                {"Text for the note"}
                                            </Typography>
                                        </Box>

                                        <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                            Role
                                        </Typography>
                                    </Stack>
                                </Card>

                                <Card sx={{mb: 2, backgroundColor: '#fffcf8'}}>
                                    <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                        <Box sx={{minWidth: 240, flexGrow: 1}}>
                                            <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                {"User"}
                                            </Link>

                                            <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                                {"Date time"}
                                            </Typography>

                                            <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                                {"Text for the note"}
                                            </Typography>
                                        </Box>

                                        <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                            Role
                                        </Typography>
                                    </Stack>
                                </Card>

                                <Divider/>

                                <Typography variant="h6" sx={{mt: 2, mb: 2}}>Post a Note:</Typography>
                                <TextField fullWidth label="Note" variant="outlined" multiline minRows={2}/>
                                <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                                    <Button variant="contained" endIcon={<SendRoundedIcon/>} sx={{mt: 2}}>
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

                                <FormHelperText>Grades will be saved without any status (+, -)</FormHelperText>
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
