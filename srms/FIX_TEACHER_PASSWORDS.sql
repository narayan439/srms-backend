-- =====================================================
-- TEACHER LOGIN FIX - Update Passwords
-- =====================================================
-- Run this if you have existing teachers with NULL passwords
-- Password Format: First 3 letters of NAME (UPPERCASE) + Last 4 digits of PHONE
-- =====================================================

USE srms_database;

-- Check current state before update
SELECT '=== BEFORE UPDATE ===' as status;
SELECT teacher_id, name, email, phone, password, is_active 
FROM teachers 
ORDER BY teacher_id;

-- Update passwords for all teachers
UPDATE teachers SET password = 'SHA6655' WHERE email = 'sharma@teacher.com';
UPDATE teachers SET password = 'GUP6656' WHERE email = 'gupta@teacher.com';
UPDATE teachers SET password = 'YAD6657' WHERE email = 'yadav@teacher.com';
UPDATE teachers SET password = 'DES6658' WHERE email = 'desai@teacher.com';
UPDATE teachers SET password = 'NAR9473' WHERE email = 'narayansahu2888@gmail.com';
UPDATE teachers SET password = 'RAM3001' WHERE email = 'ramesh.kumar@gmail.com';

-- Verify all passwords are set
SELECT '=== AFTER UPDATE ===' as status;
SELECT teacher_id, name, email, phone, password, is_active 
FROM teachers 
ORDER BY teacher_id;

-- Count verification
SELECT 
  CONCAT('Total Teachers: ', COUNT(*)) as total,
  CONCAT('With Password: ', COUNT(password)) as with_pwd,
  CONCAT('Without Password: ', COUNT(CASE WHEN password IS NULL THEN 1 END)) as without_pwd
FROM teachers;

-- Test credentials display
SELECT '=== TEST CREDENTIALS ===' as status;
SELECT 
  email,
  password,
  CONCAT('Role: TEACHER | Redirect: /teacher/dashboard') as info
FROM teachers
WHERE is_active = 1
ORDER BY teacher_id;
