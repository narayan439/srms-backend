-- =====================================================
-- Update Teacher Passwords to Match Auto-Generated Format
-- Format: First 3 letters of name (UPPERCASE) + Last 4 digits of phone
-- =====================================================

-- Update Dr. Sharma: First 3 = "SHA", Last 4 digits of "9988776655" = "6655"
UPDATE teachers SET password = 'SHA6655' WHERE email = 'sharma@teacher.com';

-- Update Mrs. Gupta: First 3 = "GUP", Last 4 digits of "9988776656" = "6656"
UPDATE teachers SET password = 'GUP6656' WHERE email = 'gupta@teacher.com';

-- Update Mr. Yadav: First 3 = "YAD", Last 4 digits of "9988776657" = "6657"
UPDATE teachers SET password = 'YAD6657' WHERE email = 'yadav@teacher.com';

-- Update Ms. Desai: First 3 = "DES", Last 4 digits of "9988776658" = "6658"
UPDATE teachers SET password = 'DES6658' WHERE email = 'desai@teacher.com';

-- If there are other teachers from previous data, update them too:
-- Narayan: NAR + 9473 (last 4 of 6371349473)
UPDATE teachers SET password = 'NAR9473' WHERE email = 'narayansahu2888@gmail.com';

-- Ramesh: RAM + 3001 (last 4 of 9876543001)
UPDATE teachers SET password = 'RAM3001' WHERE email = 'ramesh.kumar@gmail.com';

-- Verify all teachers have passwords
SELECT teacher_id, name, email, phone, password, is_active FROM teachers;
