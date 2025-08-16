--liquibase formatted sql

-- Test User 1 Todos
--changeset robotTX:load-test-user-1-todos
--Loads data for test user 1
--rollback DELETE FROM todo WHERE owner = 'testuser1'
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, OWNER, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (1, 'Test Todo', 'My test todo description', '2027-10-02 09:15:30', false, 'testuser1', null, false, 3,
        '2025-08-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, OWNER, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (2, 'No priority todo', 'My test todo description, this todo has categories', null, false,
        'testuser1', null, false, 1, '2025-09-15 10:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, OWNER, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (3, 'Completed Todo', 'Job is done', '2025-10-02 09:15:30', true, 'testuser1', null, false, 4,
        '2025-09-15 10:00:00', '2025-08-15 11:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, OWNER, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (4, 'Todo with Parent', 'Can be done after the parent is done', '2025-11-02 10:15:30', false, 'testuser1', 3,
        false, 5, '2025-10-15 15:00:00', '2025-08-15 10:00:00');
INSERT INTO todo(ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, OWNER, PARENT, SHARED, PRIORITY, CREATED_AT, UPDATED_AT)
VALUES (5, 'Shared todo!', 'Shared with testuser2', '2026-12-02 10:15:30', false, 'testuser1', null, true, 3,
        '2025-10-15 10:00:00', '2025-08-15 15:30:00');
COMMIT;


--changeset robotTX:load-test-user-1-todo-user
--Loads join table data for test user 1
--rollback DELETE FROM todo_user WHERE user_id = 'testuser1'
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
--rollback DELETE FROM category
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
--rollback DELETE FROM category_todo
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
--changeset robotTX:load-test-user-1-shares
--Loads join table data for test user 2
--rollback DELETE FROM todo_user WHERE user_id = 'testuser2' AND todo_id = 5
INSERT INTO todo_user(TODO_ID, USER_ID, ACCESS_LEVEL)
VALUES (5, 'testuser2', 1);
COMMIT;