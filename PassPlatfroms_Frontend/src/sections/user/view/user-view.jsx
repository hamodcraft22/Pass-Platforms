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

import UserTableRow from '../user-table-row';
import UserTableToolbar from '../user-table-toolbar';
import UserProfile from "../../../components/auth/UserInfo";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";
import {object} from "prop-types";
import ExportToExcel from "../../../utils/exportExcel";

// ----------------------------------------------------------------------

export default function UserPage()
{
    const queryParameters = new URLSearchParams(window.location.search);
    const schoolIDParm = queryParameters.get("schoolID");
    const courseIDParm = queryParameters.get("courseID");

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

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('desc');

    const [orderBy, setOrderBy] = useState('userName');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    // users list
    const [users, setUsers] = useState([]);

    const [parsedUsers, setParsedUsers] = useState([]);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);
    }

    useEffect(() =>
    {
        getUserInfo()
    }, []);

    // get users api
    async function getAllUsers()
    {
        try
        {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            let url = `${process.env.REACT_APP_BACKEND_URL}/api/user`;

            if (schoolIDParm !== null)
            {
                url = `${process.env.REACT_APP_BACKEND_URL}/api/user/school/${schoolIDParm}`
            }
            else if (courseIDParm !== null)
            {
                url = `${process.env.REACT_APP_BACKEND_URL}/api/user/course/${courseIDParm}`
            }

            await fetch(url, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 201)
                    {
                        return response.json()
                    }
                    else
                    {
                        setErrorMsg("No Users Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        if (Object.keys(data.transObject).length !== 0)
                        {
                            setUsers(data.transObject)
                        }
                        else
                        {
                            setErrorMsg("No Users Found");
                            setErrorShow(true);
                        }
                    }
                })

        }
        catch (error)
        {
            console.log(error)
        }
        finally
        {
            setLoadingShow(false);
        }
    }

    function parseUsers()
    {
        let parsedUsers = [];

        users.forEach((user) =>
        {
            parsedUsers.push({"userid": user.userid, "userName": user.userName, "roleName": user.role.rolename})
        });

        setParsedUsers(parsedUsers);
    }

    // get all schools on load
    useEffect(() =>
    {
        getAllUsers()
    }, []);

    // parse users
    useEffect(() =>
    {
        if (Object.keys(users).length !== 0)
        {
            parseUsers()
        }
    }, [users]);

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

    const handleFilter = (event) =>
    {
        setPage(0);
        setFilterName(event.target.value);
    };

    const dataFiltered = applyFilter({
        inputData: parsedUsers,
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
                <Typography variant="h4">Users</Typography>

                {
                    (schoolIDParm !== null || courseIDParm !== null) &&
                    <ExportToExcel data={dataFiltered} filename="Leaders List"/>
                }
            </Stack>

            <Card>
                <UserTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilter}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={parsedUsers.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'leftm', label: ''},
                                    {id: 'userName', label: 'Name'},
                                    {id: 'userid', label: 'User ID'},
                                    {id: 'roleName', label: 'Role'},
                                    {id: 'rightm', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row, idx) => (
                                        <UserTableRow
                                            key={idx}
                                            name={row.userName}
                                            userid={row.userid}
                                            role={row.roleName}
                                            loggedRole={userRole}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, parsedUsers.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={parsedUsers.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Card>
        </Container>
    );
}
