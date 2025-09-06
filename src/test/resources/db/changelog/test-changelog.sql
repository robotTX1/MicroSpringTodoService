--liquibase formatted sql

-- Test User 1 Todos
--changeset robotTX:load-test-user-1-todos
--Loads data for test user 1
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (1, 'Test Todo', 'My test todo description', '2027-10-02 09:15:30', false, null, false, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (2, 'No priority todo', 'My test todo description, this todo has categories', null, false, null, false, 1,
        '2025-09-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (3, 'Completed Todo', 'Job is done', '2025-10-02 09:15:30', true, null, false, 4,
        '2025-09-15 10:00:00', '2025-08-15 11:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (4, 'Todo with Parent', 'Can be done after the parent is done', '2025-11-02 10:15:30', false, 3,
        false, 5, '2025-10-15 15:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (5, 'Shared todo!', 'Shared with testuser2', '2026-12-02 10:15:30', false, null, true, 3,
        '2025-10-15 10:00:00', '2025-08-15 15:30:00');
COMMIT;

--changeset robotTX:load-test-user-1-todo-user
--Loads join table data for test user 1
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (1, 'testuser1', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (2, 'testuser1', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (3, 'testuser1', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (4, 'testuser1', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (5, 'testuser1', 4);
COMMIT;

--changeset robotTX:add-categories
--Adds categories
INSERT INTO category(ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'category-1', '2025-10-15 10:00:00', '2025-10-15 10:00:00');
INSERT INTO category(ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (2, 'category-2', '2025-10-15 10:00:00', '2025-10-15 10:00:00');
INSERT INTO category(ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (3, 'category-3', '2025-10-15 10:00:00', '2025-10-15 10:00:00');
INSERT INTO category(ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (4, 'category-4', '2025-10-15 10:00:00', '2025-10-15 10:00:00');
INSERT INTO category(ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (5, 'category-5', '2025-10-15 10:00:00', '2025-10-15 10:00:00');
COMMIT;

--changeset robotTX:load-test-user-1-todo-category
--Join todos with categories
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (1, 2);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (2, 2);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (3, 2);

INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (1, 3);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (3, 3);

INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (2, 4);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (4, 4);

INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (3, 5);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (4, 5);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (5, 5);
COMMIT;

-- Test User 2 Todos
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (6, 'User twos todo', 'Watch TV all day', '2027-10-02 09:15:30', false, null, false, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (7, 'Read Test', 'Shared with user1 with read privilege', '2027-10-02 09:15:30', false, null, true, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (8, 'Write Test', 'Shared with user1 with write privilege', '2027-10-02 09:15:30', false, null, true, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (9, 'Manage Test', 'Shared with user1 with manage privilege', '2027-10-02 09:15:30', false, null, true, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
COMMIT;

--changeset robotTX:load-test-user-2-todo-category
--Join todos with categories
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (1, 6);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (2, 6);
INSERT INTO category_todo(CATEGORY_ID, TODO_ID)
VALUES (3, 6);
COMMIT;

--changeset robotTX:load-test-user-2-todo-user
--Loads join table data for test user 2
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (6, 'testuser2', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (7, 'testuser2', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (8, 'testuser2', 4);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (9, 'testuser2', 4);

--changeset robotTX:load-test-user-1-shares
--Loads share data for test user 1
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (5, 'testuser2', 1);
COMMIT;

--changeset robotTX:load-test-user-2-shares
--Loads share data for test user 2
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (7, 'testuser1', 1);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (8, 'testuser1', 2);
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (9, 'testuser1', 3);
COMMIT;