import PropTypes from 'prop-types';

import Toolbar from '@mui/material/Toolbar';

// ----------------------------------------------------------------------

export default function ScheduleTableToolbar()
{
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
