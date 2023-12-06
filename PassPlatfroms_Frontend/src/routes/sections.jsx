import {lazy, Suspense} from 'react';
import {Navigate, Outlet, useRoutes} from 'react-router-dom';

import DashboardLayout from '../layouts/dashboard';

export const IndexPage = lazy(() => import('../pages/app'));
export const UserPage = lazy(() => import('../pages/user'));
export const Page404 = lazy(() => import('../pages/page-not-found'));
export const NewBookingPage = lazy(() => import('../pages/newBooking'));
export const NewRevisionPage = lazy(() => import('../pages/newRevision'));
export const NewSchoolPage = lazy(() => import('../pages/newSchool'));
export const SchoolsPage = lazy(() => import('../pages/schools'));
export const CoursesPage = lazy(() => import('../pages/courses'));

// ----------------------------------------------------------------------

export default function Router() {
    const routes = useRoutes([
        {
            element: (
                <DashboardLayout>
                    <Suspense>
                        <Outlet/>
                    </Suspense>
                </DashboardLayout>
            ),
            children: [
                {element: <IndexPage/>, index: true},
                {path: 'user', element: <UserPage/>},
                {path: 'newBooking', element: <NewBookingPage/>},
                {path: 'newRevision', element: <NewRevisionPage/>},
                {path: 'newSchool', element: <NewSchoolPage/>},
                {path: 'schools', element: <SchoolsPage/>},
                {path: 'courses', element: <CoursesPage/>}
            ],
        },
        {
            path: '404',
            element: <Page404/>,
        },
        {
            path: '*',
            element: <Navigate to="/404" replace/>,
        },
    ]);

    return routes;
}
