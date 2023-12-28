import React, {useEffect, useState} from 'react';

import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import Container from '@mui/material/Container';
import TableBody from '@mui/material/TableBody';
import Typography from '@mui/material/Typography';
import TableContainer from '@mui/material/TableContainer';
import TablePagination from '@mui/material/TablePagination';

import Scrollbar from '../../../components/scrollbar';

import TableNoData from '../../../components/table/table-no-data';
import TableMainHead from '../../../components/table/table-head';
import TableEmptyRows from '../../../components/table/table-empty-rows';
import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';


import TranscriptTableRow from '../transcript-table-row';
import TranscriptTableToolbar from '../transcript-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import extractTextFromPdf from '../extractTextFromPdf';
import extractCoursesFromText from '../extractCoursesFromText';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import {styled} from '@mui/material/styles';
import TableHead from "@mui/material/TableHead";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {Alert, Backdrop, CircularProgress, FormHelperText, Snackbar} from "@mui/material";
import UserProfile from "../../../components/auth/UserInfo";
import ExportToExcel from "../../../utils/exportExcel";


// ----------------------------------------------------------------------

export default function TranscriptPage()
{

    // this page is only for the managers / users of the transcript

    const [loadingShow, setLoadingShow] = useState(false);

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

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);


    // trascripts
    const [transcripts, setTranscripts] = useState([]);

    // get transcripts api
    async function getTranscripts(leaderID)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`https://zift.ddnsfree.com:5679/api/transcript/leader/${leaderID}`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 204)
                    {
                        setErrorMsg("no transcripts found");
                        setErrorShow(true);
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
                        console.log(response);
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    setLoadingShow(false);
                    if (isok)
                    {
                        setTranscripts(data.transObject);
                    }
                });

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }


    const handleSort = (event, id) =>
    {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '')
        {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
    };

    const handleChangePage = (event, newPage) =>
    {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) =>
    {
        setPage(0);
        setRowsPerPage(parseInt(event.target.value, 10));
    };

    const handleFilterByName = (event) =>
    {
        setPage(0);
        setFilterName(event.target.value);
    };

    const dataFiltered = applyFilter({
        inputData: transcripts,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;


    const [showAddDialog, setShowAddDialog] = useState(false);


    const handleAddClickOpen = () =>
    {
        setShowAddDialog(true);
    };
    const handleAddClose = () =>
    {
        setShowAddDialog(false);

        setCourses([]);
    };
    const handleAddSave = () =>
    {
        if (courses !== null && Object.keys(courses).length !== 0)
        {
            setShowAddDialog(false);
            submitTranscript();
        }
        else
        {
            setErrorMsg("no transcripts found");
            setErrorShow(true);
        }
    };


    const queryParameters = new URLSearchParams(window.location.search)
    const studentIDParm = queryParameters.get("studentID");

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");


    async function getUserInfo()
    {
        // if leader, get his booking
        // if student, get his booking,

        // if admin/manager, get param and get his booking

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
                // get bookings made by student (could be leader as well)
                getTranscripts(studentIDParm);
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
                setErrorMsg("you are not allowed to access others Transcript");
                setErrorShow(true);
            }
            else
            {
                // get student bookings
                getTranscripts(userID);
            }
        }
        else if (userRole === "leader")
        {
            if (studentIDParm !== null)
            {
                setErrorMsg("you are not allowed to access others Transcript");
                setErrorShow(true);
            }
            else
            {
                // get student bookings
                getTranscripts(userID);
            }
        }
        else
        {
            setErrorMsg("you are not allowed to access others Transcript");
            setErrorShow(true);
        }

    }

    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        getUserInfo()
    }, []);


    const [courses, setCourses] = useState([]);

    // transcript extract
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
                        courseGrade = course.grade;
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

    async function submitTranscript()
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(courses)};

            await fetch(`https://zift.ddnsfree.com:5679/api/transcript/multi`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
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
                        // refresh transcript
                        setSuccessMsg("Courses added, duplicates (if any) ignored");
                        setSuccessShow(true);
                        getTranscripts(userID);
                    }
                });

        }
        catch (error)
        {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }

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

            <Snackbar open={successShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleSuccessAlertClose} severity="success" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {successMsg}
                </Alert>
            </Snackbar>

            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">{transcripts && Object.keys(transcripts).length !== 0 && <>{transcripts[0].student.userName}</>} - Transcript</Typography>

                <div>
                    {
                        studentIDParm === null && userRole === "leader" &&
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                                onClick={handleAddClickOpen}>
                            Upload Transcript
                        </Button>
                    }

                    <ExportToExcel data={dataFiltered} filename="Transcript List"/>
                </div>
            </Stack>

            <Card>
                <TranscriptTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'courseid', label: 'Code'},
                                    {id: 'grade', label: 'Grade', align: 'center'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <TranscriptTableRow
                                            transID={row.transid}
                                            courseID={row.courseid}
                                            grade={row.grade}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, transcripts.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={transcripts.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />


                {/* Add dialog */}
                <Dialog
                    open={showAddDialog}
                    onClose={handleAddClose}
                >
                    <DialogTitle>
                        Upload Transcript File
                    </DialogTitle>
                    <DialogContent>
                        <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
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
                                                    {course.courseid} {course.title}
                                                </TableCell>
                                                <TableCell align="center">{course.grade}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        }
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleAddClose}>Cancel</Button>
                        <Button onClick={handleAddSave} autoFocus>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>
            </Card>
        </Container>
    );
}
