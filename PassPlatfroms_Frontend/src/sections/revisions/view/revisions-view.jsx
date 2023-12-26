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
import TableMainHead from '../../../components/table/table-head';
import TableEmptyRows from '../../../components/table/table-empty-rows';

import RevisionsTableRow from '../revisions-table-row';
import RevisionsTableToolbar from '../revisions-table-toolbar';

import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {useNavigate} from "react-router-dom";
import UserProfile from "../../../components/auth/UserInfo";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";
import ExportToExcel from "../../../utils/exportExcel";


// ----------------------------------------------------------------------

export default function RevisionsPage()
{

    // this page is available for everyone other than tutors

    const queryParameters = new URLSearchParams(window.location.search)
    const studentIDParm = queryParameters.get("studentID");
    const leaderIDParm = queryParameters.get("leaderID");

    const [loadingShow, setLoadingShow] = useState(false);

    // alerts elements
    const [errorShow, setErrorShow] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");
    const handleAlertClose = (event, reason) =>
    {
        setErrorShow(false);
    };


    // fake revisions

    const [revisions, setRevisions] = useState([]);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");


    // types
    // myRevisions -- revisions the user is a part of
    // leaderRevisions -- revisions the leader is offering

    const [typeSelection, setTypeSelection] = useState("myRevisions");

    function parseRevisions(revisionsDto)
    {
        let parssedRevisions = [];

        revisionsDto.forEach((booking) =>
        {
            parssedRevisions.push({
                "bookingid": booking.bookingid,
                "bookingDate": booking.bookingDate,
                "starttime": booking.starttime,
                "endtime": booking.endtime,
                "status": booking.bookingStatus.statusname,
                "subject": booking.course.courseid + " " + booking.course.coursename,
                "online": booking.isonline
            });
        });

        console.log(parssedRevisions);

        setRevisions(parssedRevisions);
    }

    async function getRevisions(userID, revisionType)
    {
        let isok = false;

        setLoadingShow(true);

        setRevisions([]);

        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            let urlPath = "";

            if (revisionType === "myRevisions")
            {
                urlPath = `https://zift.ddnsfree.com:5679/api/revision/student/${userID}`
            }
            else if (revisionType === "leaderRevisions")
            {
                urlPath = `https://zift.ddnsfree.com:5679/api/revision/leader/${userID}`
            }

            await fetch(urlPath, requestOptions)
                .then((response) =>
                {
                    if (response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 204)
                    {
                        setErrorMsg("no revisions found")
                        setErrorShow(true);
                    }
                    else if (response.status === 401)
                    {
                        setErrorMsg("you are unauthorized to access the selected information");
                        setErrorShow(true);
                    }
                    else
                    {
                        console.log(response);
                        setErrorMsg("Unknown error, please check logs");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    if (isok)
                    {
                        parseRevisions(data.transObject);
                    }
                })
        }
        catch (error)
        {
            setErrorMsg("Unknown error, please check console");
            setErrorShow(true);
            console.log(error);
        }
        finally
        {
            setLoadingShow(false);
        }
    }


    async function getUserInfo()
    {
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
                getRevisions(studentIDParm, "myRevisions");
            }
            else if (leaderIDParm !== null)
            {
                // get booking the leader has
                getRevisions(leaderIDParm, "leaderRevisions");
            }
            else
            {
                setErrorMsg("No student id supplied!");
                setErrorShow(true);
            }
        }
        else if (userRole === "student")
        {
            // get student bookings
            getRevisions(userID, typeSelection);
        }
        else if (userRole === "leader")
        {
            // get leader bookings
            getRevisions(userID, typeSelection);
        }
        else
        {
            setErrorMsg("you are not allowed to access Bookings");
            setErrorShow(true);
        }

    }

    function changeType(bookingType)
    {
        setTypeSelection(bookingType);

        // call api
        getRevisions(userID, bookingType);
    }

    useEffect(() =>
    {
        getUserInfo()
    }, []);

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);


    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    const handleStartDate = (dateValue) =>
    {
        setPage(0);
        setStartDate(dateValue);
    };

    const handleEndDate = (dateValue) =>
    {
        setPage(0);
        setEndDate(dateValue);
    };

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


    const dataFiltered = applyFilter({
        inputData: revisions,
        comparator: getComparator(order, orderBy),
        startDate,
        endDate
    });

    let navigate = useNavigate();
    const goToNewRevision = () =>
    {
        let path = `/newRevision`;
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


            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4"> Student Name - Revisions</Typography>

                <div>
                    {/* only show if a leader */}
                    {
                        userRole === 'leader' &&
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} onClick={() =>
                        {
                            goToNewRevision()
                        }}>
                            New Revision
                        </Button>
                    }

                    <ExportToExcel data={dataFiltered} filename="Revisions List"/>
                </div>
            </Stack>

            {/* leader selection bar - only show if a leader - switch between my bookings & leader bookings / */}
            {
                userRole === 'leader' &&
                <Card sx={{mb: 2, p: 2, display: 'flex', justifyContent: 'space-around'}}>
                    <Button variant="contained" disabled={typeSelection === 'myRevisions'} onClick={() =>
                    {
                        changeType("myRevisions")
                    }}>My Revisions</Button>
                    <Button variant="contained" disabled={typeSelection === 'leaderRevisions'} onClick={() =>
                    {
                        changeType("leaderRevisions")
                    }}>Offered Revisions</Button>
                </Card>
            }


            <Card>
                <RevisionsTableToolbar
                    startDate={startDate}
                    onDateStart={handleStartDate}
                    endDate={endDate}
                    onDateEnd={handleEndDate}
                    onClearButton={() =>
                    {
                        handleStartDate(null);
                        handleEndDate(null);
                    }}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={revisions.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'subject', label: 'Subject'},
                                    {id: 'bookingDate', label: 'Date'},
                                    {id: 'starttime', label: 'Start Time'},
                                    {id: 'endtime', label: 'End Time'},
                                    {id: 'status', label: 'Status'},
                                    {id: 'online', label: ''},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <RevisionsTableRow
                                            bookingID={row.bookingid}
                                            subject={row.subject}
                                            date={row.bookingDate}
                                            startTime={row.starttime}
                                            endTime={row.endtime}
                                            status={row.status}
                                            online={row.online}
                                            viewType={typeSelection}
                                            userType={userRole}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, revisions.length)}
                                />
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={revisions.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />

            </Card>
        </Container>
    );
}
