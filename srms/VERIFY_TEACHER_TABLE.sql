-- =====================================================
-- VERIFY TEACHER TABLE DATA & PASSWORDS
-- =====================================================

USE srms_database;

-- 1. Check table structure
DESCRIBE teachers;

-- 2. Count teachers
SELECT COUNT(*) as total_teachers FROM teachers;

-- 3. Show all teachers with their passwords
SELECT 
  teacher_id,
  name,
  email,
  phone,
  password,
  is_active,
  CASE 
    WHEN password IS NULL THEN '❌ NULL'
    WHEN password = '' THEN '❌ EMPTY'
    ELSE '✅ SET'
  END as password_status
FROM teachers
ORDER BY teacher_id;

-- 4. Count teachers by password status
SELECT 
  CONCAT('Total: ', COUNT(*)) as total,
  CONCAT('With password: ', COUNT(password)) as with_pwd,
  CONCAT('NULL password: ', COUNT(CASE WHEN password IS NULL THEN 1 END)) as null_pwd
FROM teachers;

-- 5. If passwords are NULL, show password that SHOULD be set
SELECT 
  teacher_id,
  name,
  email,
  phone,
  CONCAT(UPPER(SUBSTRING(name, 1, 3)), SUBSTRING(REPLACE(phone, '-', ''), -4)) as suggested_password,
  password as current_password
FROM teachers
WHERE password IS NULL;
