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
import GroupAddTwoToneIcon from "@mui/icons-material/GroupAddTwoTone";
import AddReactionTwoToneIcon from "@mui/icons-material/AddReactionTwoTone";
import ManageAccountsTwoToneIcon from "@mui/icons-material/ManageAccountsTwoTone";

// ----------------------------------------------------------------------


const managerNav = [
    {
        title: 'Management',
        path: '/management',
        icon: <ManageAccountsTwoToneIcon sx={{width: 1, height: 1}}/>,
    },
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
];

export default managerNav;
