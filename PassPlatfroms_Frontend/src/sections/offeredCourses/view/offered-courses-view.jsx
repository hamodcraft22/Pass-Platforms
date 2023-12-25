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


import OfferedCoursesTableRow from '../offeredCourses-table-row';

import OfferedCoursesTableToolbar from '../offeredCourses-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {useSearchParams} from "react-router-dom";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import MultiSelect from "../MultiSelect";
import {it} from "date-fns/locale";
import UserProfile from "../../../components/auth/UserInfo";
import {Alert, Backdrop, CircularProgress, FormHelperText, Snackbar} from "@mui/material";

// ----------------------------------------------------------------------

export default function OfferedCoursesPage() {

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

    const [successShow, setSuccessShow] = useState(false);
    const [successMsg, setSuccessMsg] = useState("");
    const handleSuccessAlertClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setSuccessShow(false);
    };

    const queryParameters = new URLSearchParams(window.location.search)
    const leaderIDParm = queryParameters.get("leaderID");


    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(10);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    const [offeredCourses, setOfferedCourses] = useState([]);

    const [canEdit, setCanEdit] = useState(false);

    // get offered courses api
    async function getLeaderCourses(leaderID) {
        try {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/offeredcourse/leader/${leaderID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        return response.json()
                    }
                })
                .then((data) =>
                {
                    if (data !== null && Object.keys(data.transObject).length !== 0)
                    {
                        parseOfferedCourses(data.transObject);
                    }
                    else
                    {
                        setErrorMsg("No Courses Found");
                        setErrorShow(true);
                    }
                }).then(() => {setLoadingShow(false);})
        } catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Courses Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    function parseOfferedCourses(offeredCourses)
    {
        let correctedOfferedCourses = [];

        offeredCourses.forEach((offer) => {
            correctedOfferedCourses.push({"offerid":offer.offerid, "courseid":offer.course.courseid, "coursename":offer.course.coursename, "userName":offer.leader.userName});
        });

        setOfferedCourses(correctedOfferedCourses);
    }

    async function getCoursesAvlb(leaderID) {
        try {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/course/leader/${leaderID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200)
                    {
                        return response.json()
                    }
                    else
                    {
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        setCoursesAvlb(data.transObject)
                    }
                }).then(() => {setLoadingShow(false);})
        } catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Courses Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    // get user info
    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        setUserID(userID);
        setUserRole(userRole);

        setCanEdit(true);

        getLeaderCourses(userID);
        getCoursesAvlb(userID);
    }

    // get school and courses on load - if not leader and there is param
    useEffect(() => {if (leaderIDParm !== null && leaderIDParm !== undefined && Object.keys(leaderIDParm).length !== 0) {getLeaderCourses(leaderIDParm); setCanEdit(false);} else {getUserInfo()}}, [])

    const handleSort = (event, id) => {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '') {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
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
        inputData: offeredCourses,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;

    const [showAddDialog, setShowAddDialog] = useState(false);



    const [coursesAvlb, setCoursesAvlb] = useState([]);

    const [coursesToAdd, setCoursesToAdd] = useState([]);

    function createSubmit()
    {
        let offeredCoursesList = [];

        coursesToAdd.forEach((course) => {
            offeredCoursesList.push({"leader":{"userid":userID}, "course":{"courseid":course.courseid}})
        })

        submitCourses(offeredCoursesList);
    }

    // add offered courses api
    async function submitCourses(offeredCoursesList)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(offeredCoursesList)};

            await fetch(`http://localhost:8080/api/offeredcourse/multi`, requestOptions)
                .then((response) => {
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
                .then((data) => {
                    setLoadingShow(false);
                    if (isok)
                    {
                        // refresh transcript
                        setSuccessMsg("Courses added, duplicates (if any) ignored");
                        setSuccessShow(true);
                        getLeaderCourses(userID);
                        getCoursesAvlb(userID);
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

    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
        createSubmit();
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
                <Typography variant="h4">{offeredCourses && Object.keys(offeredCourses).length !== 0 && <>{offeredCourses[0].userName}</>} Offered Courses</Typography>

                {
                    leaderIDParm === null && userRole === "leader" &&
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                            onClick={handleAddClickOpen}>
                        New Course
                    </Button>
                }
            </Stack>

            <Card>
                <OfferedCoursesTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={offeredCourses.length}
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
                                        <OfferedCoursesTableRow
                                            offerID={row.offerid}
                                            courseID={row.courseid}
                                            courseName={row.coursename}
                                            canDelete={canEdit}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, offeredCourses.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={offeredCourses.length}
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
                        <div style={{marginTop: "5px"}}>
                            <MultiSelect
                                items={coursesAvlb}
                                label="Courses"
                                selectAllLabel="All"
                                courses={(items) => {setCoursesToAdd(items)}}
                            />
                            <FormHelperText>Make sure you have uploaded your transcript, if not uploaded no courses with be allowed.</FormHelperText>
                        </div>
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
