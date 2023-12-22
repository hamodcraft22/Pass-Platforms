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

import Label from '../../components/label';
import Iconify from '../../components/iconify';

// ----------------------------------------------------------------------

export default function UserTableRow({name, role, userid,})
{
    const [open, setOpen] = useState(null);

    const handleOpenMenu = (event) => {
        setOpen(event.currentTarget);
    };

    const handleCloseMenu = () => {
        setOpen(null);
    };

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

                <TableCell><Label color={(role === 'banned' && 'error') || 'success'}>{role}</Label></TableCell>


                <TableCell align="right">
                    <IconButton onClick={handleOpenMenu}>
                        <Iconify icon="eva:more-vertical-fill"/>
                    </IconButton>
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
                {/* change into normal buttons */}
                <MenuItem onClick={handleCloseMenu}>
                    <Iconify icon="eva:edit-fill" sx={{mr: 2}}/>
                    View
                </MenuItem>

                {
                    role === "student" &&
                    <MenuItem onClick={handleCloseMenu} sx={{color: 'info.main'}}>
                        <Iconify icon="eva:trash-2-outline" sx={{mr: 2}}/>
                        Leader-ify
                    </MenuItem>
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
