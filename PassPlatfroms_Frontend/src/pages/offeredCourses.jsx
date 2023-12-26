import {Helmet} from 'react-helmet-async';

import {OfferedCoursesView} from '../sections/offeredCourses/view';

// ----------------------------------------------------------------------

export default function OfferedCoursesPage()
{
    return (
        <>
            <Helmet>
                <title> Offered Courses </title>
            </Helmet>

            <OfferedCoursesView/>
        </>
    );
}
