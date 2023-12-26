import {Helmet} from 'react-helmet-async';

import {NewSchoolView} from '../sections/newSchool/view';

// ----------------------------------------------------------------------

export default function SchoolsPage()
{
    return (
        <>
            <Helmet>
                <title> New School </title>
            </Helmet>

            <NewSchoolView/>
        </>
    );
}
