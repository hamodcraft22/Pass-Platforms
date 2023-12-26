import {Helmet} from 'react-helmet-async';

import {LogsView} from '../sections/logs/view';

// ----------------------------------------------------------------------

export default function LogsPage()
{
    return (
        <>
            <Helmet>
                <title> Logs </title>
            </Helmet>

            <LogsView/>
        </>
    );
}
