import PropTypes from 'prop-types';

import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputAdornment from '@mui/material/InputAdornment';

import Iconify from '../../components/iconify';

// ----------------------------------------------------------------------

export default function ScheduleTableToolbar({numSelected, filterName, onFilterName}) {
    return (
        <Toolbar
            sx={{
                height: 30,
                display: 'flex',
                justifyContent: 'space-between',
                p: (theme) => theme.spacing(0, 1, 0, 3),
            }}
        >


        </Toolbar>
    );
}

ScheduleTableToolbar.propTypes = {
    numSelected: PropTypes.number,
    filterName: PropTypes.string,
    onFilterName: PropTypes.func,
};
