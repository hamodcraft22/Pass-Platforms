import AnalyticsTwoToneIcon from '@mui/icons-material/AnalyticsTwoTone';
import AccountBoxTwoToneIcon from '@mui/icons-material/AccountBoxTwoTone';
import AccountBalanceTwoToneIcon from '@mui/icons-material/AccountBalanceTwoTone';


const navConfig = [
    {
        title: 'home',
        path: '/',
        icon: <AnalyticsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'users',
        path: '/user',
        icon: <AccountBoxTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Schools',
        path: '/schools',
        icon: <AccountBalanceTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    // {
    //     title: 'Profile',
    //     path: '/profile',
    //     icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
    // },
    // {
    //     title: 'Unscheduled Booking',
    //     path: '/unscheduledBooking',
    //     icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
    // },
    // {
    //     title: 'about',
    //     path: '/404',
    //     icon: icon('ic_disabled'),
    // },
];

export default navConfig;
