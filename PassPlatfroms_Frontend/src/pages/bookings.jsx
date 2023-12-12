import {Helmet} from 'react-helmet-async';

import {BookingsView} from '../sections/bookings/view';

// ----------------------------------------------------------------------

export default function BookingsPage() {
    return (
        <>
            <Helmet>
                <title> Bookings </title>
            </Helmet>

            <BookingsView/>
        </>
    );
}
