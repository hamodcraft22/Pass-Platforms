import {useState} from "react";
import {Autocomplete, Checkbox, Chip, TextField} from "@mui/material";
import {createFilterOptions} from "@mui/material/Autocomplete";
import SquareRoundedIcon from '@mui/icons-material/SquareRounded';

export default function MultiSelect({
                                        items,
                                        label,
                                        selectAllLabel,
                                        leaders
                                    }) {
    const [selectedOptions, setSelectedOptions] = useState(items);
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
            if (selectedOps.find((option) => option.name === "All")) {
                handleToggleSelectAll();
                const result = items.filter((el) => el.name !== "All");
                return leaders(result);
            } else {
                handleToggleOption && handleToggleOption(selectedOps);
                return leaders(selectedOps);
            }
        } else if (reason === "clear") {
            handleClearOptions && handleClearOptions();
        }
    };

    const filter = createFilterOptions();

    return (
        <Autocomplete
            style={{width: "100%"}}
            multiple
            disableCloseOnSelect
            options={items}
            value={selectedOptions}
            onChange={handleChange}
            getOptionLabel={(option) => option.name}
            filterOptions={(options, params) => {
                const filtered = filter(options, params);
                return [{id: 0, name: selectAllLabel}, ...filtered];
            }}
            renderOption={(props, option, {selected}) => {
                // To control the state of 'select-all' checkbox
                const selectAllProps =
                    option.name === "All" ? {checked: allSelected} : {};
                return (
                    <li {...props}>
                        <Checkbox checked={selected} {...selectAllProps} />
                        <SquareRoundedIcon fontSize="small" style={{marginRight: 8, color:`${option.color}`}} />
                        {option.name}
                    </li>
                );
            }}
            renderInput={(params) => (
                <TextField {...params} label={label} />
            )}
            renderTags={(value) => value.map((option, index) => (
                <Chip
                    key={index}
                    variant="outlined"
                    label={option.name}
                    sx={{background: `${option.color}`, marginRight:"3px"}}
                    onDelete={() => (setSelectedOptions([
                      ...selectedOptions.slice(0, index),
                      ...selectedOptions.slice(index + 1)
                    ]))}
                />
            ))}
        />
    );
}

MultiSelect.defaultProps = {
    items: [],
    selectedValues: []
};
