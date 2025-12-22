package com.studentresult.config;

import com.studentresult.entity.Marks;
import com.studentresult.entity.Student;
import com.studentresult.entity.Subject;
import com.studentresult.repository.MarksRepository;
import com.studentresult.repository.StudentRepository;
import com.studentresult.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Data Initializer - Adds sample marks data to the database on application startup
 * This ensures that the backend always has test data for development
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n════════════════════════════════════════════════════════");
        System.out.println("🔄 DATA INITIALIZER - Checking and Adding Sample Marks");
        System.out.println("════════════════════════════════════════════════════════");

        try {
            // Check database state
            System.out.println("\n📊 Database Status:");
            System.out.println("  Students in DB: " + studentRepository.count());
            System.out.println("  Subjects in DB: " + subjectRepository.count());
            System.out.println("  Marks in DB: " + marksRepository.count());
            
            // Check if marks already exist
            long existingMarksCount = marksRepository.count();
            if (existingMarksCount > 0) {
                System.out.println("\n✓ Database already has " + existingMarksCount + " marks records");
                System.out.println("⚠️  Skipping initialization (data already exists)");
                return;
            }

            System.out.println("\n🔍 Database is empty. Checking prerequisites...");

            // Check if students exist
            if (studentRepository.count() == 0) {
                System.out.println("❌ No students found in database. Please add students first.");
                return;
            }

            // Check if subjects exist
            if (subjectRepository.count() == 0) {
                System.out.println("❌ No subjects found in database. Please add subjects first.");
                return;
            }

            System.out.println("\n✓ Prerequisites met. Adding sample marks...");

            // Find student with ID 21 (Raj Kumar Singh, Class 10)
            Student student21 = studentRepository.findById(21L).orElse(null);
            if (student21 == null) {
                System.out.println("⚠️  Student ID 21 not found. Cannot add marks.");
                System.out.println("Available students:");
                studentRepository.findAll().forEach(s -> {
                    System.out.println("  - ID: " + s.getStudentId() + ", Name: " + s.getName() + ", RollNo: " + s.getRollNo());
                });
                return;
            }

            System.out.println("✓ Found student: " + student21.getName() + " (ID: 21)");

            // Add marks for student 21 (5 subjects)
            addMarksForStudent(student21, "Mathematics", 85);
            addMarksForStudent(student21, "Physics", 78);
            addMarksForStudent(student21, "Chemistry", 82);
            addMarksForStudent(student21, "English", 88);
            addMarksForStudent(student21, "Hindi", 80);

            System.out.println("✓ Added 5 marks for student " + student21.getName());

            // Find student with ID 22 (Neha Verma, Class 10)
            Student student22 = studentRepository.findById(22L).orElse(null);
            if (student22 != null) {
                System.out.println("✓ Found student: " + student22.getName() + " (ID: 22)");
                addMarksForStudent(student22, "Mathematics", 92);
                addMarksForStudent(student22, "Physics", 88);
                addMarksForStudent(student22, "Chemistry", 90);
                System.out.println("✓ Added 3 marks for student " + student22.getName());
            }

            System.out.println("\n✅ Data initialization completed successfully!");
            System.out.println("Total marks in database: " + marksRepository.count());
            System.out.println("════════════════════════════════════════════════════════\n");

        } catch (Exception e) {
            System.out.println("❌ Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to add a mark for a student by subject name
     */
    private void addMarksForStudent(Student student, String subjectName, int marksObtained) {
        try {
            // Find subject by name
            Subject subject = subjectRepository.findBySubjectNameIgnoreCase(subjectName).orElse(null);
            if (subject == null) {
                System.out.println("  ⚠️  Subject '" + subjectName + "' not found");
                return;
            }

            // Check if marks already exist for this student-subject combination
            boolean alreadyExists = marksRepository.findByStudentAndSubject(student, subject)
                    .stream()
                    .anyMatch(m -> true);

            if (alreadyExists) {
                System.out.println("  ⚠️  Marks already exist for " + student.getName() + " - " + subjectName);
                return;
            }

            // Create and save marks
            Marks marks = new Marks();
            marks.setStudent(student);
            marks.setSubject(subject);
            marks.setMarksObtained(marksObtained);
            marks.setMaxMarks(100);
            marks.setTerm("Term 1");
            marks.setYear(2024);
            marks.setIsRecheckRequested(false);
            marks.setCreatedAt(LocalDateTime.now());
            marks.setUpdatedAt(LocalDateTime.now());

            Marks savedMarks = marksRepository.save(marks);
            System.out.println("  ✓ Added: " + subjectName + " (" + marksObtained + "/100)");

        } catch (Exception e) {
            System.out.println("  ❌ Error adding marks for " + subjectName + ": " + e.getMessage());
        }
    }
}
