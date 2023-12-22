import SvgColor from '../../components/svg-color';

// ----------------------------------------------------------------------

const icon = (name) => (
    <SvgColor src={`/assets/icons/navbar/${name}.svg`} sx={{width: 1, height: 1}}/>
);

const studentNav = [
    {
        title: 'Schools',
        path: '/schools',
        icon: icon('ic_user'),
    },
    {
        title: 'New Booking',
        path: '/newBooking',
        icon: icon('ic_user'),
    },
    {
        title: 'Schedule',
        path: '/schedule',
        icon: icon('ic_user'),
    },
    {
        title: 'Bookings',
        path: '/bookings',
        icon: icon('ic_user'),
    },
    {
        title: 'Revisions',
        path: '/revisions',
        icon: icon('ic_user'),
    },
    {
        title: 'Revision Registration',
        path: '/revisionRegistration',
        icon: icon('ic_user'),
    }
];

export default studentNav;
