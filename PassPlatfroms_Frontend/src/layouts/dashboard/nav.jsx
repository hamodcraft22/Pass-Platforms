import {useEffect, useState} from 'react';
import PropTypes from 'prop-types';

import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Drawer from '@mui/material/Drawer';
import {alpha} from '@mui/material/styles';
import ListItemButton from '@mui/material/ListItemButton';

import {usePathname} from '../../routes/hooks';
import {RouterLink} from '../../routes/components';

import {useResponsive} from '../../hooks/use-responsive';

import Logo from '../../components/logo';
import Scrollbar from '../../components/scrollbar';

import {NAV} from './config-layout';
import navConfig from './config-navigation';
import studentNav from './student-navs';
import {Divider} from '@mui/material';
import UserProfile from "../../components/auth/UserInfo";
import adminNav from "./admin-navs";
import leaderNav from "./leader-navs";
import managerNavs from "./manager-navs";
import tutorNavs from "./tutor-navs";

// ----------------------------------------------------------------------

export default function Nav({openNav, onCloseNav})
{
    const pathname = usePathname();

    const upLg = useResponsive('up', 'lg');

    const [userRole, setUserRole] = useState("");

    async function getUserInfo()
    {
        let userRole = await UserProfile.getUserRole();

        await setUserRole(userRole);
    }

    useEffect(() =>
    {
        getUserInfo()
    }, []);

    useEffect(() =>
    {
        if (openNav)
        {
            onCloseNav();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pathname]);


    const renderMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2, mt: 4}}>
            {navConfig.map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );


    // student menu
    const studentMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2}}>
            {studentNav.map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );

    // admin menu
    const adminMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2}}>
            {adminNav.slice(0, 4).map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
            <Divider sx={{mt: 2, mb: 2}}/>
            {adminNav.slice(4).map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );

    // leaders menu
    const leaderMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2}}>
            {leaderNav.slice(0, 6).map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
            <Divider sx={{mt: 2, mb: 2}}/>
            {leaderNav.slice(6).map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );

    // leaders menu
    const managerMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2}}>
            {managerNavs.map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );

    // tutor menu
    const tutorMenu = (
        <Stack component="nav" spacing={0.5} sx={{px: 2}}>
            {tutorNavs.map((item) => (
                <NavItem key={item.title} item={item}/>
            ))}
        </Stack>
    );

    const renderContent = (
        <Scrollbar
            sx={{
                height: 1,
                '& .simplebar-content': {
                    height: 1,
                    display: 'flex',
                    flexDirection: 'column',
                },
            }}
        >
            <Logo sx={{mt: 3, ml: 4}}/>

            {renderMenu}

            <Divider sx={{mt: 2, mb: 2}}/>

            {
                userRole === "student" &&
                <>{studentMenu}</>
            }

            {
                userRole === "admin" &&
                <>{adminMenu}</>
            }

            {
                userRole === "leader" &&
                <>{leaderMenu}</>
            }

            {
                userRole === "manager" &&
                <>{managerMenu}</>
            }

            {
                userRole === "tutor" &&
                <>{tutorMenu}</>
            }

            <Box sx={{flexGrow: 1}}/>

        </Scrollbar>
    );

    return (
        <Box
            sx={{
                flexShrink: {lg: 0},
                width: {lg: NAV.WIDTH},
            }}
        >
            {upLg ? (
                <Box
                    sx={{
                        height: 1,
                        position: 'fixed',
                        width: NAV.WIDTH,
                        borderRight: (theme) => `dashed 1px ${theme.palette.divider}`,
                    }}
                >
                    {renderContent}
                </Box>
            ) : (
                <Drawer
                    open={openNav}
                    onClose={onCloseNav}
                    PaperProps={{
                        sx: {
                            width: NAV.WIDTH,
                        },
                    }}
                >
                    {renderContent}
                </Drawer>
            )}
        </Box>
    );
}

Nav.propTypes = {
    openNav: PropTypes.bool,
    onCloseNav: PropTypes.func,
};

// ----------------------------------------------------------------------

function NavItem({item})
{
    const pathname = usePathname();

    let active;

    // custom paths and active sessions
    if (item.path === "/schools" && pathname === "/courses")
    {
        active = true;
    }
    else if (item.path === "/bookings" && pathname === "/viewBooking")
    {
        active = true;
    }
    else if (item.path === "/revisions" && pathname === "/viewRevision")
    {
        active = true;
    }
    else
    {
        active = item.path === pathname;
    }

    return (
        <ListItemButton
            component={RouterLink}
            href={item.path}
            sx={{
                minHeight: 44,
                borderRadius: 0.75,
                typography: 'body2',
                color: 'text.secondary',
                textTransform: 'capitalize',
                fontWeight: 'fontWeightMedium',
                ...(active && {
                    color: 'primary.main',
                    fontWeight: 'fontWeightSemiBold',
                    bgcolor: (theme) => alpha(theme.palette.primary.main, 0.08),
                    '&:hover': {
                        bgcolor: (theme) => alpha(theme.palette.primary.main, 0.16),
                    },
                }),
            }}
        >
            <Box component="span" sx={{width: 24, height: 24, mr: 2}}>
                {item.icon}
            </Box>

            <Box component="span">{item.title} </Box>
        </ListItemButton>
    );
}

NavItem.propTypes = {
    item: PropTypes.object,
};
