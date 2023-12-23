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

// ----------------------------------------------------------------------

export default function CoursesPage() {

    const [loadingShow, setLoadingShow] = useState(false);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorShow(false);
    };

    const queryParameters = new URLSearchParams(window.location.search)
    const schoolIDParm = queryParameters.get("schoolID")

    const [school, setSchool] = useState(null);
    const [courses, setCourses] = useState([]);

    // get school and courses api
    async function getSchoolCourses() {
        try {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/school/${schoolIDParm}`, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then((data) => {
                    setSchool(data.transObject);
                    setCourses(data.transObject.courses)
                }).then(() => {setLoadingShow(false);})
        } catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Courses Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    // get school and courses on load
    useEffect(() => {
        if (schoolIDParm !== null && schoolIDParm !== undefined && Object.keys(schoolIDParm).length !== 0) {getSchoolCourses()}
    }, [])

    const [showAddDialog, setShowAddDialog] = useState(false);
    const [addCourseID, setAddCourseID] = useState(null);
    const [addCourseName, setAddCourseName] = useState(null);


    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);

        setAddCourseID(null);
        setAddCourseName(null);

    };
    const handleAddSave = () => {
        setShowAddDialog(false);
    };


    // table vars and functions

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [selected, setSelected] = useState([]);

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    const handleSort = (event, id) => {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '') {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
    };

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = courses.map((n) => n.name);
            setSelected(newSelecteds);
            return;
        }
        setSelected([]);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setPage(0);
        setRowsPerPage(parseInt(event.target.value, 10));
    };

    const handleFilterByName = (event) => {
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
    const goToSchools = () => {
            let path = `/schools`;
            navigate(path);

    }

    const goToHome = () => {
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

                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                                onClick={handleAddClickOpen}>
                            New Course
                        </Button>
                    </Stack>

                    <Card>
                        <CoursesTableToolbar
                            numSelected={selected.length}
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
                                        numSelected={selected.length}
                                        onRequestSort={handleSort}
                                        onSelectAllClick={handleSelectAllClick}
                                        headLabel={[
                                            {id: '', label: ''},
                                            {id: 'name', label: 'Name'},
                                            {id: '', label: ''}
                                        ]}
                                    />
                                    <TableBody>
                                        {dataFiltered
                                            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                            .map((row) => (
                                                <CoursesTableRow
                                                    courseID={row.courseid}
                                                    courseName={row.coursename}
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
