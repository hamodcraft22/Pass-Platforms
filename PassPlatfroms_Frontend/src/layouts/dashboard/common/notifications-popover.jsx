import {useState} from 'react';
import PropTypes from 'prop-types';
import {set, sub} from 'date-fns';

import Box from '@mui/material/Box';
import List from '@mui/material/List';
import Badge from '@mui/material/Badge';
import Divider from '@mui/material/Divider';
import Tooltip from '@mui/material/Tooltip';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import ListItemButton from '@mui/material/ListItemButton';

import {fToNow} from '../../../utils/format-time';

import Iconify from '../../../components/iconify';
import Scrollbar from '../../../components/scrollbar';
import ReplayCircleFilledRoundedIcon from '@mui/icons-material/ReplayCircleFilledRounded';
import UserProfile from "../../../components/auth/UserInfo";

// ----------------------------------------------------------------------

const NOTIFICATIONS = [
    {
        id: 234234,
        title: 'Your order is placed',
        description: 'waiting for shipping',
        avatar: null,
        type: 'order_placed',
        createdAt: set(new Date(), {hours: 10, minutes: 30}),
        isUnRead: true,
    },
    {
        id: 324453,
        title: "SDfsdfsdf",
        description: 'answered to your comment on the Minimal',
        avatar: '/assets/images/avatars/avatar_2.jpg',
        type: 'friend_interactive',
        createdAt: sub(new Date(), {hours: 3, minutes: 30}),
        isUnRead: true,
    },
    {
        id: 65756,
        title: 'You have new message',
        description: '5 unread messages',
        avatar: null,
        type: 'chat_message',
        createdAt: sub(new Date(), {days: 1, hours: 3, minutes: 30}),
        isUnRead: false,
    },
    {
        id: 876655,
        title: 'You have new mail',
        description: 'sent from Guido Padberg',
        avatar: null,
        type: 'mail',
        createdAt: sub(new Date(), {days: 2, hours: 3, minutes: 30}),
        isUnRead: false,
    },
    {
        id: 35452345,
        title: 'Delivery processing',
        description: 'Your order is being shipped',
        avatar: null,
        type: 'order_shipped',
        createdAt: sub(new Date(), {days: 3, hours: 3, minutes: 30}),
        isUnRead: false,
    },
];

export default function NotificationsPopover() {
    const [notifications, setNotifications] = useState([]);

    async function getNotification()
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch("http://localhost:8080/api/notification", requestOptions)
                .then((response) => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                    else {return null}
                })
                .then((data) => {
                    if (data !== null)
                    {
                        setNotifications(data.transObject);
                    }
                })
        }
        catch (error)
        {
            console.log(error);
        }
    }

    useState(() => {getNotification()}, []);

    const totalUnRead = notifications.filter((item) => item.seen === false).length;

    const [open, setOpen] = useState(null);

    const handleOpen = (event) => {
        setOpen(event.currentTarget);
    };

    const handleClose = () => {
        setOpen(null);
    };

    async function setNotficationRead(notficID)
    {
        try
        {
            let token = await UserProfile.getAuthToken();

            const requestOptions = {method: "GET", headers: {'Content-Type': 'application/json', "Authorization": token}};

            await fetch(`http://localhost:8080/api/notification/${notficID}`, requestOptions)
                .then((response) => {
                    if (response.status === 200)
                    {
                        return response.json();
                    }
                    else {return null}
                })
                .then((data) => {
                    if (data !== null)
                    {
                        getNotification();
                    }
                })
        }
        catch (error)
        {
            console.log(error);
        }
    }

    return (
        <>
            <IconButton color={open ? 'primary' : 'default'} onClick={handleOpen}>
                <Badge badgeContent={totalUnRead} color="error">
                    <Iconify width={24} icon="solar:bell-bing-bold-duotone"/>
                </Badge>
            </IconButton>

            <Popover
                open={!!open}
                anchorEl={open}
                onClose={handleClose}
                anchorOrigin={{vertical: 'bottom', horizontal: 'right'}}
                transformOrigin={{vertical: 'top', horizontal: 'right'}}
                PaperProps={{
                    sx: {
                        mt: 1.5,
                        ml: 0.75,
                        width: 360,
                    },
                }}
            >
                <Box sx={{display: 'flex', alignItems: 'center', py: 2, px: 2.5}}>
                    <Box sx={{flexGrow: 1}}>
                        <Typography variant="subtitle1">Notifications</Typography>
                        <Typography variant="body2" sx={{color: 'text.secondary'}}>
                            You have {totalUnRead} unread messages
                        </Typography>
                    </Box>

                    <Tooltip title=" Refresh">
                        <IconButton color="primary" onClick={() => {getNotification()}}>
                            <ReplayCircleFilledRoundedIcon/>
                        </IconButton>
                    </Tooltip>

                </Box>

                <Divider sx={{borderStyle: 'dashed'}}/>

                <Scrollbar sx={{height: {xs: 340, sm: 'auto'}}}>
                    <List
                        disablePadding
                        subheader={
                            <ListSubheader disableSticky sx={{py: 1, px: 2.5, typography: 'overline'}}>
                                New
                            </ListSubheader>
                        }
                    >
                        {notifications.filter((item) => item.seen === false).map((notification) => (
                            <NotificationItem key={notification.notficid} notification={notification} onReadNotifc={() => {setNotficationRead(notification.notficid)}}/>
                        ))}
                    </List>

                    <List
                        disablePadding
                        subheader={
                            <ListSubheader disableSticky sx={{py: 1, px: 2.5, typography: 'overline'}}>
                                Before that
                            </ListSubheader>
                        }
                    >
                        {notifications.filter((item) => item.seen === true).map((notification) => (
                            <NotificationItem key={notification.notficid} notification={notification}/>
                        ))}
                    </List>
                </Scrollbar>

                <Divider sx={{borderStyle: 'dashed'}}/>
            </Popover>
        </>
    );
}

// ----------------------------------------------------------------------

NotificationItem.propTypes = {
    notification: PropTypes.shape({
        createdAt: PropTypes.instanceOf(Date),
        id: PropTypes.number,
        isUnRead: PropTypes.bool,
        title: PropTypes.string,
        description: PropTypes.string,
        type: PropTypes.string
    }),
};

function NotificationItem({notification, onReadNotifc}) {
    const {title} = renderContent(notification);

    return (
        <ListItemButton
            sx={{
                py: 1.5,
                px: 2.5,
                mt: '1px',
                ...(notification.seen !== true && {
                    bgcolor: 'action.selected',
                }),
            }}
        >
            <ListItemText
                primary={title}
                secondary={
                    <Typography
                        variant="caption"
                        sx={{
                            mt: 0.5,
                            display: 'flex',
                            alignItems: 'center',
                            color: 'text.disabled',
                        }}
                    >
                        <Iconify icon="eva:clock-outline" sx={{mr: 0.5, width: 16, height: 16}}/>
                        {fToNow(notification.createdAt)}
                    </Typography>
                }
                onClick={onReadNotifc}
            />
        </ListItemButton>
    );
}

// ----------------------------------------------------------------------

function renderContent(notification) {
    const title = (
        <Typography variant="subtitle2">
            {notification.entity}
            <Typography component="span" variant="body2" sx={{color: 'text.secondary'}}>
                &nbsp; {notification.notficmsg}
            </Typography>
        </Typography>
    );

    return {
        title,
    };
}
