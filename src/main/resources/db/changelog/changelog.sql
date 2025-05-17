--liquibase formatted sql

--changeset robotTX:create-priority-table
--Creates Priority table
--rollback DROP TABLE priority
CREATE TABLE priority
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    priority_level INT UNIQUE  NOT NULL,
    name           VARCHAR(30) NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

--changeset robotTX:create-category-table
--Creates Category table
--rollback DROP TABLE category
CREATE TABLE category
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(30) UNIQUE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

--changeset robotTX:create-todo-table
--Creates Todo table
--rollback DROP TABLE todo
CREATE TABLE todo
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(100),
    description VARCHAR(4000),
    deadline    DATETIME NULL,
    completed   BOOLEAN     NOT NULL DEFAULT FALSE,
    owner       VARCHAR(36) NOT NULL,
    parent      BIGINT NULL,
    shared      BOOLEAN     NOT NULL DEFAULT FALSE,
    priority    BIGINT      NOT NULL,
    created_at  DATETIME             DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent) REFERENCES todo (id),
    FOREIGN KEY (priority) REFERENCES priority (id),
    CONSTRAINT check_has_content CHECK (title IS NOT NULL OR description IS NOT NULL),
    INDEX       idx_todo_title (title)
) AUTO_INCREMENT = 1000000;

--changeset robotTX:create-category_todo-join-table
--Creates Category_todo join table
--rollback DROP TABLE category_todo
CREATE TABLE category_todo
(
    category_id BIGINT NOT NULL,
    todo_id     BIGINT NOT NULL,
    PRIMARY KEY (category_id, todo_id),
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE,
    FOREIGN KEY (todo_id) REFERENCES todo (id) ON DELETE CASCADE,
    INDEX       idx_todo_id (todo_id)
);


--changeset robotTX:create-access_level-table
--Creates Access Level table
--rollback DROP TABLE access_level
CREATE TABLE access_level
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    access_level BIGINT UNIQUE NOT NULL,
    name         VARCHAR(30)   NOT NULL,
    description  VARCHAR(100)  NOT NULL
);

--changeset robotTX:create-todo_user-join-table
--Creates Todo_user join table
--rollback DROP TABLE todo_user CASCADE CONSTRAINTS
CREATE TABLE todo_user
(
    todo_id      BIGINT      NOT NULL,
    user_id      VARCHAR(36) NOT NULL,
    access_level BIGINT      NOT NULL,
    PRIMARY KEY (todo_id, user_id),
    FOREIGN KEY (todo_id) REFERENCES todo (id) ON DELETE CASCADE,
    FOREIGN KEY (access_level) REFERENCES access_level (id),
    INDEX        idx_user_id (user_id)
);

--changeset robotTX:add-priorities
--Add values for priorities
--rollback DELETE FROM priority
INSERT INTO priority(ID, PRIORITY_LEVEL, NAME)
VALUES (1, 0, 'Not required');
INSERT INTO priority(ID, PRIORITY_LEVEL, NAME)
VALUES (2, 1, 'Low');
INSERT INTO priority(ID, PRIORITY_LEVEL, NAME)
VALUES (3, 2, 'Normal');
INSERT INTO priority(ID, PRIORITY_LEVEL, NAME)
VALUES (4, 3, 'High');
INSERT INTO priority(ID, PRIORITY_LEVEL, NAME)
VALUES (5, 4, 'Critical');
COMMIT;

--changeset robotTX:add-access-levels
--Add available access levels
--rollback DELETE FROM access_level
INSERT INTO access_level(ACCESS_LEVEL, NAME, DESCRIPTION)
VALUES (0, 'READ', 'Allows the user to read the contents of the Todo');
INSERT INTO access_level(ACCESS_LEVEL, NAME, DESCRIPTION)
VALUES (1, 'WRITE', 'Allows the user to read and write the contents of the Todo');
INSERT INTO access_level(ACCESS_LEVEL, NAME, DESCRIPTION)
VALUES (2, 'MANAGE', 'Allows the user to read, write and manage the Todo');
INSERT INTO access_level(ACCESS_LEVEL, NAME, DESCRIPTION)
VALUES (3, 'OWNER', 'Allows the user to read, write and manage the Todo');
COMMIT;

--changeset robotTX:create-resource-limit-table
--Create resource limit table
--rollback DROP TABLE resource_limit
CREATE TABLE resource_limit
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource   varchar(50) UNIQUE NOT NULL,
    max_number integer            NOT NULL
);

--changeset robotTX:add-resource-limits
--Add values for resource limit
--rollback DELETE FROM resource_limit
INSERT INTO resource_limit(id, resource, max_number)
VALUES (1, 'TODO_LIMIT', 100);
INSERT INTO resource_limit(id, resource, max_number)
VALUES (2, 'CATEGORY_LIMIT_PER_TODO', 10);
INSERT INTO resource_limit(id, resource, max_number)
VALUES (3, 'SHARE_LIMIT_PER_TODO', 10);

--changeset robotTX:fix-character-set-and-collation
--Fixed character set for categories
ALTER TABLE category
    MODIFY name VARCHAR (30)
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_bin;
