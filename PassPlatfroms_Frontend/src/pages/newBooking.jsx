import {Helmet} from 'react-helmet-async';

import {NewBookingView} from '../sections/newBooking/view';

// ----------------------------------------------------------------------

export default function NewBookingPage() {
    return (
        <>
            <Helmet>
                <title> New Booking </title>
            </Helmet>

            <NewBookingView/>
        </>
    );
}
