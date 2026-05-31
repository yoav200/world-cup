--
-- Test users for local H2 development
-- All passwords are: password
-- BCrypt hash of "password" (cost 10)
--

-- Admin user (matches admin-emails in application-local.yml)
INSERT INTO account (id, email, first_name, last_name, enabled, locked, status, createdat)
VALUES (1, 'yoav.alhalel@bny.com',
        'Admin', 'User', true, false, 'ACTIVE', CURRENT_TIMESTAMP);

-- Regular user
INSERT INTO account (id, email, first_name, last_name, enabled, locked, status, createdat)
VALUES (2, 'user@test.com',
        'Test', 'User', true, false, 'ACTIVE', CURRENT_TIMESTAMP);

-- Second regular user (for comparison)
INSERT INTO account (id, email, first_name, last_name, enabled, locked, status, createdat)
VALUES (3, 'user2@test.com',
        'Second', 'User', true, false, 'ACTIVE', CURRENT_TIMESTAMP);

-- No need to pre-populate user_bet - the app creates entries when bets are placed
