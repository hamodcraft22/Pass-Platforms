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

import BookingsTableRow from '../bookings-table-row';
import BookingsTableToolbar from '../bookings-table-toolbar';

import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import ExportToExcel from "../../../utils/exportExcel";
import UserProfile from "../../../components/auth/UserInfo";
import {useNavigate} from "react-router-dom";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";


// ----------------------------------------------------------------------

export default function RecommendationsPage()
{

    // this page is open for everyone / other than tutors

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


    // fake bookings
    const [bookings, setBookings] = useState([]);


    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");


    // types
    // myBookings -- bookings made by a leader
    // memberBookings -- bookings the leader is a member off
    // leaderBookings -- bookings where the leader has been booked

    const [typeSelection, setTypeSelection] = useState("myBookings");


    function parseBookings(bookingsDto)
    {
        let parssedBookings = [];

        bookingsDto.forEach((booking) =>
        {
            parssedBookings.push({
                "bookingid": booking.bookingid,
                "bookingDate": booking.bookingDate,
                "slotstarttime": booking.slot.starttime,
                "slotendtime": booking.slot.endtime,
                "realstarttime": booking.starttime,
                "realendtime": booking.endtime,
                "status": booking.bookingStatus.statusname,
                "bookingType": booking.bookingType.typename,
                "subject": booking.course.courseid + " " + booking.course.coursename,
                "online": booking.isonline
            });
        });

        setBookings(parssedBookings);
    }


    // get bookings api
    async function getBookings(userID, bookingType)
    {
        let isok = false;
        setLoadingShow(true);
        setBookings([]);
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            let urlPath = "";

            if (bookingType === "myBookings")
            {
                urlPath = `${process.env.REACT_APP_BACKEND_URL}/api/booking/student/${userID}`
            }
            else if (bookingType === "memberBookings")
            {
                urlPath = `${process.env.REACT_APP_BACKEND_URL}/api/booking/member/${userID}`
            }
            else if (bookingType === "leaderBookings")
            {
                urlPath = `${process.env.REACT_APP_BACKEND_URL}/api/booking/leader/${userID}`
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
                        setErrorMsg("no bookings found")
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
                        parseBookings(data.transObject);
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

    // user info - get bookings if leader
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
                getBookings(studentIDParm, "myBookings");
            }
            else if (leaderIDParm !== null)
            {
                // get booking the leader has
                getBookings(leaderIDParm, "leaderBookings");
            }
            else
            {
                setErrorMsg("No student id supplied!");
                setErrorShow(true);
            }
        }
        else if (userRole === "student" || userRole === "leader")
        {
            if (studentIDParm === null && leaderIDParm === null)
            {
                // get student bookings
                getBookings(userID, typeSelection);
            }
            else
            {
                setErrorMsg("you are not allowed to access others Bookings");
                setErrorShow(true);
            }
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
        getBookings(userID, bookingType);
    }

    useEffect(() =>
    {
        getUserInfo()
    }, []);


    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('desc');

    const [orderBy, setOrderBy] = useState('date');

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
        inputData: bookings,
        comparator: getComparator(order, orderBy),
        startDate,
        endDate
    });

    // go to courses
    let navigate = useNavigate();
    const goToNewBooking = () =>
    {
        let path = `/newBooking`;
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
                <Typography variant="h4">Bookings</Typography>

                {/* only show if a student */}
                <div>
                    {
                        (userRole === 'leader' || userRole === 'student') &&
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} onClick={() =>
                        {
                            goToNewBooking()
                        }}>
                            New Booking
                        </Button>
                    }

                    <ExportToExcel data={dataFiltered} filename="Bookings List"/>
                </div>

            </Stack>

            {/* selection bar - only show if a leader - switch between my bookings & leader bookings / */}

            {
                (userRole === 'leader' || userRole === 'student') &&
                <Card sx={{mb: 2, p: 2, display: 'flex', justifyContent: 'space-around'}}>
                    <Button variant="contained" disabled={typeSelection === 'myBookings'} onClick={() =>
                    {
                        changeType("myBookings")
                    }}>My Bookings</Button>
                    <Button variant="contained" disabled={typeSelection === 'memberBookings'} onClick={() =>
                    {
                        changeType("memberBookings")
                    }}>Member Bookings</Button>
                    {
                        userRole === 'leader' &&
                        <Button variant="contained" disabled={typeSelection === 'leaderBookings'} onClick={() =>
                        {
                            changeType("leaderBookings")
                        }}>Offered Bookings</Button>
                    }
                </Card>
            }


            <Card>
                <BookingsTableToolbar
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
                                rowCount={bookings.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'subject', label: 'Subject'},
                                    {id: 'bookingDate', label: 'Date'},
                                    {id: 'startTime', label: 'Start Time'},
                                    {id: 'endTime', label: 'End Time'},
                                    {id: 'status', label: 'Status'},
                                    {id: 'bookingtype', label: ''},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <BookingsTableRow
                                            bookingID={row.bookingid}
                                            subject={row.subject}
                                            date={row.bookingDate}
                                            startTime={row.realstarttime}
                                            endTime={row.realendtime}
                                            slotStartTime={row.slotstarttime}
                                            slotEndTime={row.slotendtime}
                                            status={row.status}
                                            bookingType={row.bookingType}
                                            online={row.online}
                                            viewType={typeSelection}
                                            userType={userRole}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, bookings.length)}
                                />

                            </TableBody>
                        </Table>
                    </TableContainer>


                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={bookings.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />

            </Card>
        </Container>
    );
}
