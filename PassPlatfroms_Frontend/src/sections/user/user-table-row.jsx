import {useState} from 'react';
import PropTypes from 'prop-types';

import Stack from '@mui/material/Stack';
import Avatar from '@mui/material/Avatar';
import Popover from '@mui/material/Popover';
import TableRow from '@mui/material/TableRow';
import MenuItem from '@mui/material/MenuItem';
import TableCell from '@mui/material/TableCell';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import {useNavigate} from "react-router-dom";


import Label from '../../components/label';
import Iconify from '../../components/iconify';

// ----------------------------------------------------------------------

export default function UserTableRow({name, role, userid, loggedRole}) {
    const [open, setOpen] = useState(null);

    const handleOpenMenu = (event) => {
        setOpen(event.currentTarget);
    };

    const handleCloseMenu = () => {
        setOpen(null);
    };


    // naviagation links
    let navigate = useNavigate();
    const goToSchedule = () => {
        let path = `/schedule?studentID=${userid}`;
        navigate(path);
    }
    const goToSlots = () => {
        let path = `/slot?leaderID=${userid}`;
        navigate(path);
    }
    const goToCourses = () => {
        let path = `/offeredCourses?leaderID=${userid}`;
        navigate(path);
    }
    const goToTranscript = () => {
        let path = `/transcript?studentID=${userid}`;
        navigate(path);
    }
    const goToBookings = () => {
        let path = `/bookings?studentID=${userid}`;
        navigate(path);
    }
    const goToRevisions = () => {
        let path = `/revisions?studentID=${userid}`;
        navigate(path);
    }

    const goToLeaderBookings = () => {
        let path = `/bookings?leaderID=${userid}`;
        navigate(path);
    }
    const goToLeaderRevisions = () => {
        let path = `/revisions?leaderID=${userid}`;
        navigate(path);
    }

    return (
        <>
            <TableRow hover tabIndex={-1} role="checkbox">

                <TableCell>

                </TableCell>

                <TableCell component="th" scope="row" padding="none">
                    <Stack direction="row" alignItems="center" spacing={2}>
                        <Avatar alt={name}/>
                        <Typography variant="subtitle2" noWrap>
                            {name}
                        </Typography>
                    </Stack>
                </TableCell>

                <TableCell>{userid}</TableCell>

                <TableCell><Label color={(role === 'tutor' && 'secondary') || (role === 'student' && 'warning') || (role === 'leader' && 'info') || 'success'}>{role}</Label></TableCell>


                <TableCell align="right">
                    {
                        (role === 'student' || role === 'leader') &&
                        <IconButton onClick={handleOpenMenu}>
                            <Iconify icon="eva:more-vertical-fill"/>
                        </IconButton>
                    }
                </TableCell>
            </TableRow>

            <Popover
                open={!!open}
                anchorEl={open}
                onClose={handleCloseMenu}
                anchorOrigin={{vertical: 'top', horizontal: 'left'}}
                transformOrigin={{vertical: 'top', horizontal: 'right'}}
                PaperProps={{
                    sx: {width: 140},
                }}
            >
                {
                    (role === 'student' || role === 'leader') &&
                    <MenuItem onClick={goToSchedule} >
                        <Iconify icon="eva:calendar-outline" sx={{mr: 2}}/>
                        Schedule
                    </MenuItem>
                }
                {
                    role === "leader" &&
                    <>
                        <MenuItem onClick={goToSlots} >
                            <Iconify icon="eva:clock-outline" sx={{mr: 2}}/>
                            Slots
                        </MenuItem>
                        <MenuItem onClick={goToCourses} >
                            <Iconify icon="eva:briefcase-outline" sx={{mr: 2}}/>
                            Courses
                        </MenuItem>
                    </>
                }
                {
                    (role === "leader" || role === "student") && (loggedRole === 'manager' || loggedRole === 'admin') &&
                    <>
                        <MenuItem onClick={goToTranscript} >
                            <Iconify icon="eva:briefcase-outline" sx={{mr: 2}}/>
                            Transcript
                        </MenuItem>
                        <MenuItem onClick={goToBookings} >
                            <Iconify icon="eva:book-open-fill" sx={{mr: 2}}/>
                            Bookings
                        </MenuItem>
                        <MenuItem onClick={goToRevisions} >
                            <Iconify icon="eva:book-open-outline" sx={{mr: 2}}/>
                            Revisions
                        </MenuItem>
                    </>
                }
                {
                    (role === "leader") && (loggedRole === 'manager' || loggedRole === 'admin') &&
                    <>
                        <MenuItem onClick={goToLeaderBookings} >
                            <Iconify icon="eva:clipboard-fill" sx={{mr: 2}}/>
                            L Bookings
                        </MenuItem>
                        <MenuItem onClick={goToLeaderRevisions} >
                            <Iconify icon="eva:clipboard-outline" sx={{mr: 2}}/>
                            L Revisions
                        </MenuItem>
                    </>
                }
            </Popover>
        </>
    );
}

UserTableRow.propTypes = {
    name: PropTypes.any,
    role: PropTypes.any,
    userid: PropTypes.any,
};
