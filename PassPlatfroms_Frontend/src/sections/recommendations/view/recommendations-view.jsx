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

import RecommendationsTableRow from '../recommendations-table-row';
import RecommendationsTableToolbar from '../recommendations-table-toolbar';

import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import {Alert, Autocomplete, Backdrop, CircularProgress, FormHelperText, Snackbar, TextField} from "@mui/material";
import UserProfile from "../../../components/auth/UserInfo";
import moment from "moment";
import ExportToExcel from "../../../utils/exportExcel";
import {createFilterOptions} from "@mui/material/Autocomplete";


// ----------------------------------------------------------------------

export default function RecommendationsPage()
{

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

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    const [recommendations, setRecommendations] = useState([]);

    // all users to be added to group
    const [allUsers, setAllUsers] = useState([]);

    // get all users api
    async function getAllUsers()
    {
        try
        {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/users/students`, requestOptions)
                .then(response =>
                {
                    return response.json()
                })
                .then((data) =>
                {
                    setAllUsers(data)
                })
                .then(() =>
                {
                    setLoadingShow(false);
                })

        }
        catch (error)
        {
            console.log(error);
            setLoadingShow(false);
        }
    }


    async function getAllRecommendations()
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/recommendation`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("No Recommendations Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    parseRecommendations(data.transObject);
                }).then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Recommendations Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    async function getTutorRecommendations(tutorID)
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/recommendation/tutor/${tutorID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 200)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("You dont have any recommendations");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        parseRecommendations(data.transObject);
                    }
                }).then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("You do not have any Recommendations");
            setErrorShow(true);
            console.log(error);
        }
    }

    function parseRecommendations(retrivedRecommendations)
    {
        let correctedRecommendations = [];

        retrivedRecommendations.forEach((recm) =>
        {
            correctedRecommendations.push({
                "recid": recm.recid,
                "datetime": recm.datetime,
                "note": recm.note,
                "recStatus": recm.recStatus.statusname,
                "studentid": recm.student.userid,
                "studentname": recm.student.userName,
                "tutorid": recm.tutor.userid,
                "tutorname": recm.tutor.userName
            });
        });

        setRecommendations(correctedRecommendations);
    }

    // get user info (get the recommendations of the logged in user)
    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        setUserID(userID);
        setUserRole(userRole);

        if (userRole === "manager" || userRole === "admin")
        {
            getAllRecommendations();
        }
        else if (userRole === "tutor")
        {
            getTutorRecommendations(userID);
            getAllUsers();
        }
        else
        {
            setErrorMsg("You are not allowed to view recommendations");
            setErrorShow(true);
        }
    }


    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        getUserInfo()
    }, []);


    const [showAddDialog, setShowAddDialog] = useState(false);

    const [studentToRecommend, setStudentToRecommend] = useState(null);
    const [recmText, setRecmText] = useState("");

    const handleAddClickOpen = () =>
    {
        setShowAddDialog(true);
    };
    const handleAddClose = () =>
    {
        setShowAddDialog(false);

        setStudentToRecommend(null);
        setRecmText("");
    };
    const handleAddSave = () =>
    {
        if (studentToRecommend !== null && recmText !== "null")
        {
            setShowAddDialog(false);
            createSubmit();
        }
        else
        {
            setErrorMsg("please fill in all data");
            setErrorShow(true);
        }
    };


    function createSubmit()
    {
        const recmToSubmit = {"datetime": moment(), "note": recmText, "student": {"userid": studentToRecommend.userID}, "tutor": {"userid": userID}};

        console.log(recmToSubmit);

        submitRecommendation(recmToSubmit);
    }

    // add recommendation submit
    async function submitRecommendation(recmToSubmit)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(recmToSubmit)};

            await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/recommendation`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
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
                        setSuccessMsg("Recommendation added");
                        setSuccessShow(true);
                        getTutorRecommendations(userID);

                        // clear all the things in add
                        setStudentToRecommend(null);
                        setRecmText("");
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
        inputData: recommendations,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;

    const OPTIONS_LIMIT = 30;
    const defaultFilterOptions = createFilterOptions();

    const filterOptions = (options, state) => {
        return defaultFilterOptions(options, state).slice(0, OPTIONS_LIMIT);
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
                <Typography variant="h4">Recommendations</Typography>

                <div>
                    {
                        userRole === "tutor" &&
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} onClick={handleAddClickOpen}>
                            New Recommendation
                        </Button>
                    }

                    <ExportToExcel data={dataFiltered} filename="Recommendations List"/>
                </div>
            </Stack>

            <Card>
                <RecommendationsTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={recommendations.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'studentid', label: 'Student'},
                                    {id: 'datetime', label: 'Date'},
                                    {id: 'recStatus', label: 'Status'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <RecommendationsTableRow
                                            recID={row.recid}
                                            dateTime={row.datetime}
                                            note={row.note}
                                            recStatus={row.recStatus}
                                            studentID={row.studentid}
                                            studentName={row.studentname}
                                            tutorID={row.tutorid}
                                            tutorName={row.tutorname}
                                            userRole={userRole}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, recommendations.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={recommendations.length}
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
                        Add New Recommendation
                    </DialogTitle>
                    <DialogContent>
                        <div style={{marginTop: "5px"}}>
                            <Autocomplete
                                filterOptions={filterOptions}
                                options={allUsers}
                                getOptionLabel={(option) => option.userID + " | " + option.userName}
                                value={studentToRecommend}
                                onChange={(event, newValue) =>
                                {
                                    setStudentToRecommend(newValue)
                                }}
                                sx={{width: '100%', mt: 1}}
                                renderInput={(params) => <TextField {...params} label="Student"/>}
                            />
                            <FormHelperText>Student you want to recommend. please note that the student may already be a pass leader</FormHelperText>

                            <TextField label="Note" multiline rows={2} fullWidth sx={{mt: 2}} value={recmText} onChange={(event) =>
                            {
                                setRecmText(event.target.value)
                            }}/>
                            <FormHelperText>why you think this student is fit?.</FormHelperText>
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
