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

import TableNoData from '../table-no-data';
import CoursesTableRow from '../courses-table-row';
import CoursesTableHead from '../courses-table-head';
import TableEmptyRows from '../table-empty-rows';
import CoursesTableToolbar from '../courses-table-toolbar';
import {applyFilter, emptyRows, getComparator} from '../utils';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {useSearchParams} from "react-router-dom";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {FormHelperText, TextField, ToggleButton} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import DialogActions from "@mui/material/DialogActions";

// ----------------------------------------------------------------------

export default function CoursesPage() {

    const queryParameters = new URLSearchParams(window.location.search)
    const schoolIDParm = queryParameters.get("schoolID")

    const [school, setSchool] = useState([]);
    const [courses, setCourses] = useState([]);

    // get school and courses api
    async function getSchoolCourses()
    {
        try
        {
            const requestOptions = {method: "GET", headers: { 'Content-Type': 'application/json' }};

            await fetch(`http://localhost:8080/api/school/${schoolIDParm}`, requestOptions)
                .then(response => {return response.json()})
                .then((data) => {setSchool(data.transObject); setCourses(data.transObject.courses)})
        }
        catch (error)
        {
            console.log(error)
        }
    }

    // get school and courses on load
    useEffect(() => {getSchoolCourses()}, [])

    const [showAddDialog, setShowAddDialog] = useState(false);
    const [addCourseID, setAddCourseID] = useState(null);
    const [addCourseName, setAddCourseName] = useState(null);
    const [addCourseDesc, setAddCourseDesc] = useState(null);
    const [addCourseSem, setAddCourseSem] = useState(null);
    const [addCourseAvalb, setAddCourseAvalb] = useState(false);

    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);

        setAddCourseID(null);
        setAddCourseName(null);
        setAddCourseDesc(null);
        setAddCourseSem(null);
        setAddCourseAvalb(null);
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
    };


    // table vars and functions

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [selected, setSelected] = useState([]);

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);

    const handleSort = (event, id) => {
        const isAsc = orderBy === id && order === 'asc';
        if (id !== '') {
            setOrder(isAsc ? 'desc' : 'asc');
            setOrderBy(id);
        }
    };

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = courses.map((n) => n.name);
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
        inputData: courses,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;


    return (
        <Container>
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">{school.schoolname} Courses</Typography>

                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                        onClick={handleAddClickOpen}>
                    New Course
                </Button>
            </Stack>

            <Card>
                <CoursesTableToolbar
                    numSelected={selected.length}
                    filterName={filterName}
                    onFilterName={handleFilterByName}
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <CoursesTableHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={courses.length}
                                numSelected={selected.length}
                                onRequestSort={handleSort}
                                onSelectAllClick={handleSelectAllClick}
                                headLabel={[
                                    {id: '', label: ''},
                                    {id: 'name', label: 'Name'},
                                    {id: 'semester', label: 'Semester', align: 'center'},
                                    {id: 'available', label: 'Available', align: 'center'},
                                    {id: '', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <CoursesTableRow
                                            courseID={row.courseid}
                                            courseName={row.coursename}
                                            courseDesc={row.coursedesc}
                                            sem={row.semaster}
                                            avlb={row.available}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, courses.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={courses.length}
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
                        Add New Course
                    </DialogTitle>
                    <DialogContent>
                        <TextField sx={{width: '100%', mt: 1}} label="Course Code" variant="outlined"
                                   value={addCourseID} onChange={(newValue) => setAddCourseID(newValue.target.value)}/>

                        <TextField sx={{width: '100%', mt: 1}} label="Course Name" variant="outlined"
                                   value={addCourseName}
                                   onChange={(newValue) => setAddCourseName(newValue.target.value)}/>

                        <TextField sx={{width: '100%', mt: 1}} label="Course Description" variant="outlined" multiline
                                   rows={2} value={addCourseDesc}
                                   onChange={(newValue) => setAddCourseDesc(newValue.target.value)}/>

                        <TextField
                            select
                            label="Semester"
                            sx={{width: '100%', mt: 1}}
                            value={addCourseSem}
                            onChange={(event, newValue) => {
                                setAddCourseSem(newValue.props.value)
                            }}
                        >
                            <MenuItem value={'A'}>A</MenuItem>
                            <MenuItem value={'B'}>B</MenuItem>
                            <MenuItem value={'S'}>Summer</MenuItem>
                        </TextField>

                        <FormHelperText sx={{ml: 2}}>Available</FormHelperText>
                        <ToggleButton
                            value={addCourseAvalb}
                            selected={addCourseAvalb}
                            sx={{width: '100%'}}
                            color={"primary"}
                            onChange={() => {
                                setAddCourseAvalb(!addCourseAvalb)
                            }}
                        >
                            <CheckBoxIcon/>
                        </ToggleButton>

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