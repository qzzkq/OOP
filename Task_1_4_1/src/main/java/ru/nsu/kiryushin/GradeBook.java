package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a student's grade book.
 */
public class GradeBook {
    final int recordBookNumber;
    private int semesterNow;

    private final Map<Integer, List<Grade>> gradesBySemester = new HashMap<>();

    /**
     * Creates a new GradeBook instance.
     *
     * @param recordBookNumber the student's record book number
     * @param semesterNow      the current semester number (must be between 1 and 12)
     * @throws IllegalArgumentException if the semester is not in the valid range
     */
    public GradeBook(int recordBookNumber, int semesterNow) {
        if (semesterNow < 1 || semesterNow > 12) {
            throw new IllegalArgumentException("Диапазон допустимых семестров - 1-12.");
        }
        this.recordBookNumber = recordBookNumber;
        this.semesterNow = semesterNow;
    }

    /**
     * Retrieves the list of grades for a specific semester.
     *
     * @param semester the semester number
     * @return a list of grades for the specified semester, or an empty list if none exist
     */
    public List<Grade> getGrades(int semester) {
        return gradesBySemester.getOrDefault(semester, List.of());
    }

    /**
     * Adds a grade to the grade book for a specific semester.
     *
     * @param semester the semester number
     * @param grade    the grade to add
     */
    public void addGrade(int semester, Grade grade) {
        gradesBySemester
                .computeIfAbsent(semester, s -> new ArrayList<>())
                .add(grade);
    }

    /**
     * Increases the current semester by one.
     */
    public void increaseSemester() {
        this.semesterNow++;
    }

    /**
     * Checks if the student is eligible for a stipend in the current semester.
     *
     * @return true if the student has a stipend, false otherwise
     */
    public boolean hasStipend() {
        if (semesterNow < 2) {
            return false;
        }

        List<Grade> gradesLastSem = getGrades(semesterNow - 1);

        return gradesLastSem.stream().allMatch(g -> {
            GradeValue gv = g.grade();
            return gv.isSatisfactory() && (gv.getValue() == 5 || gv.getValue() == 0);
        });
    }

    /**
     * Checks if the student is eligible for a Red Diploma (diploma with honors).
     *
     * @return true if the student meets the requirements for a Red Diploma, false otherwise
     */
    public boolean canGetRedDiploma() {
        if (gradesBySemester.isEmpty()) {
            return true;
        }

        int countFive = 0;
        int countGraded = 0;

        for (List<Grade> semesterGrades : gradesBySemester.values()) {
            for (Grade grade : semesterGrades) {
                GradeValue gv = grade.grade();
                int val = gv.getValue();

                if (!gv.isSatisfactory() || val == 3) {
                    return false;
                }

                if (grade.assessmentType() == AssessmentType.QUALIFICATION_WORK && val != 5) {
                    return false;
                }

                if (val > 0) {
                    countGraded++;
                    if (val == 5) {
                        countFive++;
                    }
                }
            }
        }

        if (countGraded == 0) {
            return true;
        }

        return ((double) countFive / countGraded) >= 0.75;
    }

    /**
     * Checks if the student can transfer to the specified form of study.
     *
     * @param formOfStudy the target form of study
     * @return 0 if the student is already on BUDGET, 1 if transfer is possible, -1 otherwise
     */
    public int canTransferTo(FormOfStudy formOfStudy) {
        if (formOfStudy == FormOfStudy.BUDGET) {
            return 0;
        }

        if (semesterNow == 1) {
            return -1;
        }

        List<Grade> allGrades = Stream.concat(
                getGrades(semesterNow).stream(),
                getGrades(semesterNow - 1).stream()
        ).toList();

        boolean hasBadGrades = allGrades.stream().anyMatch(g -> {
            GradeValue gv = g.grade();
            return !gv.isSatisfactory() ||
                    (g.assessmentType() == AssessmentType.EXAM && gv.getValue() == 3);
        });

        if (!hasBadGrades) {
            return 1;
        }
        return -1;
    }

    /**
     * Computes the average grade for the entire period of study.
     *
     * @return the average grade value
     */
    public double computeAverageGrade() {
        double sum = 0;
        int count = 0;

        List<Grade> allGrades = gradesBySemester.values().stream()
                .flatMap(List::stream)
                .toList();

        for (Grade grade : allGrades) {
            int val = grade.grade().getValue();
            if (val > 0) {
                sum += val;
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return sum / count;
    }
}
