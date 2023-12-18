import SvgColor from '../../components/svg-color';

// ----------------------------------------------------------------------

const icon = (name) => (
    <SvgColor src={`/assets/icons/navbar/${name}.svg`} sx={{width: 1, height: 1}}/>
);

const navConfig = [
    {
        title: 'home',
        path: '/',
        icon: icon('ic_analytics'),
    },
    {
        title: 'users',
        path: '/user',
        icon: icon('ic_user'),
    },
    {
        title: 'New Booking',
        path: '/newBooking',
        icon: icon('ic_user'),
    },
    {
        title: 'New Revision',
        path: '/newRevision',
        icon: icon('ic_user'),
    },
    {
        title: 'Schools',
        path: '/schools',
        icon: icon('ic_user'),
    },
    {
        title: 'New School',
        path: '/newSchool',
        icon: icon('ic_user'),
    },
    {
        title: 'Courses',
        path: '/courses',
        icon: icon('ic_user'),
    },
    {
        title: 'Offered Courses',
        path: '/offeredCourses',
        icon: icon('ic_user'),
    },
    {
        title: 'Transcript',
        path: '/transcript',
        icon: icon('ic_user'),
    },
    {
        title: 'Slot',
        path: '/slot',
        icon: icon('ic_user'),
    },
    {
        title: 'Schedule',
        path: '/schedule',
        icon: icon('ic_user'),
    },
    {
        title: 'Recommendations',
        path: '/recommendations',
        icon: icon('ic_user'),
    },
    {
        title: 'Applications',
        path: '/applications',
        icon: icon('ic_user'),
    },
    {
        title: 'View Application',
        path: '/viewApplication',
        icon: icon('ic_user'),
    },
    {
        title: 'Bookings',
        path: '/bookings',
        icon: icon('ic_user'),
    },
    {
        title: 'View Booking',
        path: '/viewBooking',
        icon: icon('ic_user'),
    },
    {
        title: 'Revisions',
        path: '/revisions',
        icon: icon('ic_user'),
    },
    {
        title: 'View Revision',
        path: '/viewRevision',
        icon: icon('ic_user'),
    },
    {
        title: 'Revision Registration',
        path: '/revisionRegistration',
        icon: icon('ic_user'),
    },
    {
        title: 'Management',
        path: '/management',
        icon: icon('ic_user'),
    },
    {
        title: 'about',
        path: '/404',
        icon: icon('ic_disabled'),
    },
    {
        title: 'Not found',
        path: '/404',
        icon: icon('ic_disabled'),
    },
];

export default navConfig;
