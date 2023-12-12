import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import React, {useState} from "react";
import Button from "@mui/material/Button";
import {CardContent, Divider, FormHelperText, ListItem, ListItemIcon, TextField} from "@mui/material";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Unstable_Grid2";
import EmailIcon from '@mui/icons-material/Email';
import Link from "@mui/material/Link";
import SendRoundedIcon from '@mui/icons-material/SendRounded';
import List from "@mui/material/List";
import {AccountCircle} from "@mui/icons-material";
import ListItemText from "@mui/material/ListItemText";


// ----------------------------------------------------------------------

export default function ViewBookingPage() {
    const [groupMembers, setGroupMembers] = useState([]);

    return (


        <Container>

            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">Student - Booking</Typography>
            </Stack>

            {/* view application elements - if student with application or a manager / admin */}

            <Grid container spacing={3}>
                {/* Left Card */}
                <Grid xs={12} md={6} lg={4}>
                    <Card sx={{backgroundColor: '#f5f5f5'}}>
                        <CardContent>
                            <Typography variant="h6">Booking Information</Typography>

                            <TextField label="Student" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Leader" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Booked Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Booking Date" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Booking Day" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Booked Status" variant="standard" fullWidth sx={{mt: 2}} InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            <TextField label="Booking Note" variant="standard" fullWidth sx={{mt: 2}} multiline InputProps={{readOnly: true}} defaultValue={"asd"}/>

                            {/* show icon if online */}

                            {/* show icon if group */}

                            {/* group members - only when group */}
                            {/* loop of members - if added - maybe add name get? */}
                            {
                                groupMembers !== null && groupMembers !== undefined && Object.keys(groupMembers).length !== 0 ?
                                    (
                                        <>
                                            <FormHelperText>Members</FormHelperText>
                                            <List dense>
                                                {
                                                    groupMembers && groupMembers.map((studentID) => (
                                                        <ListItem>
                                                            <ListItemIcon><AccountCircle/></ListItemIcon>
                                                            <ListItemText primary={studentID}/>
                                                        </ListItem>
                                                    ))
                                                }
                                            </List>
                                        </>
                                    ) : (<></>)

                            }


                            {/* if student */}
                            <Button variant="contained" startIcon={<EmailIcon/>} href={"mailto:test@example.com"} sx={{mt: 2}}>
                                Email Leader
                            </Button>

                            <Button variant="contained" color={"error"} sx={{mt: 2}}>
                                Cancel Booking
                            </Button>

                            <Button variant="contained" color={"secondary"} sx={{mt: 2}}>
                                Add Members
                            </Button>

                            {/* finalize booking  - leader only - set actual start and end time, and what was discussed (as final note) */}
                            <Button variant="contained" color={"info"} sx={{mt: 2}}>
                                Complete Session
                            </Button>

                        </CardContent>
                    </Card>
                </Grid>

                {/* Right Card */}
                <Grid xs={12} md={6} lg={8}>
                    <Card>
                        <CardContent>
                            <Typography variant="h6" sx={{mb: 2}}>Booking Notes</Typography>

                            {/* loop to display all notes */}
                            <Card sx={{mb: 2, backgroundColor: '#fafff8'}}>
                                <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                    <Box sx={{minWidth: 240, flexGrow: 1}}>
                                        <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                            {"User"}
                                        </Link>

                                        <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                            {"Date time"}
                                        </Typography>

                                        <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                            {"Text for the note"}
                                        </Typography>
                                    </Box>

                                    <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                        Role
                                    </Typography>
                                </Stack>
                            </Card>

                            <Card sx={{mb: 2, backgroundColor: '#fffcf8'}}>
                                <Stack direction="row" alignItems="center" spacing={3} sx={{p: 3, pr: 0}}>
                                    <Box sx={{minWidth: 240, flexGrow: 1}}>
                                        <Link color="inherit" variant="subtitle2" underline="hover" noWrap>
                                            {"User"}
                                        </Link>

                                        <Typography variant="caption" sx={{color: 'text.secondary', ml: 2}} noWrap>
                                            {"Date time"}
                                        </Typography>

                                        <Typography variant="body2" sx={{color: 'text.secondary', mt: 1}} noWrap>
                                            {"Text for the note"}
                                        </Typography>
                                    </Box>

                                    <Typography variant="caption" sx={{pr: 3, flexShrink: 0, color: 'text.secondary'}}>
                                        Role
                                    </Typography>
                                </Stack>
                            </Card>

                            <Divider/>

                            <Typography variant="h6" sx={{mt: 2, mb: 2}}>Post a Note:</Typography>
                            <TextField fullWidth label="Note" variant="outlined" multiline minRows={2}/>
                            <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                                <Button variant="contained" endIcon={<SendRoundedIcon/>} sx={{mt: 2}}>
                                    Submit
                                </Button>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>


        </Container>
    );
}
