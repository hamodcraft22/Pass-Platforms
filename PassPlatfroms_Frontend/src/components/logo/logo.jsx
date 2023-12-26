import PropTypes from 'prop-types';
import {forwardRef} from 'react';

import Box from '@mui/material/Box';
import Link from '@mui/material/Link';

import {RouterLink} from '../../routes/components';

// ----------------------------------------------------------------------

const Logo = forwardRef(({disabledLink = false, sx, ...other}, ref) => {


    // OR using local (public folder)
    // -------------------------------------------------------
    const logo = (
      <Box
        component="img"
        src="/assets/logo/pass_logo.svg"
        sx={{ width: 200, cursor: 'pointer', ...sx }}
      />
    );

    // const logo = (
    //     <Box
    //         ref={ref}
    //         component="div"
    //         sx={{
    //             width: 40,
    //             height: 40,
    //             display: 'inline-flex',
    //             ...sx,
    //         }}
    //         {...other}
    //     >
    //         <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    //             <path
    //                 d="M7 12H17M8 8.5C8 8.5 9 9 10 9C11.5 9 12.5 8 14 8C15 8 16 8.5 16 8.5M8 15.5C8 15.5 9 16 10 16C11.5 16 12.5 15 14 15C15 15 16 15.5 16 15.5M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
    //                 stroke="#000000" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
    //         </svg>
    //     </Box>
    // );

    if (disabledLink) {
        return logo;
    }

    return (
        <Link component={RouterLink} href="/" sx={{display: 'contents'}}>
            {logo}
        </Link>
    );
});

Logo.propTypes = {
    disabledLink: PropTypes.bool,
    sx: PropTypes.object,
};

export default Logo;
