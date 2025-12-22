-- Add subjectList column to classes table if it doesn't exist
-- This migration adds support for storing comma-separated subject lists per class

-- Check if column exists before adding (MySQL doesn't have IF NOT EXISTS for ALTER TABLE)
-- Run this manually or use your database migration tool

-- For MySQL:
ALTER TABLE classes ADD COLUMN subject_list TEXT NULL AFTER is_active;

-- Update existing classes with default subject list values (optional)
-- You can set specific subjects per class here
UPDATE classes SET subject_list = 'Mathematics, English, Science, Social Studies, Hindi, Physical Education' WHERE class_number = 1;
UPDATE classes SET subject_list = 'Mathematics, English, Science, Social Studies, Hindi, Physical Education' WHERE class_number = 2;
UPDATE classes SET subject_list = 'Mathematics, English, Science, Social Studies, Hindi, Computer Science' WHERE class_number = 3;

-- Add more classes as needed...

-- Verify the column was added
SHOW COLUMNS FROM classes LIKE 'subject_list';
