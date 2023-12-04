import {Helmet} from 'react-helmet-async';

import {NewBookingView} from '../sections/newBooking/view';

// ----------------------------------------------------------------------

export default function BookingPage() {
    return (
        <>
            <Helmet>
                <title> User | Minimal UI </title>
            </Helmet>

            <NewBookingView/>
        </>
    );
}
