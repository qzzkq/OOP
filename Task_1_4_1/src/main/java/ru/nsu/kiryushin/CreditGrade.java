package ru.nsu.kiryushin;

public enum CreditGrade implements GradeValue {
    CREDIT,
    NONCREDIT;

    /**
     * Checks if the grade is a pass (CREDIT).
     *
     * @return true if CREDIT, false otherwise
     */
    @Override
    public boolean isSatisfactory() {
        return this == CREDIT;
    }

    /**
     * Returns the numeric value of the grade.
     *
     * @return 0 for credit grades
     */
    @Override
    public int getValue() { return 0; }
}