export function applyFilter({inputData, comparator, startDate, endDate}) {
    const stabilizedThis = inputData.map((el, index) => [el, index]);

    stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) return order;
        return a[1] - b[1];
    });

    inputData = stabilizedThis.map((el) => el[0]);

    if (startDate) {
        inputData = inputData.filter((booking) => {
            return (new Date(booking.bookingDate).getTime() >= new Date(startDate).getTime())
        });
    }

    if (endDate) {
        inputData = inputData.filter((booking) => {
            return (new Date(booking.bookingDate).getTime() <= new Date(endDate).getTime())
        });
    }

    return inputData;
}
