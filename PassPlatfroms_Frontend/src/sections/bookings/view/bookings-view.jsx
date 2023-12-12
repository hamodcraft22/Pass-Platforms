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

import TableNoData from '../../../components/table/table-no-data';
import TableMainHead from '../../../components/table/table-head';
import TableEmptyRows from '../../../components/table/table-empty-rows';

import BookingsTableRow from '../bookings-table-row';
import BookingsTableToolbar from '../bookings-table-toolbar';

import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";


// ----------------------------------------------------------------------

export default function RecommendationsPage() {
    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [selected, setSelected] = useState([]);

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);


    // fake users

    const users = [...Array(24)].map((_, index) => ({
        userid: 567,
        avatarUrl: `/assets/images/avatars/avatar_${index + 1}.jpg`,
        name: "faker.person.fullName()",
        role: "Leader"
    }));


    const handleSort = (event, id) => {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '') {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
    };

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = users.map((n) => n.name);
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
        inputData: users,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;


    return (
        <Container>
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4"> Student Name - Bookings</Typography>

                {/* only show if a student */}
                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}>
                    New Booking
                </Button>
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
                                rowCount={users.length}
                                numSelected={selected.length}
                                onRequestSort={handleSort}
                                onSelectAllClick={handleSelectAllClick}
                                headLabel={[
                                    {id: '', label: ''},
                                    {id: 'subject', label: 'Subject'},
                                    {id: 'date', label: 'Date'},
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
                                            subject={row.course}
                                            student={row.student}
                                            date={row.date}
                                            status={row.status}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, users.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={users.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />

            </Card>
        </Container>
    );
}
