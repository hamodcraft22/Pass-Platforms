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

import SchoolsTableRow from '../schools-table-row';
import SchoolsTableToolbar from '../schools-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import DialogActions from "@mui/material/DialogActions";
import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";
import {read, utils} from 'xlsx';
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import UserProfile from "../../../components/auth/UserInfo";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";
import {useNavigate} from "react-router-dom";
import ExportToExcel from "../../../utils/exportExcel";



export default function SchoolsPage() {

    // this page is used by everyone, and editing is only allowed for admins/ managers

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

    // schools
    const [schools, setSchools] = useState([]);

    // get schools api
    async function getSchools() {
        try {
            setLoadingShow(true);
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`http://localhost:8080/api/school`, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then((data) => {
                    setSchools(data.transObject)
                })
                .then(() => {
                    setLoadingShow(false)
                })

        } catch (error) {
            setLoadingShow(false);
            console.log(error);
        }
    }

    // get all schools on load
    useEffect(() => {
        getSchools()
    }, [])




    // table vars
    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    // table functions
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
        inputData: schools,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;

    const [showAddDialog, setShowAddDialog] = useState(false);


    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        await setUserID(userID);
        await setUserRole(userRole);
    }

    // get school and courses on load - if not leader and there is param
    useEffect(() => {getUserInfo()}, []);


    // excel extract
    const [schoolsUpload, setSchoolsUpload] = useState([]);

    const handleFileChange = async (event) => {
        const file = event.target.files[0];

        const workbook = await readFile(file);
        const sheetNames = workbook.SheetNames;
        let sheetsData = [];

        sheetNames.forEach((sheetName) => {
            const sheet = workbook.Sheets[sheetName];
            const data = utils.sheet_to_json(sheet, {header: 1});
            const courses = data.slice(1);

            let formattedCourses = [];

            courses.forEach((course) => {
                formattedCourses.push({"courseid": sheetName + course[0], "coursename": course[1], "school":{"schoolid": sheetName}})
            });

            sheetsData.push({"schoolid": sheetName, "schoolname": data[0][0], "courses": formattedCourses});
        });

        setSchoolsUpload(sheetsData);
    };

    const readFile = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();

            reader.onload = (e) => {
                const data = new Uint8Array(e.target.result);
                const workbook = read(data, {type: 'array'});
                resolve(workbook);
            };

            reader.onerror = (error) => {
                reject(error);
            };

            reader.readAsArrayBuffer(file);
        });
    };

    // upload school elements
    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
        submitBooking();
    };


    async function submitBooking()
    {
        let isok = false;
        let isBad = false;

        try {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(schoolsUpload)};

            await fetch(`http://localhost:8080/api/school/multi`, requestOptions)
                .then(response => {
                    if (response.status === 201 || response.status === 200) {
                        isok = true;
                        return response.json();
                    } else if (response.status === 400) {
                        isBad = true;
                        return response.json();
                    } else if (response.status === 401) {
                        setErrorMsg("you are not allowed to do this action");
                        setErrorShow(true);
                    } else if (response.status === 404) {
                        setErrorMsg("the request was not found on the server, double check your connection");
                        setErrorShow(true);
                    } else {
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then((data) => {
                    setLoadingShow(false);
                    if (isok)
                    {
                        // it is fine, go on
                        getSchools()
                            .then(() => {
                                setSuccessMsg("Courses added, duplicates (if any) ignored");
                                setSuccessShow(true);
                                console.log(data);
                            })
                    }
                    else if (isBad)
                    {
                        setErrorMsg("Unkown error, some schools or courses may have not been uploaded");
                        setErrorShow(true);
                        console.log(data);
                    }
                    else
                    {
                        console.log(data);
                    }
                })

        } catch (error) {
            setErrorMsg("an unknown error occurred, please check console");
            setErrorShow(true);
            console.log(error);
            setLoadingShow(false);
        }
    }



    // naviagation links
    let navigate = useNavigate();
    const goToNewSchool = () => {
            let path = `/newSchool`;
            navigate(path);

    }


    const VisuallyHiddenInput = styled('input')({
        clip: 'rect(0 0 0 0)',
        clipPath: 'inset(50%)',
        height: 1,
        overflow: 'hidden',
        position: 'absolute',
        bottom: 0,
        left: 0,
        whiteSpace: 'nowrap',
        width: 1,
    });

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
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
                <Alert onClose={handleAlertClose} severity="error" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {errorMsg}
                </Alert>
            </Snackbar>

            <Snackbar open={successShow} autoHideDuration={6000} onClose={handleAlertClose}
                      anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
                <Alert onClose={handleSuccessAlertClose} severity="success" sx={{width: '100%', whiteSpace: 'pre-line'}}>
                    {successMsg}
                </Alert>
            </Snackbar>

            {/* tob bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Schools</Typography>

                <div>
                {
                    (userRole === 'admin' || userRole === 'manager') &&
                    <>
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} sx={{m: 1}} onClick={handleAddClickOpen}>
                            Upload Schools
                        </Button>

                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} sx={{m: 1}} onClick={goToNewSchool}>
                            New School
                        </Button>
                    </>
                }

                    <ExportToExcel data={dataFiltered} filename="Schools List"/>
                </div>
            </Stack>

            <Card>
                <SchoolsTableToolbar
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={schools.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'schoolname', label: 'Name'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <SchoolsTableRow
                                            schoolID={row.schoolid}
                                            schoolName={row.schoolname}
                                            role={userRole}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, schools.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={schools.length}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handleChangePage}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Card>

            {/* upload dialog */}
            <Dialog
                open={showAddDialog}
                onClose={handleAddClose}
            >
                <DialogTitle>
                    Upload Schools Sheet
                </DialogTitle>
                <DialogContent>
                    <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
                        Upload file
                        <VisuallyHiddenInput type="file" onChange={handleFileChange} accept=".xlsx"/>
                    </Button>

                    {
                        Object.keys(schoolsUpload).length !== 0 &&
                        <>
                            {schoolsUpload.map((school) => (
                                <TableContainer component={CustomPaper} sx={{mt: 3}}>
                                    <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell colspan="2" style={{"text-align": "center"}}><b>{school.schoolid + " | " + school.schoolname}</b></TableCell>
                                            </TableRow>
                                            <TableRow>
                                                <TableCell>Course Code</TableCell>
                                                <TableCell align="center">Course Name</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {
                                                school.courses.map((course, index) => (
                                                    <TableRow
                                                        key={index}
                                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                    >
                                                        <TableCell align="center" component="th" scope="row">
                                                            {course.courseid}
                                                        </TableCell>
                                                        <TableCell>{course.coursename}</TableCell>
                                                    </TableRow>
                                                ))
                                            }
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            ))}
                        </>
                    }
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleAddClose}>Cancel</Button>
                    <Button onClick={handleAddSave} autoFocus>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

        </Container>
    );
}
