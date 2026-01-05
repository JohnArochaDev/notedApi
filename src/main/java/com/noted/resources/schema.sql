CREATE TABLE IF NOT EXISTS user_folder (
    id UUID PRIMARY KEY
    user_id UUID NOT NULL UNIQUE,   -- a user_folder is owned by only one useer
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS folder (
    id UUID PRIMARY KEY,
    user_folder_id UUID NOT NULL,   -- which user this folder belongs to
    parent_id UUID,                 -- for nested folders (null = top-level)
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'folder',
    FOREIGN KEY (user_folder_id) REFERENCES user_folder(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES folder(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS node_file (
    id UUID PRIMARY KEY,
    parent_id UUID NOT NULL,        -- not nullable, a file MUST be owned by a folder
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'node',
    FOREIGN KEY (parent_id) REFERENCES folder(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS nodule (
    id UUID PRIMARY KEY,
    parent_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL DEFAULT 'textNode',
    x INT NOT NULL,
    y INT NOT NULL,
    width INT NOT NULL,
    height INT NOT NULL,
    text_content VARCHAR(10000),
    FOREIGN KEY (parent_id) REFERENCES node_file(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
