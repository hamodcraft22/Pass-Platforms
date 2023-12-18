import {Helmet} from 'react-helmet-async';

import {ManagementView} from '../sections/management/view';

// ----------------------------------------------------------------------

export default function ManagementPage() {
    return (
        <>
            <Helmet>
                <title> Management </title>
            </Helmet>

            <ManagementView/>
        </>
    );
}
