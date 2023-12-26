import {Helmet} from 'react-helmet-async';

import {ViewBookingView} from '../sections/viewBooking/view';

// ----------------------------------------------------------------------

export default function ViewBookingPage()
{
    return (
        <>
            <Helmet>
                <title> View Booking </title>
            </Helmet>

            <ViewBookingView/>
        </>
    );
}
