import {Helmet} from 'react-helmet-async';

import {RevisionRegistrationView} from '../sections/revisionRegistration/view';

// ----------------------------------------------------------------------

export default function RevisionRegistrationPage()
{
    return (
        <>
            <Helmet>
                <title> Revision Registration </title>
            </Helmet>

            <RevisionRegistrationView/>
        </>
    );
}
