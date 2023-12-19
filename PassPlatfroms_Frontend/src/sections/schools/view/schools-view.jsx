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


// ----------------------------------------------------------------------

export default function SchoolsPage() {

    // schools
    const [schools, setSchools] = useState([]);


    // get schools api
    async function getSchools() {
        try {
            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json'}};

            await fetch(`http://localhost:8080/api/school`, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then((data) => {
                    setSchools(data.transObject)
                })
        } catch (error) {
            console.log(error)
        }
    }

    // get all schools on load
    useEffect(() => {
        getSchools()
    }, [])

    // table vars
    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [selected, setSelected] = useState([]);

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

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = schools.map((n) => n.name);
            setSelected(newSelecteds);
            return;
        }
        setSelected([]);
    };

    const handleClick = (event, name) => {
        const selectedIndex = selected.indexOf(name);
        let newSelected = [];
        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, name);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1)
            );
        }
        setSelected(newSelected);
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


    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
    };

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
                formattedCourses.push({"courseCode": sheetName + course[0], "courseName": course[1]})
            });

            sheetsData.push({"schoolCode": sheetName, "schoolName": data[0][0], "courses": formattedCourses});
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
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Schools</Typography>


                <div>
                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} sx={{m: 1}} onClick={handleAddClickOpen}>
                        Upload Schools
                    </Button>

                    <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>} sx={{m: 1}}>
                        New School
                    </Button>
                </div>
            </Stack>

            <Card>
                <SchoolsTableToolbar
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
                                rowCount={schools.length}
                                numSelected={selected.length}
                                onRequestSort={handleSort}
                                onSelectAllClick={handleSelectAllClick}
                                headLabel={[
                                    {id: '', label: ''},
                                    {id: 'name', label: 'Name'},
                                    {id: '', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <SchoolsTableRow
                                            schoolID={row.schoolid}
                                            schoolName={row.schoolname}
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
                                                <TableCell colspan="2" style={{"text-align": "center"}}><b>{school.schoolName}</b></TableCell>
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
                                                            {course.courseCode}
                                                        </TableCell>
                                                        <TableCell>{course.courseName}</TableCell>
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
