import PropTypes from 'prop-types';

import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputAdornment from '@mui/material/InputAdornment';

import Iconify from '../../components/iconify';

// ----------------------------------------------------------------------

export default function CoursesTableToolbar({filterName, onFilterName}) {
    return (
        <Toolbar
            sx={{
                height: 96,
                display: 'flex',
                justifyContent: 'space-between',
                p: (theme) => theme.spacing(0, 1, 0, 3)
            }}
        >

                <OutlinedInput
                    value={filterName}
                    onChange={onFilterName}
                    placeholder="Search Courses..."
                    startAdornment={
                        <InputAdornment position="start">
                            <Iconify
                                icon="eva:search-fill"
                                sx={{color: 'text.disabled', width: 20, height: 20}}
                            />
                        </InputAdornment>
                    }
                />

        </Toolbar>
    );
}

CoursesTableToolbar.propTypes = {
    filterName: PropTypes.string,
    onFilterName: PropTypes.func,
};
