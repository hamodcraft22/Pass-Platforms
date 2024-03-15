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

import CoursesTableRow from '../courses-table-row';
import CoursesTableToolbar from '../courses-table-toolbar';


import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {Alert, Backdrop, CardActions, CardContent, CircularProgress, Snackbar, TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import Box from "@mui/material/Box";
import UserProfile from "../../../components/auth/UserInfo";
import {useNavigate} from "react-router-dom";
import ExportToExcel from "../../../utils/exportExcel";

// ----------------------------------------------------------------------

export default function CoursesPage()
{
    // this page is used by everyone, and editing is only allowed for admins/ managers

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

    const queryParameters = new URLSearchParams(window.location.search);
    const schoolIDParm = queryParameters.get("schoolID");

    const [school, setSchool] = useState(null);
    const [courses, setCourses] = useState([]);

    // get school and courses api
    async function getSchoolCourses()
    {
        try
        {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/school/${schoolIDParm}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("No Courses Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        setSchool(data.transObject);
                        setCourses(data.transObject.courses);
                    }
                }).then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Courses Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    // get school and courses on load
    useEffect(() =>
    {
        if (schoolIDParm !== null && schoolIDParm !== undefined && Object.keys(schoolIDParm).length !== 0)
        {
            getSchoolCourses()
        }
    }, [])


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
    }

    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        getUserInfo()
    }, []);


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

        setAddCourseID(null);
        setAddCourseName(null);

    };
    const handleAddSave = () =>
    {
        if (addCourseID !== null && addCourseID !== "" && addCourseName !== null && addCourseName !== "")
        {
            setShowAddDialog(false);
            createSubmit();
        }
        else
        {
            setErrorMsg("Please fill in all information");
            setErrorShow(true);
        }
    };


    async function createSubmit()
    {

        // do course dto
        const courseDto = {"courseid": addCourseID, "coursename": addCourseName, "school": {"schoolid": schoolIDParm}};
        console.log(courseDto);

        await submitCourse(courseDto);
    }

    async function submitCourse(courseDto)
    {
        let isok = false;
        let isBad = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(courseDto)};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/course`, requestOptions)
                .then(response =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
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
                        window.location.reload();
                        console.log(data);
                    }
                    else if (isBad)
                    {
                        setErrorMsg("the course code is already present");
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


    // table vars and functions

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);

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
        inputData: courses,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;

    let navigate = useNavigate();
    const goToSchools = () =>
    {
        let path = `/schools`;
        navigate(path);

    }

    const goToHome = () =>
    {
        let path = `/`;
        navigate(path);

    }


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
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            {/* if there is a school id */}
            {
                schoolIDParm !== null &&
                <>
                    <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                        <Typography variant="h4">{school !== null && school.schoolname} Courses</Typography>

                        <div>
                            {
                                (userRole === 'admin' || userRole === 'manager') &&
                                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                                        onClick={handleAddClickOpen}>
                                    New Course
                                </Button>
                            }

                            <ExportToExcel data={dataFiltered} filename="Courses List"/>
                        </div>
                    </Stack>

                    <Card>
                        <CoursesTableToolbar
                            filterName={filterName}
                            onFilterName={handleFilterByName}
                        />

                        <Scrollbar>
                            <TableContainer sx={{overflow: 'unset'}}>
                                <Table sx={{minWidth: 800}}>
                                    <TableMainHead
                                        order={order}
                                        orderBy={orderBy}
                                        rowCount={courses.length}
                                        onRequestSort={handleSort}
                                        headLabel={[
                                            {id: 'zift1', label: ''},
                                            {id: 'courseid', label: 'Code'},
                                            {id: 'coursename', label: 'Name'},
                                            {id: 'zift2', label: ''}
                                        ]}
                                    />
                                    <TableBody>
                                        {dataFiltered
                                            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                            .map((row) => (
                                                <CoursesTableRow
                                                    courseID={row.courseid}
                                                    courseName={row.coursename}
                                                    role={userRole}
                                                />
                                            ))}

                                        <TableEmptyRows
                                            height={77}
                                            emptyRows={emptyRows(page, rowsPerPage, courses.length)}
                                        />

                                        {notFound && <TableNoData query={filterName}/>}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Scrollbar>

                        <TablePagination
                            page={page}
                            component="div"
                            count={courses.length}
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
                                Add New Course
                            </DialogTitle>
                            <DialogContent>
                                <TextField sx={{width: '100%', mt: 1}} label="Course Code" variant="outlined"
                                           value={addCourseID} onChange={(newValue) => setAddCourseID(newValue.target.value)}/>

                                <TextField sx={{width: '100%', mt: 1}} label="Course Name" variant="outlined"
                                           value={addCourseName}
                                           onChange={(newValue) => setAddCourseName(newValue.target.value)}/>


                            </DialogContent>
                            <DialogActions>
                                <Button onClick={handleAddClose}>Cancel</Button>
                                <Button onClick={handleAddSave} autoFocus>
                                    Save
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </Card>
                </>
            }

            {/* if there is no course id */}
            {
                schoolIDParm === null &&
                <Box width={"100%"} height={"100%"} display="flex" justifyContent="center" alignItems="center">
                    <Card sx={{maxWidth: 345}}>
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                Uh oh!
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                it seems like you have gotten here somehow with selecting a school! please select one from the schools list
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button size="small" onClick={goToHome}>Home</Button>
                            <Button size="small" onClick={goToSchools}>Schools</Button>
                        </CardActions>
                    </Card>
                </Box>
            }

        </Container>
    );
}
