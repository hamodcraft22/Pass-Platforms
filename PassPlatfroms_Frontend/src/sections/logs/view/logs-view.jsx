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


import LogsTableRow from '../logs-table-row';
import LogsTableToolbar from '../logs-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {useSearchParams} from "react-router-dom";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {Alert, Backdrop, CircularProgress, FormHelperText, Snackbar, TextField, ToggleButton} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import DialogActions from "@mui/material/DialogActions";
import PublicIcon from '@mui/icons-material/Public';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import DeskRoundedIcon from '@mui/icons-material/DeskRounded';
import UserProfile from "../../../components/auth/UserInfo";
import {applyFilter} from "../filterUtil";

// ----------------------------------------------------------------------

export default function LogsPage()
{

    // this page is open for admins only

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


    // user logs
    const [userLogs, setUserLogs]= useState([]);

    function parseLogs(logsDto)
    {
        let parssedLogs = [];

        logsDto.forEach((log) => {
            parssedLogs.push({"logid":log.logid, "errormsg":log.errormsg, "datetime":log.datetime, "user":log.user.userid + " " + log.user.userName});
        })

        setUserLogs(parssedLogs);
    }

    async function getLogs()
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`http://localhost:8080/api/log`, requestOptions)
                .then((response) => {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 204)
                    {
                        setErrorMsg("no Logs found");
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
                .then((data) => {
                    setLoadingShow(false);
                    if (isok)
                    {
                        parseLogs(data.transObject);
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


    // get user info (get the logs of the logged in user)
    async function getUserInfo()
    {
        let userRole = await UserProfile.getUserRole();

        if (userRole === 'admin')
        {
            getLogs();
        }
        else
        {
            setErrorMsg("You do not have access to this page");
            setErrorShow(true);
        }
    }


    // get school and courses on load - if not leader and there is param
    useEffect(() => {getUserInfo()}, []);



    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('desc');

    const [orderBy, setOrderBy] = useState('datetime');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);


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

    const handleFilterByName = (event) => {
        setPage(0);
        setFilterName(event.target.value);
    };

    const handleChangeRowsPerPage = (event) => {
        setPage(0);
        setRowsPerPage(parseInt(event.target.value, 10));
    };

    const dataFiltered = applyFilter({
        inputData: userLogs,
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
                <Typography variant="h4">Logs</Typography>
            </Stack>

            <Card>
                <LogsTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={userLogs.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'user', label: 'User'},
                                    {id: 'datetime', label: 'Date & Time', align: 'center'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <LogsTableRow
                                            logID={row.logid}
                                            user={row.user}
                                            dateTime={row.datetime}
                                            logErrorMsg={row.errormsg}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, userLogs.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}

                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={userLogs.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Card>
        </Container>
    );
}
