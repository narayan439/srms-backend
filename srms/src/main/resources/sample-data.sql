-- =====================================================
-- Sample Test Data for Student Result Management System
-- =====================================================

-- Clear existing data (optional)
-- DELETE FROM users;
-- DELETE FROM students;
-- DELETE FROM teachers;
-- DELETE FROM subjects;
-- DELETE FROM classes;

-- =====================================================
-- Insert Sample Classes
-- =====================================================
INSERT INTO classes (class_name, class_number, max_capacity, is_active, created_at, updated_at) 
VALUES 
('Class 1', 1, 60, 1, NOW(), NOW()),
('Class 2', 2, 60, 1, NOW(), NOW()),
('Class 3', 3, 60, 1, NOW(), NOW()),
('Class 4', 4, 60, 1, NOW(), NOW()),
('Class 5', 5, 60, 1, NOW(), NOW()),
('Class 6', 6, 60, 1, NOW(), NOW()),
('Class 7', 7, 60, 1, NOW(), NOW()),
('Class 8', 8, 60, 1, NOW(), NOW()),
('Class 9', 9, 60, 1, NOW(), NOW()),
('Class 10', 10, 60, 1, NOW(), NOW());

-- =====================================================
-- Insert Sample Students
-- =====================================================
INSERT INTO students (name, email, class_name, roll_no, phone, dob, is_active, created_at, updated_at) 
VALUES 
('Raj Kumar', 'raj.kumar@student.com', '12A', 'STU001', '9876543210', '2005-05-15', 1, NOW(), NOW()),
('Priya Singh', 'priya.singh@student.com', '12A', 'STU002', '9876543211', '2005-06-20', 1, NOW(), NOW()),
('Amit Patel', 'amit.patel@student.com', '12B', 'STU003', '9876543212', '2005-07-10', 1, NOW(), NOW()),
('Neha Verma', 'neha.verma@student.com', '12B', 'STU004', '9876543213', '2005-08-25', 1, NOW(), NOW());

-- =====================================================
-- Insert Sample Teachers
-- Password Format: First 3 letters of name (UPPERCASE) + Last 4 digits of phone
-- =====================================================
INSERT INTO teachers (name, email, phone, password, subjects, experience, is_active, created_at, updated_at) 
VALUES 
('Dr. Sharma', 'sharma@teacher.com', '9988776655', 'SHA6655', 'Mathematics,Physics', 10, 1, NOW(), NOW()),
('Mrs. Gupta', 'gupta@teacher.com', '9988776656', 'GUP6656', 'English,Hindi', 8, 1, NOW(), NOW()),
('Mr. Yadav', 'yadav@teacher.com', '9988776657', 'YAD6657', 'Chemistry,Science', 12, 1, NOW(), NOW()),
('Ms. Desai', 'desai@teacher.com', '9988776658', 'DES6658', 'History,Geography', 6, 1, NOW(), NOW()),
('Narayan Sahu', 'narayansahu2888@gmail.com', '6371349473', 'NAR9473', 'Computer Science,Programming', 5, 1, NOW(), NOW()),
('Ramesh Kumar', 'ramesh.kumar@gmail.com', '9876543001', 'RAM3001', 'Mathematics,Physics', 7, 1, NOW(), NOW());

-- =====================================================
-- Insert Sample Subjects
-- =====================================================
INSERT INTO subjects (subject_name, description, code, is_active, created_at, updated_at) 
VALUES 
('Mathematics', 'Basic Mathematics for Class 12', 'MATH', 1, NOW(), NOW()),
('Physics', 'Physics with practical applications', 'PHYS', 1, NOW(), NOW()),
('Chemistry', 'Organic and Inorganic Chemistry', 'CHEM', 1, NOW(), NOW()),
('English', 'English Language and Literature', 'ENG', 1, NOW(), NOW()),
('Hindi', 'Hindi Language and Literature', 'HINDI', 1, NOW(), NOW()),
('History', 'World and Indian History', 'HIST', 1, NOW(), NOW()),
('Geography', 'Physical and Human Geography', 'GEO', 1, NOW(), NOW()),
('Science', 'General Science', 'SCI', 1, NOW(), NOW());

-- =====================================================
-- Insert Sample Users (Login Credentials)
-- Password: 123456 (for all users) - PLAIN TEXT
-- =====================================================

-- STUDENT USERS
-- Password: 123456
INSERT INTO users (username, email, password, role, reference_id, is_active, created_at, updated_at) 
VALUES 
('raj_kumar', 'raj.kumar@student.com', '123456', 'STUDENT', 1, 1, NOW(), NOW()),
('priya_singh', 'priya.singh@student.com', '123456', 'STUDENT', 2, 1, NOW(), NOW()),
('amit_patel', 'amit.patel@student.com', '123456', 'STUDENT', 3, 1, NOW(), NOW()),
('neha_verma', 'neha.verma@student.com', '123456', 'STUDENT', 4, 1, NOW(), NOW());

-- TEACHER USERS
-- Password: 123456
INSERT INTO users (username, email, password, role, reference_id, is_active, created_at, updated_at) 
VALUES 
('dr_sharma', 'sharma@teacher.com', '123456', 'TEACHER', 1, 1, NOW(), NOW()),
('mrs_gupta', 'gupta@teacher.com', '123456', 'TEACHER', 2, 1, NOW(), NOW()),
('mr_yadav', 'yadav@teacher.com', '123456', 'TEACHER', 3, 1, NOW(), NOW()),
('ms_desai', 'desai@teacher.com', '123456', 'TEACHER', 4, 1, NOW(), NOW());

-- ADMIN USER
-- Password: 123456
INSERT INTO users (username, email, password, role, reference_id, is_active, created_at, updated_at) 
VALUES 
('admin', 'admin@srms.com', '123456', 'ADMIN', NULL, 1, NOW(), NOW());

-- =====================================================
-- Insert Sample Marks
-- =====================================================
INSERT INTO marks (student_id, subject_id, marks_obtained, max_marks, term, year, is_recheck_requested, created_at, updated_at) 
VALUES 
(1, 1, 85, 100, 'Term1', 2024, 0, NOW(), NOW()),
(1, 2, 78, 100, 'Term1', 2024, 0, NOW(), NOW()),
(1, 3, 92, 100, 'Term1', 2024, 0, NOW(), NOW()),
(2, 1, 88, 100, 'Term1', 2024, 0, NOW(), NOW()),
(2, 4, 95, 100, 'Term1', 2024, 0, NOW(), NOW()),
(3, 2, 72, 100, 'Term1', 2024, 0, NOW(), NOW()),
(3, 8, 80, 100, 'Term1', 2024, 0, NOW(), NOW()),
(4, 4, 91, 100, 'Term1', 2024, 0, NOW(), NOW());

-- =====================================================
-- Test Login Credentials Summary
-- =====================================================
-- ADMIN:
-- Email: admin@gmail.com (HARDCODED - NOT IN DATABASE)
-- Password: 123456
-- Redirect: /admin/dashboard

-- TEACHER 1:
-- Email: sharma@teacher.com
-- Password: SHA6655
-- Redirect: /teacher/dashboard

-- TEACHER 2:
-- Email: gupta@teacher.com
-- Password: GUP6656
-- Redirect: /teacher/dashboard

-- TEACHER 3:
-- Email: yadav@teacher.com
-- Password: YAD6657
-- Redirect: /teacher/dashboard

-- TEACHER 4:
-- Email: desai@teacher.com
-- Password: DES6658
-- Redirect: /teacher/dashboard

-- TEACHER 5:
-- Email: narayansahu2888@gmail.com
-- Password: NAR9473
-- Redirect: /teacher/dashboard

-- TEACHER 6:
-- Email: ramesh.kumar@gmail.com
-- Password: RAM3001
-- Redirect: /teacher/dashboard

-- STUDENT 1:
-- Email: raj.kumar@student.com
-- Password: 15052005 (DOB as DDMMYYYY)
-- Redirect: /student/dashboard

-- STUDENT 2:
-- Email: priya.singh@student.com
-- Password: 20062005 (DOB as DDMMYYYY)
-- Redirect: /student/dashboard
