DELETE FROM users_roles WHERE user_id = (SELECT id FROM users WHERE username = 'uniq_username');
DELETE FROM users WHERE username = 'uniq_username';
