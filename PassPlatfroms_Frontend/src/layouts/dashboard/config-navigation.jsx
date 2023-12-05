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
