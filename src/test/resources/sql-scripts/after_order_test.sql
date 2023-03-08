DELETE FROM orders;
DELETE FROM products;

INSERT INTO products VALUES (1, NULL, FALSE, 'Salad', 240, NULL, 2),
                            (2, NULL, TRUE, 'Steak', 720, NULL, 2),
                            (3, NULL, TRUE, 'Red caviar', 700, 10, 2),
                            (4, NULL, TRUE, 'Tea', 60, NULL, 3),
                            (5, NULL, TRUE, 'Coffee', 110, NULL, 3),
                            (6, NULL, TRUE, 'Branded souvenir', 1000, 5, 1),
                            (7, NULL, FALSE, 'Sugar', 10, 0, 2);
ALTER SEQUENCE products_seq RESTART WITH 8;

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