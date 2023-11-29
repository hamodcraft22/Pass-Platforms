import {Helmet} from 'react-helmet-async';

import {BookingView} from '../sections/booking/view';

// ----------------------------------------------------------------------

export default function BookingPage() {
    return (
        <>
            <Helmet>
                <title> User | Minimal UI </title>
            </Helmet>

            <BookingView/>
        </>
    );
}
