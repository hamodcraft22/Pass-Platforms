import SvgColor from '../../components/svg-color';
import AccountBalanceTwoToneIcon from "@mui/icons-material/AccountBalanceTwoTone";
import EventNoteTwoToneIcon from "@mui/icons-material/EventNoteTwoTone";
import DnsTwoToneIcon from "@mui/icons-material/DnsTwoTone";
import EditCalendarTwoToneIcon from "@mui/icons-material/EditCalendarTwoTone";
import FactCheckTwoToneIcon from "@mui/icons-material/FactCheckTwoTone";
import HistoryEduTwoToneIcon from "@mui/icons-material/HistoryEduTwoTone";
import PendingActionsTwoToneIcon from "@mui/icons-material/PendingActionsTwoTone";
import BusinessCenterTwoToneIcon from "@mui/icons-material/BusinessCenterTwoTone";
import WorkHistoryTwoToneIcon from "@mui/icons-material/WorkHistoryTwoTone";

// ----------------------------------------------------------------------


const leaderNav = [
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
        title: 'Offered Courses',
        path: '/offeredCourses',
        icon: <BusinessCenterTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Slot',
        path: '/slot',
        icon: <PendingActionsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'New Revision',
        path: '/newRevision',
        icon: <WorkHistoryTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
];

export default leaderNav;
