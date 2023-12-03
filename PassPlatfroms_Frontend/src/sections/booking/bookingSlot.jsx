import React from 'react';
import PropTypes from 'prop-types';
import Box from "@mui/material/Box";
import PublicIcon from '@mui/icons-material/Public';

const propTypes = {
    start: PropTypes.object.isRequired,
    end: PropTypes.object.isRequired,
    color: PropTypes.string.isRequired,
};


class bookingSlot extends React.PureComponent {
    render() {
        const {
            start,
            end,
            online,
            color
        } = this.props;
        return (
            <div className="slotEvent" style={{ padding:5}}>
                <Box
                    sx={{
                        width: '100%',
                        height: '100%',
                        borderRadius: 1.5,
                        bgcolor: `${color}`, '&:hover': {bgcolor: `#b3b3b3`},
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'center',
                    }}
                >
                    {online && <PublicIcon></PublicIcon>}
                    <span style={{ fontSize:"0.7em", textAlign:"center"}}>{start.format('h:mm a')} - {end.format('h:mm a')}</span>
                </Box>
            </div>
        );
    }
}

bookingSlot.propTypes = propTypes;
export default bookingSlot;
