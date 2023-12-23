import React, {useEffect, useState} from "react";
import {Autocomplete, Checkbox, TextField} from "@mui/material";
import {createFilterOptions} from "@mui/material/Autocomplete";
import Paper from "@mui/material/Paper";

export default function MultiSelect({
                                        items,
                                        label,
                                        selectAllLabel,
                                        courses
                                    }) {
    const [selectedOptions, setSelectedOptions] = useState([]);
    const allSelected = items.length === selectedOptions.length;

    const handleToggleOption = (selectedOps) => setSelectedOptions(selectedOps);
    const handleClearOptions = () => setSelectedOptions([]);

    const handleSelectAll = (isSelected) => {
        if (isSelected) {
            setSelectedOptions(items);
        } else {
            handleClearOptions();
        }
    };

    const handleToggleSelectAll = () => {
        handleSelectAll && handleSelectAll(!allSelected);
    };

    const handleChange = (event, selectedOps, reason) => {
        if (reason === "selectOption" || reason === "removeOption") {
            if (selectedOps.find((option) => option.coursename === "All")) {
                handleToggleSelectAll();
            } else {
                handleToggleOption && handleToggleOption(selectedOps);
            }
        } else if (reason === "clear") {
            handleClearOptions && handleClearOptions();
        }
    };

    const filter = createFilterOptions();

    useEffect(() => {
        return courses(selectedOptions)
    }, [courses, selectedOptions]);

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (
        <Autocomplete
            PaperComponent={CustomPaper}
            style={{width: "100%", minWidth: "350px"}}
            multiple
            disableCloseOnSelect
            options={items}
            value={selectedOptions}
            onChange={handleChange}
            getOptionLabel={(option) => option.courseid + " " + option.coursename}
            filterOptions={(options, params) => {
                const filtered = filter(options, params);
                return [{id: 0, courseid: "", coursename: selectAllLabel}, ...filtered];
            }}
            renderOption={(props, option, {selected}) => {
                // To control the state of 'select-all' checkbox
                const selectAllProps =
                    option.coursename === "All" ? {checked: allSelected} : {};
                return (
                    <li {...props}>
                        <Checkbox checked={selected} {...selectAllProps} />
                        {option.courseid + " " + option.coursename}
                    </li>
                );
            }}
            renderInput={(params) => (
                <TextField {...params} label={label}/>
            )}
        />
    );
}

MultiSelect.defaultProps = {
    items: [],
    selectedValues: []
};
