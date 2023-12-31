import PropTypes from 'prop-types';

import Toolbar from '@mui/material/Toolbar';
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {DatePicker} from "@mui/x-date-pickers";
import Button from "@mui/material/Button";
import React from "react";

// ----------------------------------------------------------------------

export default function RevisionsTableToolbar({startDate, onDateStart, endDate, onDateEnd, onClearButton})
{
    return (
        <Toolbar
            sx={{
                height: 96,
                display: 'flex',
                justifyContent: 'space-between',
                p: (theme) => theme.spacing(0, 1, 0, 3)
            }}
        >

            <div>
                <LocalizationProvider dateAdapter={AdapterMoment}>
                    <DatePicker label="From Date" value={startDate} onChange={(newValue) =>
                    {
                        onDateStart(newValue)
                    }}/>
                </LocalizationProvider>

                <LocalizationProvider dateAdapter={AdapterMoment}>
                    <DatePicker sx={{ml: 2}} label="To Date" minDate={startDate} value={endDate} onChange={(newValue) =>
                    {
                        onDateEnd(newValue)
                    }}/>
                </LocalizationProvider>
            </div>


            <Button variant={"contained"} onClick={onClearButton} disabled={(startDate === null && endDate === null) || (startDate === undefined && endDate === undefined)}>Clear</Button>


        </Toolbar>
    );
}

RevisionsTableToolbar.propTypes = {
    numSelected: PropTypes.number,
    filterName: PropTypes.string,
    onFilterName: PropTypes.func,
};
