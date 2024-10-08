import {useEffect, useState} from 'react';

import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Divider from '@mui/material/Divider';
import Popover from '@mui/material/Popover';
import {alpha} from '@mui/material/styles';
import MenuItem from '@mui/material/MenuItem';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';

import {useMsal} from '@azure/msal-react';
import UserProfile from "../../../components/auth/UserInfo";


export default function AccountPopover()
{
    const {instance} = useMsal();

    let activeAccount;

    if (instance)
    {
        activeAccount = instance.getActiveAccount();

    }

    const [open, setOpen] = useState(null);

    const handleOpen = (event) =>
    {
        setOpen(event.currentTarget);
    };

    const handleClose = () =>
    {
        setOpen(null);
    };

    const handleLogoutRedirect = () =>
    {
        // instance.logoutRedirect({account: activeAccount}).catch((error) => console.log(error));
        instance.logoutRedirect({onRedirectNavigate: (url) => {return false;}}).catch((error) => console.log(error));
    };


    const [userName, setUserName] = useState("");

    async function getUserName()
    {
        await UserProfile.getUserName().then((data) =>
        {
            const words = data.trim().split(/\s+/);
            const firstName = words[0];
            const lastName = words[words.length - 1];
            setUserName(firstName + " " + lastName);
        })
    }

    useEffect(() =>
    {
        getUserName()
    }, [])

    return (
        <>
            <IconButton
                onClick={handleOpen}
                sx={{
                    width: 40,
                    height: 40,
                    background: (theme) => alpha(theme.palette.grey[500], 0.08),
                    ...(open && {
                        background: (theme) =>
                            `linear-gradient(135deg, ${theme.palette.primary.light} 0%, ${theme.palette.primary.main} 100%)`,
                    }),
                }}
            >
                <Avatar

                    sx={{
                        width: 36,
                        height: 36,
                        border: (theme) => `solid 2px ${theme.palette.background.default}`,
                    }}
                >
                    {userName.charAt(0).toUpperCase()}
                </Avatar>
            </IconButton>

            <Popover
                open={!!open}
                anchorEl={open}
                onClose={handleClose}
                anchorOrigin={{vertical: 'bottom', horizontal: 'right'}}
                transformOrigin={{vertical: 'top', horizontal: 'right'}}
                PaperProps={{
                    sx: {
                        p: 0,
                        mt: 1,
                        ml: 0.75,
                        width: 200,
                    },
                }}
            >
                <Box sx={{my: 1.5, px: 2}}>
                    <Typography variant="subtitle2" noWrap>
                        {userName}
                    </Typography>
                </Box>

                <Divider sx={{borderStyle: 'dashed'}}/>

                <MenuItem
                    disableRipple
                    disableTouchRipple
                    onClick={handleLogoutRedirect}
                    sx={{typography: 'body2', color: 'error.main', py: 1.5}}
                >
                    Logout
                </MenuItem>
            </Popover>
        </>
    );
}
