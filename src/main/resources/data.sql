-- Clear existing data (optional, good for fresh start each time)
DELETE FROM nodule;
DELETE FROM node_file;
DELETE FROM folder;
DELETE FROM user_folder;
DELETE FROM users;

-- 1. Create a test user
INSERT INTO users (user_id, username, password)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'testuser',
    '$2a$10$dwOzqmzqcYESXDzQkLlDoOXxbEZ9WMIe6oVr16z6djxHGmolxZqbO'  -- bcrypt hash for "password"
);

-- 2. Create the user's root UserFolder
INSERT INTO user_folder (id, user_id)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111'
);

-- 3. Create folders (with proper nesting)
-- Root folders (parent_id NULL)
INSERT INTO folder (id, user_folder_id, parent_id, name, type)
VALUES
('30000000-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', NULL, 'Documents', 'folder'),
('30000000-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', NULL, 'Photos', 'folder'),
('30000000-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222222', NULL, 'Projects', 'folder');

-- Subfolders under Documents
INSERT INTO folder (id, user_folder_id, parent_id, name, type)
VALUES
('40000000-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', '30000000-0000-0000-0000-000000000001', 'Work', 'folder'),
('40000000-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', '30000000-0000-0000-0000-000000000001', 'Personal', 'folder');

-- Deeply nested under Work
INSERT INTO folder (id, user_folder_id, parent_id, name, type)
VALUES
('50000000-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', '40000000-0000-0000-0000-000000000001', 'Reports', 'folder'),
('50000000-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', '40000000-0000-0000-0000-000000000001', 'Meetings', 'folder');

-- 4. Create some nodes (notes/files)
INSERT INTO node_file (id, parent_id, name, type)
VALUES
('60000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Resume.pdf', 'node'),
('60000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', 'Shopping List.txt', 'node'),
('60000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000001', 'Q4 Report.md', 'node'),
('60000000-0000-0000-0000-000000000004', '30000000-0000-0000-0000-000000000002', 'Vacation.jpg', 'node');