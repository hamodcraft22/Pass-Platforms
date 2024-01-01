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
import TableMainHead from '../../../components/table/table-head';
import TableEmptyRows from '../../../components/table/table-empty-rows';
import {emptyRows} from '../../../components/table/utils';


import SlotTableRow from '../slot-table-row';
import SlotTableToolbar from '../slot-table-toolbar';
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
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
import ExportToExcel from "../../../utils/exportExcel";

// ----------------------------------------------------------------------

export default function SlotPage()
{

    // this page is open for everyone, editing is only for the manager / user

    const queryParameters = new URLSearchParams(window.location.search)
    const leaderIDParm = queryParameters.get("leaderID");

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

    const [successShow, setSuccessShow] = useState(false);
    const [successMsg, setSuccessMsg] = useState("");
    const handleSuccessAlertClose = (event, reason) =>
    {
        if (reason === 'clickaway')
        {
            return;
        }
        setSuccessShow(false);
    };

    const [canEdit, setCanEdit] = useState(false);

    // api related items

    // user slots
    const [userSlots, setUserSlots] = useState([]);

    const [userID, setUserID] = useState("");
    const [userRole, setUserRole] = useState("");

    async function getSlots(leaderID)
    {
        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', 'Authorization': token}};

            await fetch(`https://backend.zift.ddnsfree.com/api/slot/leader/${leaderID}`, requestOptions)
                .then(response =>
                {
                    if (response.status === 200 || response.status === 201)
                    {
                        return response.json();
                    }
                    else
                    {
                        setErrorMsg("No Slots Found");
                        setErrorShow(true);
                        return null;
                    }
                })
                .then((data) =>
                {
                    if (data !== null)
                    {
                        parseUserSlots(data.transObject);
                    }
                }).then(() =>
                {
                    setLoadingShow(false);
                })
        }
        catch (error)
        {
            setLoadingShow(false);
            setErrorMsg("No Slots Found");
            setErrorShow(true);
            console.log(error);
        }
    }

    function parseUserSlots(retrivedUserSlots)
    {
        let correctedUserSlots = [];

        retrivedUserSlots.forEach((slot) =>
        {
            correctedUserSlots.push({"slotid": slot.slotid, "starttime": slot.starttime, "endtime": slot.endtime, "note": slot.note, "type": slot.slotType.typename, "day": slot.day.dayid, "userName": slot.leader.userName});
        });

        setUserSlots(correctedUserSlots);
    }

    // get user info (get the slots of the logged in user)
    async function getUserInfo()
    {
        let userID = await UserProfile.getUserID();
        let userRole = await UserProfile.getUserRole();

        setUserID(userID);
        setUserRole(userRole);

        setCanEdit(true);

        getSlots(userID);
    }


    // get school and courses on load - if not leader and there is param
    useEffect(() =>
    {
        if (leaderIDParm !== null && leaderIDParm !== undefined && Object.keys(leaderIDParm).length !== 0)
        {
            getSlots(leaderIDParm);
            setCanEdit(false);
        }
        else
        {
            getUserInfo()
        }
    }, [])


    const [showAddDialog, setShowAddDialog] = useState(false);


    const [addSlotDay, setAddSlotDay] = useState(null);
    const [addSlotOnline, setAddSlotOnline] = useState(false);
    const [addSlotPhysical, setAddSlotPhysical] = useState(false);


    const [addSlotNote, setAddSlotNote] = useState(null);

    const [slotSelectedStartTime, setSlotSelectedStartTime] = useState(null);
    const [slotSelectedEndTime, setSlotSelectedEndTime] = useState(null);

    const handleAddClickOpen = () =>
    {
        setShowAddDialog(true);
    };
    const handleAddClose = () =>
    {
        setShowAddDialog(false);

        setSlotSelectedStartTime(null);
        setSlotSelectedEndTime(null);
        setAddSlotNote(null);
        setAddSlotDay(null);
        setAddSlotOnline(false);
        setAddSlotPhysical(false);
    };
    const handleAddSave = () =>
    {
        if (slotSelectedStartTime !== null && slotSelectedEndTime !== null && addSlotDay !== null && (addSlotOnline !== false || addSlotPhysical !== false))
        {
            setShowAddDialog(false);
            createSubmit();
        }
        else
        {
            setErrorMsg("please fill in all data");
            setErrorShow(true);
        }
    };


    // submit new slot
    function createSubmit()
    {
        let slotType = '';

        if (addSlotOnline && addSlotPhysical)
        {
            slotType = 'B';
        }
        else if (addSlotOnline)
        {
            slotType = 'O'
        }
        else if (addSlotPhysical)
        {
            slotType = 'P'
        }

        const slotToSubmit = {"starttime": slotSelectedStartTime, "endtime": slotSelectedEndTime, "note": addSlotNote, "slotType": {"typeid": slotType}, "day": {"dayid": addSlotDay}, "leader": {"userid": userID}};

        submitSlot(slotToSubmit);
    }

    // add offered courses api
    async function submitSlot(slotToSubmit)
    {
        let isok = false;

        try
        {
            setLoadingShow(true);

            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "POST", headers: {'Content-Type': 'application/json', "Authorization": token}, body: JSON.stringify(slotToSubmit)};

            await fetch(`https://backend.zift.ddnsfree.com/api/slot`, requestOptions)
                .then((response) =>
                {
                    if (response.status === 201 || response.status === 200)
                    {
                        isok = true;
                        return response.json();
                    }
                    else if (response.status === 400)
                    {
                        setErrorMsg("Slot clashes with another slot time");
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
                        setErrorMsg("an unknown error occurred, please check console");
                        setErrorShow(true);
                    }
                })
                .then((data) =>
                {
                    setLoadingShow(false);
                    if (isok)
                    {
                        // refresh transcript
                        setSuccessMsg("Slots added");
                        setSuccessShow(true);
                        getSlots(userID);

                        // clear all the things in add
                        setSlotSelectedStartTime(null);
                        setSlotSelectedEndTime(null);
                        setAddSlotNote(null);
                        setAddSlotDay(null);
                        setAddSlotOnline(null);
                        setAddSlotPhysical(null);
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


    const [page, setPage] = useState(0);

    const [order, setOrder] = useState('asc');

    const [orderBy, setOrderBy] = useState('name');

    const [rowsPerPage, setRowsPerPage] = useState(5);


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
                <Typography variant="h4">{userSlots && Object.keys(userSlots).length !== 0 && <>{userSlots[0].userName}</>} Slots</Typography>

                <div>
                    {
                        leaderIDParm === null && userRole === "leader" &&
                        <Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}
                                onClick={handleAddClickOpen}>
                            Add Slot
                        </Button>
                    }

                    <ExportToExcel data={userSlots} filename="Slots List"/>
                </div>
            </Stack>

            <Card>
                <SlotTableToolbar
                />

                <Scrollbar>
                    <TableContainer sx={{overflow: 'unset'}}>
                        <Table sx={{minWidth: 800}}>
                            <TableMainHead
                                order={order}
                                orderBy={orderBy}
                                rowCount={userSlots.length}
                                onRequestSort={handleSort}
                                headLabel={[
                                    {id: 'zift1', label: ''},
                                    {id: 'day', label: 'Day'},
                                    {id: 'starttime', label: 'Start Time', align: 'center'},
                                    {id: 'endtime', label: 'EndT Time', align: 'center'},
                                    {id: 'type', label: '', align: 'center'},
                                    {id: 'zift2', label: ''}
                                ]}
                            />
                            <TableBody>
                                {userSlots
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row) => (
                                        <SlotTableRow
                                            slotID={row.slotid}
                                            day={row.day}
                                            startTime={row.starttime}
                                            endTime={row.endtime}
                                            note={row.note}
                                            type={row.type}
                                            canEdit={canEdit}
                                        />
                                    ))}

                                <TableEmptyRows
                                    height={77}
                                    emptyRows={emptyRows(page, rowsPerPage, userSlots.length)}
                                />

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
                                   onChange={(event, newValue) =>
                                   {
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
                            <TimePicker sx={{mt: 2, mr: 1}} label="Start Time" value={slotSelectedStartTime} onChange={(newValue) =>
                            {
                                setSlotSelectedStartTime(newValue)
                            }}/>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterMoment}>
                            <TimePicker sx={{mt: 2}} label="End Time" minTime={slotSelectedStartTime}
                                        value={slotSelectedEndTime} onChange={(newValue) =>
                            {
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
                            onChange={() =>
                            {
                                setAddSlotOnline(!addSlotOnline)
                            }}
                        >
                            <PublicIcon/>
                        </ToggleButton>

                        <FormHelperText sx={{ml: 2}}>Physical</FormHelperText>
                        <ToggleButton
                            value={addSlotPhysical}
                            selected={addSlotPhysical}
                            sx={{width: '100%'}}
                            color={"primary"}
                            onChange={() =>
                            {
                                setAddSlotPhysical(!addSlotPhysical)
                            }}
                        >
                            <DeskRoundedIcon/>
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
