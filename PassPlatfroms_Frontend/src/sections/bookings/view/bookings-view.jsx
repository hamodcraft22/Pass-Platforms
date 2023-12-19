import React, {useState} from 'react';

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


// ----------------------------------------------------------------------

export default function RecommendationsPage() {
    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [rowsPerPage, setRowsPerPage] = useState(5);


    // fake bookings
    const bookings = [
        {
            subject: 'IT6008, Unix Computers',
            date: '2016-08-21T00:00:00.000Z',
            starttime: '08:30 AM',
            endtime: '09:30 AM',
            status: 'active',
            online: true
        },
        {
            subject: 'IT6012, Data Structures',
            date: '2016-08-22T00:00:00.000Z',
            starttime: '10:00 AM',
            endtime: '11:00 AM',
            status: 'canceled',
            online: false
        },
        {
            subject: 'IT6018, Web Development',
            date: '2016-08-23T00:00:00.000Z',
            starttime: '02:00 PM',
            endtime: '03:00 PM',
            status: 'finished',
            online: true
        },
        {
            subject: 'IT6025, Database Management',
            date: '2016-08-24T00:00:00.000Z',
            starttime: '09:30 AM',
            endtime: '10:30 AM',
            status: 'active',
            online: false
        },
        {
            subject: 'IT6031, Software Engineering',
            date: '2016-08-25T00:00:00.000Z',
            starttime: '11:30 AM',
            endtime: '12:30 PM',
            status: 'finished',
            online: true
        },
        {
            subject: 'IT6038, Artificial Intelligence',
            date: '2016-08-26T00:00:00.000Z',
            starttime: '03:30 PM',
            endtime: '04:30 PM',
            status: 'active',
            online: true
        },
        {
            subject: 'IT6045, Networking Fundamentals',
            date: '2016-08-27T00:00:00.000Z',
            starttime: '01:00 PM',
            endtime: '02:00 PM',
            status: 'canceled',
            online: false
        },
        {
            subject: 'IT6052, Mobile App Development',
            date: '2016-08-28T00:00:00.000Z',
            starttime: '10:30 AM',
            endtime: '11:30 AM',
            status: 'finished',
            online: true
        },
        {
            subject: 'IT6062, Data Analytics',
            date: '2016-08-29T00:00:00.000Z',
            starttime: '11:00 AM',
            endtime: '12:00 PM',
            status: 'active',
            online: false
        },
        {
            subject: 'IT6071, Cybersecurity',
            date: '2016-08-30T00:00:00.000Z',
            starttime: '09:00 AM',
            endtime: '10:00 AM',
            status: 'finished',
            online: true
        },
        // Add more booking objects as needed
    ];


    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();


    const handleStartDate = (dateValue) => {
        setPage(0);
        setStartDate(dateValue);
    };

    const handleEndDate = (dateValue) => {
        setPage(0);
        setEndDate(dateValue);
    };


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


    const dataFiltered = applyFilter({
        inputData: bookings,
        comparator: getComparator(order, orderBy),
        startDate,
        endDate
    });


    return (
        <Container>
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4"> Student Name - Bookings</Typography>

                {/* only show if a student */}
                <div>
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}>
                        New Booking
                    </Button>

                    <ExportToExcel data={dataFiltered} filename="Studnet Bookings - DATE TO DATE"/>
                </div>

            </Stack>

            {/* leader selection bar - only show if a leader - switch between my bookings & leader bookings / */}
            <Card sx={{mb: 2, p: 2, display: 'flex', justifyContent: 'space-around'}}>
                <Button variant="contained" color="inherit">My Bookings</Button>
                <Button variant="contained">Student Bookings</Button>
            </Card>

            {/* student selection bar - only show if a student - switch between my bookings & member bookings / */}
            <Card sx={{mb: 2, p: 2, display: 'flex', justifyContent: 'space-around'}}>
                <Button variant="contained" color="inherit">My Bookings</Button>
                <Button variant="contained">Member Bookings</Button>
            </Card>

            <Card>
                <BookingsTableToolbar
                    startDate={startDate}
                    onDateStart={handleStartDate}
                    endDate={endDate}
                    onDateEnd={handleEndDate}
                    onClearButton={() => {
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
                                    {id: '', label: ''},
                                    {id: 'subject', label: 'Subject'},
                                    {id: 'date', label: 'Date'},
                                    {id: 'startTime', label: 'Start Time'},
                                    {id: 'endTime', label: 'End Time'},
                                    {id: 'status', label: 'Status'},
                                    {id: 'online', label: ''},
                                    {id: '', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <BookingsTableRow
                                            key={row.bookingID}
                                            subject={row.subject}
                                            date={row.date}
                                            startTime={row.starttime}
                                            endTime={row.endtime}
                                            status={row.status}
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
