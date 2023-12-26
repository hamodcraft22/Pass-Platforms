import {Helmet} from 'react-helmet-async';

import {CoursesView} from '../sections/courses/view';

// ----------------------------------------------------------------------

export default function CoursesPage()
{
    return (
        <>
            <Helmet>
                <title> Courses </title>
            </Helmet>

            <CoursesView/>
        </>
    );
}
