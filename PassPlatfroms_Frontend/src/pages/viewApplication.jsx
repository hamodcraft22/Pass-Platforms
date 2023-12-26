import {Helmet} from 'react-helmet-async';

import {ViewApplicationView} from '../sections/viewApplication/view';

// ----------------------------------------------------------------------

export default function ViewApplicationPage()
{
    return (
        <>
            <Helmet>
                <title> View Application </title>
            </Helmet>

            <ViewApplicationView/>
        </>
    );
}
