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
import {FormHelperText, TextField, ToggleButton} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import DialogActions from "@mui/material/DialogActions";
import PublicIcon from '@mui/icons-material/Public';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {TimePicker} from "@mui/x-date-pickers";
import moment from "moment/moment";

// ----------------------------------------------------------------------

export default function SlotPage() {

    const [schoolParm, setSchoolParm] = useSearchParams();
    schoolParm.get("schoolID")

    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [selected, setSelected] = useState([]);

    const [orderBy, setOrderBy] = useState('name');

    const [filterName, setFilterName] = useState('');

    const [rowsPerPage, setRowsPerPage] = useState(5);


    // fake userSlots

    const userSlots = [...Array(24)].map((_, index) => ({
        slotID: 567,
        day: 'S',
        startTime: "mock Time",
        endTime: "mock time",
        note: "some text",
        isOnline: true
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
            const newSelecteds = userSlots.map((n) => n.name);
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
        inputData: userSlots,
        comparator: getComparator(order, orderBy),
        filterName,
    });

    const notFound = !dataFiltered.length && !!filterName;


    const [showAddDialog, setShowAddDialog] = useState(false);


    const [addSlotDay, setAddSlotDay] = useState(null);
    const [addSlotOnline, setAddSlotOnline] = useState(false);

    const [slotStartTime, setSlotStartTime] = useState(moment({h: 8, m: 0}));
    const [slotEndTime, setSlotEndTime] = useState(moment({h: 22, m: 0}));

    const [addSlotNote, setAddSlotNote] = useState(null);

    const [slotSelectedStartTime, setSlotSelectedStartTime] = useState();
    const [slotSelectedEndTime, setSlotSelectedEndTime] = useState();

    const handleAddClickOpen = () => {
        setShowAddDialog(true);
    };
    const handleAddClose = () => {
        setShowAddDialog(false);

        setSlotSelectedStartTime(null);
        setSlotSelectedEndTime(null);
        setAddSlotDay(null);
        setAddSlotOnline(null);
    };
    const handleAddSave = () => {
        setShowAddDialog(false);
    };


    return (
        <Container>
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Slots</Typography>

                <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                        onClick={handleAddClickOpen}>
                    Add Slot
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
                                rowCount={userSlots.length}
                                numSelected={selected.length}
                                onRequestSort={handleSort}
                                onSelectAllClick={handleSelectAllClick}
                                headLabel={[
                                    {id: '', label: ''},
                                    {id: 'day', label: 'Day'},
                                    {id: 'startTime', label: 'Start Time', align: 'center'},
                                    {id: 'endTime', label: 'EndT Time', align: 'center'},
                                    {id: '', label: '', align: 'center'},
                                    {id: '', label: ''}
                                ]}
                            />
                            <TableBody>
                                {dataFiltered
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <CoursesTableRow
                                            slotID={row.slotID}
                                            day={row.day}
                                            startTime={row.startTime}
                                            endTime={row.endTime}
                                            note={row.note}
                                            isOnline={row.isOnline}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, userSlots.length)}
                                />

                                {notFound && <TableNoData query={filterName}/>}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Scrollbar>

                <TablePagination
                    page={page}
                    component="div"
                    count={userSlots.length}
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
                        Add New Slot
                    </DialogTitle>
                    <DialogContent>

                        <TextField select label="Day" sx={{width: '100%', mt: 1}} value={addSlotDay}
                                   onChange={(event, newValue) => {
                                       setAddSlotDay(newValue.props.value)
                                   }}>
                            <MenuItem value={'U'}>Sunday</MenuItem>
                            <MenuItem value={'M'}>Monday</MenuItem>
                            <MenuItem value={'T'}>Tuesday</MenuItem>
                            <MenuItem value={'W'}>Wednesday</MenuItem>
                            <MenuItem value={'R'}>Thursday</MenuItem>
                            <MenuItem value={'F'}>Friday</MenuItem>
                            <MenuItem value={'S'}>Saturday</MenuItem>
                        </TextField>
                        <FormHelperText>Day of which you will offer a slot.</FormHelperText>

                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 2, mr: 1}} label="Start Time" minTime={slotStartTime}
                                        maxTime={slotEndTime} value={slotSelectedStartTime} onChange={(newValue) => {
                                setSlotSelectedStartTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 2}} label="End Time" minTime={slotStartTime} maxTime={slotEndTime}
                                        value={slotSelectedEndTime} onChange={(newValue) => {
                                setSlotSelectedEndTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <FormHelperText>Select start and end time for the revision session.</FormHelperText>

                        <TextField sx={{width: '100%', mt: 1}} label="Slot Note" variant="outlined" multiline
                                   rows={2} value={addSlotNote}
                                   onChange={(newValue) => setAddSlotNote(newValue.target.value)}/>

                        <FormHelperText sx={{ml: 2}}>Online</FormHelperText>
                        <ToggleButton
                            value={addSlotOnline}
                            selected={addSlotOnline}
                            sx={{width: '100%'}}
                            color={"primary"}
                            onChange={() => {
                                setAddSlotOnline(!addSlotOnline)
                            }}
                        >
                            <PublicIcon/>
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
