import Toolbar from '@mui/material/Toolbar';

// ----------------------------------------------------------------------

export default function SlotTableToolbar()
{
    return (
        <Toolbar
            sx={{
                height: 30,
                display: 'flex',
                justifyContent: 'space-between',
                p: (theme) => theme.spacing(0, 1, 0, 3)
            }}
        >

        </Toolbar>
    );
}

SlotTableToolbar.propTypes = {};
