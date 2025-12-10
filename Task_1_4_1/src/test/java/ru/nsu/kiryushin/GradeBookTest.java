package ru.nsu.kiryushin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GradeBookTest {

    private GradeBook gradeBook;

    @BeforeEach
    void setUp() {
        gradeBook = new GradeBook(12345, 1);
    }

    @Test
    void testInvalidCreditGrade() {
        assertThrows(IllegalArgumentException.class, () ->
            Grade.create(AssessmentType.CREDIT, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "Teacher")
        );
    }

    @Test
    void testInvalidExamGrade() {
        assertThrows(IllegalArgumentException.class, () ->
            Grade.create(AssessmentType.EXAM, CreditGrade.CREDIT, "Math", LocalDate.now(), "Teacher")
        );
    }

    @Test
    void testAverageGradeEmpty() {
        assertEquals(0.0, gradeBook.computeAverageGrade());
    }

    @Test
    void testAverageGradeSimple() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.GOOD, "History", LocalDate.now(), "T2"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.SATISFACTORY, "Physics", LocalDate.now(), "T3"));

        assertEquals(4.0, gradeBook.computeAverageGrade(), 0.001);
    }

    @Test
    void testAverageGradeWithCredits() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.CREDIT, CreditGrade.CREDIT, "PE", LocalDate.now(), "T2"));

        assertEquals(5.0, gradeBook.computeAverageGrade(), 0.001);
    }

    @Test
    void testStipendFirstSemester() {
        assertFalse(gradeBook.hasStipend());
    }

    @Test
    void testStipendSuccess() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.CREDIT, CreditGrade.CREDIT, "PE", LocalDate.now(), "T2"));

        gradeBook.increaseSemester();

        assertTrue(gradeBook.hasStipend());
    }

    @Test
    void testStipendWithGoodGrade() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.GOOD, "History", LocalDate.now(), "T2"));

        gradeBook.increaseSemester();

        assertFalse(gradeBook.hasStipend());
    }

    @Test
    void testRedDiplomaWithSatisfactory() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.SATISFACTORY, "Math", LocalDate.now(), "T1"));
        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaBadQualWork() {
        gradeBook.addGrade(8, Grade.create(AssessmentType.QUALIFICATION_WORK, FivePointGrade.GOOD, "Thesis", LocalDate.now(), "T1"));
        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaNotEnoughFives() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.GOOD, "History", LocalDate.now(), "T2"));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaSuccess() {
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Phys", LocalDate.now(), "T2"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Chem", LocalDate.now(), "T3"));
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.GOOD, "Hist", LocalDate.now(), "T4"));

        gradeBook.addGrade(8, Grade.create(AssessmentType.QUALIFICATION_WORK, FivePointGrade.EXCELLENT, "Thesis", LocalDate.now(), "T5"));

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    void testTransferAlreadyBudget() {
        assertEquals(0, gradeBook.canTransferTo(FormOfStudy.BUDGET));
    }

    @Test
    void testTransferFirstSem() {
        assertEquals(-1, gradeBook.canTransferTo(FormOfStudy.PAID));
    }

    @Test
    void testTransferWithBadGrade() {
        gradeBook.increaseSemester();
        gradeBook.addGrade(1, Grade.create(AssessmentType.EXAM, FivePointGrade.SATISFACTORY, "Math", LocalDate.now(), "T1"));

        assertEquals(-1, gradeBook.canTransferTo(FormOfStudy.PAID));
    }

    @Test
    void testTransferSuccess() {
        gradeBook.increaseSemester();
        gradeBook.increaseSemester();

        gradeBook.addGrade(2, Grade.create(AssessmentType.EXAM, FivePointGrade.GOOD, "Math", LocalDate.now(), "T1"));
        gradeBook.addGrade(3, Grade.create(AssessmentType.EXAM, FivePointGrade.EXCELLENT, "Phys", LocalDate.now(), "T2"));

        assertEquals(1, gradeBook.canTransferTo(FormOfStudy.PAID));
    }
}
