import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import WeekCalendar from 'react-week-calendar';
import moment from "moment";
import Toolbar from "@mui/material/Toolbar";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Iconify from "../../../components/iconify";
import {Alert, Autocomplete, CardContent, CircularProgress, Divider, FormHelperText, ListItem, ListItemIcon, Snackbar, TextField} from "@mui/material";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import LinearProgress from '@mui/material/LinearProgress';
import InputAdornment from "@mui/material/InputAdornment";
import {AccountCircle} from "@mui/icons-material";
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import Grid from "@mui/material/Unstable_Grid2";
import EmailIcon from '@mui/icons-material/Email';
import CardHeader from "@mui/material/CardHeader";
import Scrollbar from "../../../components/scrollbar/scrollbar";
import Link from "@mui/material/Link";
import {fToNow} from "../../../utils/format-time";
import SendRoundedIcon from '@mui/icons-material/SendRounded';


// ----------------------------------------------------------------------

export default function ViewApplicationPage()
{

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (


        <Container>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Student - Application</Typography>
            </Stack>

            {/* elements */}
            {/*select course and school card */}
            {
                <Grid container spacing={3}>
                    {/* Left Card */}
                    <Grid xs={12} md={6} lg={4}>
                        <Card sx={{ backgroundColor: '#f5f5f5' }}>
                            <CardContent>
                                <Typography variant="h6">Booking Information</Typography>

                                <TextField label="Student" variant="standard" fullWidth sx={{ mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>
                                <TextField label="Application Date" variant="standard" fullWidth sx={{ mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>
                                <TextField label="Application Note" variant="standard" fullWidth sx={{ mt: 2}} multiline InputProps={{readOnly: true}} defaultValue={"asd"}/>

                                {/* if manager */}
                                <Button variant="contained" startIcon={<EmailIcon />} href={"mailto:test@example.com"} sx={{ mt: 2}}>
                                    Email Student
                                </Button>

                                {/* if student */}
                                <Button variant="contained" startIcon={<EmailIcon />} href={"mailto:test@example.com"} sx={{ mt: 2}}>
                                    Email Manager
                                </Button>
                            </CardContent>
                        </Card>
                    </Grid>

                    {/* Right Card */}
                    <Grid xs={12} md={6} lg={8}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6" sx={{ mb:2 }}>Booking Notes</Typography>

                                {/* loop to display all notes */}
                                <Card sx={{mb: 2, backgroundColor: '#fafff8'}} >
                                        <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                            <Box sx={{minWidth: 240, flexGrow: 1}}>
                                                <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                    {"User"}
                                                </Link>

                                                <Typography variant="caption" sx={{color: 'text.secondary', ml:2}} noWrap>
                                                    {"Date time"}
                                                </Typography>

                                                <Typography variant="body2" sx={{color: 'text.secondary', mt:1}} noWrap>
                                                    {"Text for the note"}
                                                </Typography>
                                            </Box>

                                            <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                                Role
                                            </Typography>
                                        </Stack>
                                </Card>

                                <Card sx={{mb: 2, backgroundColor: '#fffcf8'}} >
                                    <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                        <Box sx={{minWidth: 240, flexGrow: 1}}>
                                            <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                                {"User"}
                                            </Link>

                                            <Typography variant="caption" sx={{color: 'text.secondary', ml:2}} noWrap>
                                                {"Date time"}
                                            </Typography>

                                            <Typography variant="body2" sx={{color: 'text.secondary', mt:1}} noWrap>
                                                {"Text for the note"}
                                            </Typography>
                                        </Box>

                                        <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                            Role
                                        </Typography>
                                    </Stack>
                                </Card>

                                <Divider />

                                <Typography variant="h6" sx={{ mt:2, mb:2 }}>Post a Note:</Typography>
                                <TextField fullWidth label="Note" variant="outlined" multiline minRows={2}/>
                                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                                    <Button variant="contained" endIcon={<SendRoundedIcon />} sx={{ mt: 2 }}>
                                        Submit
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            }

        </Container>
    );
}
