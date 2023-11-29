import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import WeekCalendar from 'react-week-calendar';
import moment from "moment";
import bookingSlot from "../bookingSlot";
import Toolbar from "@mui/material/Toolbar";
import MultiSelect from "../MultiSelect";
import {useState} from "react";


// ----------------------------------------------------------------------

export default function BookingPage() {

    const selectedIntervals = [
        {
            uid: 1,
            start: moment({h: 10, m: 0}),
            end: moment({h: 11, m: 0}),
            color: "#94E387FF"
        },
        {
            uid: 2,
            start: moment({h: 12, m: 0}).add(2, 'd'),
            end: moment({h: 13, m: 0}).add(2, 'd'),
            online: true,
            color: "#E494EEFF"
        },
        {
            uid: 3,
            start: moment({h: 10, m: 0}).add(-1, 'd').add(-1, 'h'),
            end: moment({h: 11, m: 0}).add(-1, 'd').add(-1, 'h'),
            online: true,
            color: "#94E387FF"
        }

    ]

    const dimensions = [
        { id: 202002789, name: "Mohamed Hasan", color: "#94E387FF" },
        { id: 202001478, name: "Sara Alshamari", color: "#E494EEFF" },
    ];

    let leaders = [];

    return (
        <Container>
            <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
                <Typography variant="h4">New Booking</Typography>

                {/*<Button variant="contained" color="inherit" startIcon={<Iconify icon="eva:plus-fill"/>}>*/}
                {/*    New User*/}
                {/*</Button>*/}
            </Stack>

            <Card>
                <div style={{padding: "15px"}}>
                    <Toolbar
                        sx={{
                            minHeight: 96,
                            marginBottom: '10px',
                            display: 'flex',
                            justifyContent: 'space-between',
                            p: (theme) => theme.spacing(0, 1, 0, 3)
                        }}
                    >

                        {/* add list of leaders here*/}
                        <MultiSelect
                            items={dimensions}
                            label="Dimensions"
                            selectAllLabel="All"
                            slots={(items) => {leaders = items; console.log(leaders)}}
                        />


                    </Toolbar>

                    <WeekCalendar
                        dayFormat={"dd DD/MM"}
                        firstDay={moment().weekday(0)}
                        numberOfDays={7}
                        startTime={moment({h: 8, m: 0})}
                        endTime={moment({h: 22, m: 0})}
                        scaleUnit={30}
                        cellHeight={50}
                        scaleFormat={"h:mm a"}
                        useModal={false}
                        selectedIntervals={selectedIntervals}
                        onEventClick={(event) => {
                            alert(event.uid)
                        }}
                        eventComponent={bookingSlot}
                        eventSpacing={0}
                    />
                </div>
            </Card>
        </Container>
    );
}
