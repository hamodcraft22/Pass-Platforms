import SvgColor from '../../components/svg-color';
import AccountBalanceTwoToneIcon from "@mui/icons-material/AccountBalanceTwoTone";
import EventNoteTwoToneIcon from "@mui/icons-material/EventNoteTwoTone";
import DnsTwoToneIcon from "@mui/icons-material/DnsTwoTone";
import EditCalendarTwoToneIcon from "@mui/icons-material/EditCalendarTwoTone";
import FactCheckTwoToneIcon from "@mui/icons-material/FactCheckTwoTone";
import HistoryEduTwoToneIcon from "@mui/icons-material/HistoryEduTwoTone";
import SupervisedUserCircleTwoToneIcon from "@mui/icons-material/SupervisedUserCircleTwoTone";

// ----------------------------------------------------------------------


const studentNav = [
    {
        title: 'Transcript',
        path: '/transcript',
        icon: <HistoryEduTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Schedule',
        path: '/schedule',
        icon: <EventNoteTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Bookings',
        path: '/bookings',
        icon: <DnsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Revisions',
        path: '/revisions',
        icon: <DnsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'New Booking',
        path: '/newBooking',
        icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Revision Registration',
        path: '/revisionRegistration',
        icon: <FactCheckTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Become a leader',
        path: '/viewApplication',
        icon: <SupervisedUserCircleTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
];

export default studentNav;
