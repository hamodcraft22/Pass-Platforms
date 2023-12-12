import {Helmet} from 'react-helmet-async';

import {ViewRevisionView} from '../sections/viewRevision/view';

// ----------------------------------------------------------------------

export default function ViewRevisionPage() {
    return (
        <>
            <Helmet>
                <title> View Revision </title>
            </Helmet>

            <ViewRevisionView/>
        </>
    );
}
