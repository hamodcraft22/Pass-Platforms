import EditCalendarTwoToneIcon from "@mui/icons-material/EditCalendarTwoTone";
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
