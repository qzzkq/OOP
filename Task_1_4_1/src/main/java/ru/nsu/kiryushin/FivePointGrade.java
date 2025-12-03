package ru.nsu.kiryushin;

public enum FivePointGrade implements GradeValue {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3),
    UNSATISFACTORY(2);

    private final int value;

    FivePointGrade(int value) {
        this.value = value;
    }

    /**
     * Checks if the grade is satisfactory (greater than 2).
     *
     * @return true if the grade is satisfactory, false otherwise
     */
    @Override
    public boolean isSatisfactory() {
        return value > 2;
    }

    /**
     * Returns the numeric value of the grade.
     *
     * @return the integer value (2, 3, 4, or 5)
     */
    @Override
    public int getValue() { return value; }
}