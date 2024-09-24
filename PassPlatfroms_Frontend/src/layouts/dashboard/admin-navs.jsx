import EditCalendarTwoToneIcon from "@mui/icons-material/EditCalendarTwoTone";
import GroupAddTwoToneIcon from "@mui/icons-material/GroupAddTwoTone";
import AddReactionTwoToneIcon from "@mui/icons-material/AddReactionTwoTone";
import ManageAccountsTwoToneIcon from "@mui/icons-material/ManageAccountsTwoTone";
import DisplaySettingsTwoToneIcon from "@mui/icons-material/DisplaySettingsTwoTone";
import AssignmentLateTwoToneIcon from "@mui/icons-material/AssignmentLateTwoTone";
import SpeakerNotesTwoToneIcon from '@mui/icons-material/SpeakerNotesTwoTone';

// ----------------------------------------------------------------------


const adminNav = [
    {
        title: 'New School',
        path: '/newSchool',
        icon: <EditCalendarTwoToneIcon sx={{width: 1, height: 1}}/>,
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
    {
        title: 'Notice',
        path: '/notice',
        icon: <SpeakerNotesTwoToneIcon sx={{width: 1, height: 1}}/>,
    }
];

export default adminNav;
