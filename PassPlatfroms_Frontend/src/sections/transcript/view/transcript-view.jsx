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
import DialogActions from "@mui/material/DialogActions";
import extractTextFromPdf from '../extractTextFromPdf';
import extractCoursesFromText from '../extractCoursesFromText';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import {styled} from '@mui/material/styles';
import TableHead from "@mui/material/TableHead";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";


// ----------------------------------------------------------------------

export default function TranscriptPage() {

    const [schoolParm, setSchoolParm] = useSearchParams();
    schoolParm.get("schoolID")

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


    const [showAddDialog, setShowAddDialog] = useState(false);


    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);

        setCourses([]);
        setText("");
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
    };

    const [text, setText] = useState('');
    const [courses, setCourses] = useState([]);


    const handleFileChange = async (e) => {
        const file = e.target.files[0];

        if (file) {
            try {
                const pdfText = await extractTextFromPdf(file);
                setText(pdfText);

                // Define your regular expression for course extraction
                const courseRegex = /([A-Z]{2})\s+(\d+)\s+([A-Z0-9]+)\s+([A-Za-z0-9\s\-]+?)\s+(\S+)\s+(\d+\.\d+)\s+(\d+\.\d+)/gm;

                const extractedCourses = extractCoursesFromText(pdfText, courseRegex);
                setCourses(extractedCourses);
            } catch (error) {
                console.error(error.message);
            }
        }
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
                <Typography variant="h4">Courses</Typography>

                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                        onClick={handleAddClickOpen}>
                    Upload Transcript
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
                                rowCount={users.length}
                                numSelected={selected.length}
                                onRequestSort={handleSort}
                                onSelectAllClick={handleSelectAllClick}
                                headLabel={[
                                    {id: '', label: ''},
                                    {id: 'course', label: 'Course'},
                                    {id: 'grade', label: 'Grade', align: 'center'},
                                    {id: '', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <CoursesTableRow
                                            courseID={row.userid}
                                            name={row.name}
                                            grade={row.grade}
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


                {/* Add dialog */}
                <Dialog
                    open={showAddDialog}
                    onClose={handleAddClose}
                >
                    <DialogTitle>
                        Upload Transcript File
                    </DialogTitle>
                    <DialogContent>
                        <Button component="label" variant="contained" startIcon={<CloudUploadIcon/>} fullWidth>
                            Upload file
                            <VisuallyHiddenInput type="file" onChange={handleFileChange} accept=".pdf"/>
                        </Button>

                        {
                            Object.keys(courses).length !== 0 &&
                            <TableContainer component={CustomPaper} sx={{mt: 3}}>
                                <Table aria-label="simple table" sx={{minWidth: "200px"}}>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Course</TableCell>
                                            <TableCell align="center">Grade</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {courses.map((course, index) => (
                                            <TableRow
                                                key={index}
                                                sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                            >
                                                <TableCell component="th" scope="row">
                                                    {course.code}{course.number}
                                                </TableCell>
                                                <TableCell align="center">{course.grade}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        }
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