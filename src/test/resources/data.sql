INSERT INTO roles VALUES (1, 'ROLE_USER'), (2, 'ROLE_EMPLOYEE'), (3, 'ROLE_ADMIN');
ALTER SEQUENCE roles_seq RESTART WITH 4;

INSERT INTO users VALUES ('11111111-1111-1111-1111-111111111111', 'USER', '$2a$10$FDUgIaQPEVjt1rCwr/rjCO/JZShkfUBvrmpaJlhWNLCS8UH8jLu5y', 'dev_user'),
                         ('22222222-2222-2222-2222-222222222222', 'EMPLOYEE', '$2a$10$KOos0FtYUQi1sqi.gXTJZedZbrR3aUbbTDAijN0r8kspKPKbWvM4K', 'dev_employee'),
                         ('33333333-3333-3333-3333-333333333333', 'ADMIN', '$2a$10$ylqKA7G5d0gYrM.lIUwc9eIdg3vpza1HbQQHwSoSxD.9spyaO9jWm','dev_admin');
--passwords: dev_user, dev_employee, dev_admin


INSERT INTO users_roles VALUES ('11111111-1111-1111-1111-111111111111', 1),
                               ('22222222-2222-2222-2222-222222222222', 2),
                               ('33333333-3333-3333-3333-333333333333', 3);

INSERT INTO product_categories VALUES (1, 'DEFAULT'),
                                      (2, 'FOOD'),
                                      (3, 'DRINK');
ALTER SEQUENCE product_categories_seq RESTART WITH 4;

INSERT INTO products VALUES (1, NULL, FALSE, 'Salad', 240, NULL, 2),
                            (2, NULL, TRUE, 'Steak', 720, NULL, 2),
                            (3, NULL, TRUE, 'Red caviar', 700, 10, 2),
                            (4, NULL, TRUE, 'Tea', 60, NULL, 3),
                            (5, NULL, TRUE, 'Coffee', 110, NULL, 3),
                            (6, NULL, TRUE, 'Branded souvenir', 1000, 5, 1),
                            (7, NULL, FALSE, 'Sugar', 10, 0, 2);
ALTER SEQUENCE products_seq RESTART WITH 8;

INSERT INTO order_statuses VALUES (1, 'DEFAULT'),
                                  (2, 'PAID'),
                                  (3, 'SUCCESS'),
                                  (4, 'REJECTED');
ALTER SEQUENCE order_statuses_seq RESTART WITH 5;

--id, time, description, quantity, order_status, product_id, user_id
INSERT INTO orders VALUES (1, '2023-02-24 14:20:10', NULL, 1, 1, 6, '11111111-1111-1111-1111-111111111111'),
                          (2, '2023-02-24 14:20:10', NULL, 1, 2, 6, '11111111-1111-1111-1111-111111111111'),
                          (3, '2023-02-24 14:20:10', NULL, 1, 3, 6, '11111111-1111-1111-1111-111111111111'),
                          (4, '2023-02-24 14:20:10', NULL, 1, 4, 6, '11111111-1111-1111-1111-111111111111'),
                          (5, '2023-02-24 14:20:10', NULL, 1, 1, 2, '11111111-1111-1111-1111-111111111111'),
                          (6, '2023-02-24 14:20:10', NULL, 1, 1, 1, '11111111-1111-1111-1111-111111111111'),
                          (7, '2023-02-24 14:20:10', NULL, 10, 1, 6, '11111111-1111-1111-1111-111111111111'),
                          (8, '2023-02-24 14:20:10', NULL, 5, 1, 6, '11111111-1111-1111-1111-111111111111'),
                          (9, '2023-02-24 14:20:10', NULL, 3, 2, 7, '11111111-1111-1111-1111-111111111111');
ALTER SEQUENCE orders_seq RESTART WITH 10;

