import React from 'react';
import * as XLSX from 'xlsx';
import Button from "@mui/material/Button";
import SimCardDownloadRoundedIcon from '@mui/icons-material/SimCardDownloadRounded';

function ExportToExcel({data, filename})
{
    const exportToExcel = () =>
    {
        const worksheet = XLSX.utils.json_to_sheet(data);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
        XLSX.writeFile(workbook, `${filename}.xlsx`);
    };

    return (
        <Button onClick={exportToExcel} variant={"contained"} color={"success"} sx={{m: 1}} startIcon={<SimCardDownloadRoundedIcon/>}>
            Export to Excel
        </Button>
    );
}

export default ExportToExcel;
