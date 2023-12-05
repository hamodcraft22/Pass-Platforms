import {Helmet} from 'react-helmet-async';

import {SchoolsView} from '../sections/schools/view';

// ----------------------------------------------------------------------

export default function SchoolsPage() {
    return (
        <>
            <Helmet>
                <title> Schools </title>
            </Helmet>

            <SchoolsView/>
        </>
    );
}
