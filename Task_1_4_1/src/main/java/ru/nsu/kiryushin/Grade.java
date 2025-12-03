package ru.nsu.kiryushin;

import java.time.LocalDate;

public record Grade(AssessmentType assessmentType, GradeValue grade, String discipline, LocalDate date,
                    String teacher) {

    /**
     * Static factory method to create a new Grade with validation.
     *
     * @param assessmentType the type of assessment (e.g., EXAM, CREDIT)
     * @param grade          the value of the grade
     * @param discipline     the name of the subject
     * @param date           the date of the assessment
     * @param teacher        the name of the teacher
     * @return a new Grade instance
     * @throws IllegalArgumentException if the assessment type does not match the grade value type
     */
    public static Grade create(AssessmentType assessmentType, GradeValue grade, String discipline, LocalDate date, String teacher) {
        if (assessmentType == AssessmentType.CREDIT && !(grade instanceof CreditGrade)) {
            throw new IllegalArgumentException("CREDIT должен идти только с CreditGrade");
        }

        if (assessmentType != AssessmentType.CREDIT && !(grade instanceof FivePointGrade)) {
            throw new IllegalArgumentException(assessmentType + " должен идти с FivePointGrade");
        }

        return new Grade(assessmentType, grade, discipline, date, teacher);
    }
}