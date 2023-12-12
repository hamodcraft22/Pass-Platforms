import {Helmet} from 'react-helmet-async';

import {RevisionsView} from '../sections/revisions/view';

// ----------------------------------------------------------------------

export default function RevisionsPage() {
    return (
        <>
            <Helmet>
                <title> Revisions </title>
            </Helmet>

            <RevisionsView/>
        </>
    );
}
