import {lazy, Suspense} from 'react';
import {Navigate, Outlet, useRoutes} from 'react-router-dom';

import DashboardLayout from '../layouts/dashboard';

export const IndexPage = lazy(() => import('../pages/app'));
export const UserPage = lazy(() => import('../pages/user'));
export const Page404 = lazy(() => import('../pages/page-not-found'));
export const BookingPage = lazy(() => import('../pages/booking'));

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
                {path: 'booking', element: <BookingPage/>}
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