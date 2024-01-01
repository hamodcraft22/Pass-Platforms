const extractScheduleFromText = (text, regex) => {
    const matches = text.matchAll(regex);
    const courses = [];

    for (const match of matches) {
        const [, day, start, end] = match;
        courses.push({
            day,
            start,
            end,
        });
    }

    return courses;
};

export default extractScheduleFromText;
