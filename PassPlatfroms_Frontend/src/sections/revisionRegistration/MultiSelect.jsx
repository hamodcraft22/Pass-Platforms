import React, {useEffect, useState} from "react";
import {Autocomplete, Checkbox, Chip, TextField} from "@mui/material";
import {createFilterOptions} from "@mui/material/Autocomplete";
import SquareRoundedIcon from '@mui/icons-material/SquareRounded';
import Paper from "@mui/material/Paper";

export default function MultiSelect({
                                        items,
                                        label,
                                        selectAllLabel,
                                        leaders
                                    }) {
    const [selectedOptions, setSelectedOptions] = useState([]);
    const allSelected = items.length === selectedOptions.length;

    useEffect(() => {
        if (items !== null && Object.keys(items).length !== 0) {
            setSelectedOptions(items)
        }
    }, [items]);

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
            if (selectedOps.find((option) => option.leaderName === "All")) {
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
        return leaders(selectedOptions)
    }, [leaders, selectedOptions]);

    const CustomPaper = (props) => {
        return <Paper elevation={8} {...props} />;
    };

    return (
        <Autocomplete
            PaperComponent={CustomPaper}
            style={{width: "100%"}}
            multiple
            disableCloseOnSelect
            options={items}
            value={selectedOptions}
            onChange={handleChange}
            getOptionLabel={(option) => option.leaderName}
            filterOptions={(options, params) => {
                const filtered = filter(options, params);
                return [{leaderID: 0, leaderName: selectAllLabel}, ...filtered];
            }}
            renderOption={(props, option, {selected}) => {
                // To control the state of 'select-all' checkbox
                const selectAllProps =
                    option.leaderName === "All" ? {checked: allSelected} : {};
                return (
                    <li {...props}>
                        <Checkbox checked={selected} {...selectAllProps} />
                        <SquareRoundedIcon fontSize="small" style={{marginRight: 8, color: `${option.color}`}}/>
                        {option.leaderName}
                    </li>
                );
            }}
            renderInput={(params) => (
                <TextField {...params} label={label}/>
            )}
            renderTags={(value) => value.map((option, index) => (
                <Chip
                    key={index}
                    variant="outlined"
                    label={option.leaderName}
                    sx={{background: `${option.color}`, marginRight: "3px"}}
                    onDelete={() => {
                        setSelectedOptions([
                            ...selectedOptions.slice(0, index),
                            ...selectedOptions.slice(index + 1)
                        ]);
                    }}
                />
            ))}
        />
    );
}

MultiSelect.defaultProps = {
    items: [],
    selectedValues: []
};
