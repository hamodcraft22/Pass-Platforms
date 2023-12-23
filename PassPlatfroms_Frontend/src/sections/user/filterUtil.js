export function applyFilter({inputData, comparator, filterName}) {
    const stabilizedThis = inputData.map((el, index) => [el, index]);

    stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) return order;
        return a[1] - b[1];
    });

    inputData = stabilizedThis.map((el) => el[0]);

    if (filterName) {
        // if it starts with number
        if (/^\d+$/.test(filterName)) {
            inputData = inputData.filter((user) => user.userid.toLowerCase().indexOf(filterName.toLowerCase()) !== -1);
        } else {
            inputData = inputData.filter((user) => user.userName.toLowerCase().indexOf(filterName.toLowerCase()) !== -1);
        }
    }

    return inputData;
}
