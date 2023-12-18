import {Helmet} from 'react-helmet-async';

import {UnscheduledBookingView} from '../sections/unscheduledBooking/view';

// ----------------------------------------------------------------------

export default function UnscheduledBookingPage() {
    return (
        <>
            <Helmet>
                <title> Unscheduled Booking </title>
            </Helmet>

            <UnscheduledBookingView/>
        </>
    );
}
