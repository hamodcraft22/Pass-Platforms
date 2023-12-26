import AnalyticsTwoToneIcon from '@mui/icons-material/AnalyticsTwoTone';
import AccountBoxTwoToneIcon from '@mui/icons-material/AccountBoxTwoTone';
import EditCalendarTwoToneIcon from '@mui/icons-material/EditCalendarTwoTone';
import WorkHistoryTwoToneIcon from '@mui/icons-material/WorkHistoryTwoTone';
import SchoolTwoToneIcon from '@mui/icons-material/SchoolTwoTone';
import HistoryEduTwoToneIcon from '@mui/icons-material/HistoryEduTwoTone';
import BallotTwoToneIcon from '@mui/icons-material/BallotTwoTone';
import BusinessCenterTwoToneIcon from '@mui/icons-material/BusinessCenterTwoTone';
import PendingActionsTwoToneIcon from '@mui/icons-material/PendingActionsTwoTone';
import EventNoteTwoToneIcon from '@mui/icons-material/EventNoteTwoTone';
import GroupAddTwoToneIcon from '@mui/icons-material/GroupAddTwoTone';
import AddReactionTwoToneIcon from '@mui/icons-material/AddReactionTwoTone';
import SupervisedUserCircleTwoToneIcon from '@mui/icons-material/SupervisedUserCircleTwoTone';
import AccountBalanceTwoToneIcon from '@mui/icons-material/AccountBalanceTwoTone';
import HowToRegTwoToneIcon from '@mui/icons-material/HowToRegTwoTone';
import ManageAccountsTwoToneIcon from '@mui/icons-material/ManageAccountsTwoTone';
import LibraryBooksTwoToneIcon from '@mui/icons-material/LibraryBooksTwoTone';
import DnsTwoToneIcon from '@mui/icons-material/DnsTwoTone';
import FactCheckTwoToneIcon from '@mui/icons-material/FactCheckTwoTone';
import CollectionsBookmarkTwoToneIcon from '@mui/icons-material/CollectionsBookmarkTwoTone';
import VerifiedUserTwoToneIcon from '@mui/icons-material/VerifiedUserTwoTone';
import RunningWithErrorsTwoToneIcon from '@mui/icons-material/RunningWithErrorsTwoTone';
import AssignmentLateTwoToneIcon from '@mui/icons-material/AssignmentLateTwoTone';
import DisplaySettingsTwoToneIcon from '@mui/icons-material/DisplaySettingsTwoTone';

// ----------------------------------------------------------------------

// const icon = (name) => (
//     <SvgColor src={`/assets/icons/navbar/${name}.svg`} sx={{width: 1, height: 1}}/>
// );


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
        title: 'New Booking',
        path: '/newBooking',
        icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'New Revision',
        path: '/newRevision',
        icon: <WorkHistoryTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Schools',
        path: '/schools',
        icon: <AccountBalanceTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'New School',
        path: '/newSchool',
        icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Courses',
        path: '/courses',
        icon: <BallotTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Offered Courses',
        path: '/offeredCourses',
        icon: <BusinessCenterTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Transcript',
        path: '/transcript',
        icon: <HistoryEduTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Slot',
        path: '/slot',
        icon: <PendingActionsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Schedule',
        path: '/schedule',
        icon: <EventNoteTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Recommendations',
        path: '/recommendations',
        icon: <GroupAddTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Applications',
        path: '/applications',
        icon: <AddReactionTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'View Application',
        path: '/viewApplication',
        icon: <SupervisedUserCircleTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Bookings',
        path: '/bookings',
        icon: <DnsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'View Booking',
        path: '/viewBooking',
        icon: <LibraryBooksTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Revisions',
        path: '/revisions',
        icon: <DnsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'View Revision',
        path: '/viewRevision',
        icon: <CollectionsBookmarkTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Revision Registration',
        path: '/revisionRegistration',
        icon: <FactCheckTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Management',
        path: '/management',
        icon: <ManageAccountsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Audits',
        path: '/audits',
        icon: <DisplaySettingsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
    {
        title: 'Logs',
        path: '/logs',
        icon: <AssignmentLateTwoToneIcon sx={{width: 1, height: 1}}/>,
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
