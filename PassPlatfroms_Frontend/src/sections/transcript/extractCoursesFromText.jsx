const extractCoursesFromText = (text, regex) => {
    const matches = text.matchAll(regex);
    const courses = [];

    for (const match of matches) {
        const [, code, number, level, title, grade, creditHours, qualityPoints] = match;
        courses.push({
            code,
            number: parseInt(number, 10),
            level,
            title,
            grade,
            creditHours: parseFloat(creditHours),
            qualityPoints: parseFloat(qualityPoints),
        });
    }

    return courses;
};

export default extractCoursesFromText;
