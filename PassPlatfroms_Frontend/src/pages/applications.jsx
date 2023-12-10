import {Helmet} from 'react-helmet-async';

import {ApplicationsView} from '../sections/applications/view';

// ----------------------------------------------------------------------

export default function ApplicationsPage() {
    return (
        <>
            <Helmet>
                <title> Applications </title>
            </Helmet>

            <ApplicationsView/>
        </>
    );
}
