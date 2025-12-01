package ru.nsu.kiryushin;

public interface GradeValue {
    /**
     * Checks if the grade is considered satisfactory.
     *
     * @return true if the grade is satisfactory, false otherwise
     */
    boolean isSatisfactory();

    /**
     * Returns the numeric value of the grade.
     *
     * @return the integer value of the grade
     */
    int getValue();
}