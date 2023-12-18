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
export const OfferedCoursesPage = lazy(() => import('../pages/offeredCourses'));
export const TranscriptPage = lazy(() => import('../pages/transcript'));
export const SlotPage = lazy(() => import('../pages/slot'));
export const SchedulePage = lazy(() => import('../pages/schedule'));
export const RecommendationsPage = lazy(() => import('../pages/recommendations'));
export const ApplicationsPage = lazy(() => import('../pages/applications'));
export const ViewApplicationPage = lazy(() => import('../pages/viewApplication'));
export const BookingsPage = lazy(() => import('../pages/bookings'));
export const ViewBookingPage = lazy(() => import('../pages/viewBooking'));
export const RevisionsPage = lazy(() => import('../pages/revisions'));
export const ViewRevisionPage = lazy(() => import('../pages/viewRevision'));
export const RevisionRegistrationPage = lazy(() => import('../pages/revisionRegistration'));
export const ManagementPage = lazy(() => import('../pages/management'));
export const ProfilePage = lazy(() => import('../pages/profile'));
export const UnscheduledBookingPage = lazy(() => import('../pages/unscheduledBooking'));

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
                {path: 'courses', element: <CoursesPage/>},
                {path: 'offeredCourses', element: <OfferedCoursesPage/>},
                {path: 'transcript', element: <TranscriptPage/>},
                {path: 'slot', element: <SlotPage/>},
                {path: 'schedule', element: <SchedulePage/>},
                {path: 'recommendations', element: <RecommendationsPage/>},
                {path: 'applications', element: <ApplicationsPage/>},
                {path: 'viewApplication', element: <ViewApplicationPage/>},
                {path: 'bookings', element: <BookingsPage/>},
                {path: 'viewBooking', element: <ViewBookingPage/>},
                {path: 'revisions', element: <RevisionsPage/>},
                {path: 'viewRevision', element: <ViewRevisionPage/>},
                {path: 'revisionRegistration', element: <RevisionRegistrationPage/>},
                {path: 'management', element: <ManagementPage/>},
                {path: 'profile', element: <ProfilePage/>},
                {path: 'unscheduledBooking', element: <UnscheduledBookingPage/>},
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
