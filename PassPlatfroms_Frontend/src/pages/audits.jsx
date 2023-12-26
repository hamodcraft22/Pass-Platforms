import {Helmet} from 'react-helmet-async';

import {AuditsView} from '../sections/audits/view';

// ----------------------------------------------------------------------

export default function AuditsPage() {
    return (
        <>
            <Helmet>
                <title> Audits </title>
            </Helmet>

            <AuditsView/>
        </>
    );
}
