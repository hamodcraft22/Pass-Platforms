import {Helmet} from 'react-helmet-async';

import {NewRevisionView} from '../sections/newRevision/view';

// ----------------------------------------------------------------------

export default function NewBookingPage()
{
    return (
        <>
            <Helmet>
                <title> New Revision </title>
            </Helmet>

            <NewRevisionView/>
        </>
    );
}
