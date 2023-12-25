import Container from '@mui/material/Container';
import Grid from '@mui/material/Unstable_Grid2';
import Typography from '@mui/material/Typography';
import Card from "@mui/material/Card";
import React, {useState} from "react";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import GradingRoundedIcon from '@mui/icons-material/GradingRounded';
import CalendarMonthRoundedIcon from '@mui/icons-material/CalendarMonthRounded';
import PendingActionsRoundedIcon from '@mui/icons-material/PendingActionsRounded';
import HistoryEduRoundedIcon from '@mui/icons-material/HistoryEduRounded';
import Label from "../../../components/label";
import Paper from "@mui/material/Paper";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";


// ----------------------------------------------------------------------


export default function ProfileView() {

    const queryParameters = new URLSearchParams(window.location.search)
    const leaderIDParm = queryParameters.get("userID");

    const [userName, setUserName] = useState("Mohamed Adel Jaafar Abdulredha Hasan");


    const [userID, setUserID] = useState("202002789");
    const [userRole, setUserRole] = useState("Leader");


    const [courses, setCourses] = useState([]);

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (

        <Container maxWidth="xl">


            {/* top bar */}
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4" sx={{mb: 5}}>
                    Profile
                </Typography>

                <div>
                    <Button variant="contained" color="inherit" startIcon={<GradingRoundedIcon/>} sx={{m: 1}}>
                        Transcript
                    </Button>

                    <Button variant="contained" color="inherit" startIcon={<PendingActionsRoundedIcon/>} sx={{m: 1}}>
                        Slots
                    </Button>

                    <Button variant="contained" color="inherit" startIcon={<CalendarMonthRoundedIcon/>} sx={{m: 1}}>
                        Bookings
                    </Button>

                    <Button variant="contained" color="inherit" startIcon={<HistoryEduRoundedIcon/>} sx={{m: 1}}>
                        Revisions
                    </Button>
                </div>


            </Stack>


            <Grid container spacing={3}>

                <Grid xs={12} md={6} lg={4}>
                    <Card>
                        <div style={{padding: "15px"}}>
                            <img src="/assets/icons/profile-pic.png"/>

                            <Typography variant="h4" sx={{mt: 2}} textAlign={"center"}>
                                {userName}
                            </Typography>

                            <Typography variant="h5" color="text.disabled" sx={{mt: 1}} textAlign={"center"}>
                                {userID}
                            </Typography>

                            <Typography variant="h6" sx={{mt: 1}} textAlign={"center"}>
                                <Label color={"error"}>{userRole}</Label>
                            </Typography>

                        </div>
                    </Card>
                </Grid>

                <Grid xs={12} md={6} lg={8}>
                    <Card>
                        <div style={{padding: "15px"}}>
                            <Typography variant="h6" sx={{mb: 2}}>
                                Offered Courses:
                            </Typography>


                            {
                                Object.keys(courses).length !== 0 &&
                                <>
                                    <TableContainer component={CustomPaper}>
                                        <Table sx={{minWidth: 650}}>
                                            <TableHead>
                                                <TableRow>
                                                    <TableCell>Course Name</TableCell>
                                                    <TableCell align="right">Course Code</TableCell>
                                                    <TableCell align="right"></TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {courses.map((course, index) => (
                                                    <TableRow
                                                        key={index}
                                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                                    >
                                                        <TableCell component="th" scope="row">{course.coursename}</TableCell>
                                                        <TableCell align="right">{course.courseid}</TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </TableContainer>
                                </>
                            }

                            {
                                userRole !== "Leader" && <Typography variant="h6" sx={{mb: 2}}>
                                    Student is not a pass leader and they do not offer any courses</Typography>
                            }

                        </div>
                    </Card>
                </Grid>


            </Grid>
        </Container>
    );
}
