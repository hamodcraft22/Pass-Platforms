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

import ApplicationsTableRow from '../applications-table-row';
import ApplicationsTableToolbar from '../applications-table-toolbar';

import {emptyRows, getComparator} from '../../../components/table/utils';
import {applyFilter} from '../filterUtil';
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import {Alert, Autocomplete, Backdrop, CircularProgress, Snackbar, TextField} from "@mui/material";
import UserProfile from "../../../components/auth/UserInfo";


// ----------------------------------------------------------------------

export default function ApplicationsPage() {

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


    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);


    const [applications, setApplications] = useState([]);


    // get all applications api
    async function getAllApplications()
    {
        try
        {
            let isok = false;

            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`http://localhost:8080/api/application`, requestOptions)
                .then(response => {
                    if (response.status === 200)
                    {
                        isok = true;
                        return response.json()
                    }
                    else if (response.status === 401)
                    {
                        setErrorMsg("you are not allowed to view applications");
                        setErrorShow(true);
                    }
                    else
                    {
                        console.log(response);
                    }
                })
                .then((data) => {
                    if (isok)
                    {
                        parseApplications(data.transObject);
                    }
                })
                .then(() => {
                    setLoadingShow(false);
                })

        } catch (error) {
            console.log(error);
            setLoadingShow(false);
        }
    }

    function parseApplications(applicationsToParse)
    {
        let parrsedApplicatrions = [];

        applicationsToParse.forEach((application) => {
            parrsedApplicatrions.push({"applicationid":application.applicationid, "datetime":application.datetime, "status":application.applicationStatus.statusname, "student":application.user.userid + " " +application.user.userName, "studentid":application.user.userid});
        })

        setApplications(parrsedApplicatrions);
    }

    useEffect(() => {getAllApplications()},[]);

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
        inputData: applications,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;


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
                <Typography variant="h4">Applications</Typography>
            </Stack>

            <Card>
                <ApplicationsTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={applications.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'student', label: 'Student'},
                                    {id: 'datetime', label: 'Date'},
                                    {id: 'status', label: 'Status'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <ApplicationsTableRow
                                            aplicID={row.applicationid}
                                            studentID={row.studentid}
                                            student={row.student}
                                            date={row.datetime}
                                            status={row.status}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, applications.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={applications.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Card>
        </Container>
    );
}
