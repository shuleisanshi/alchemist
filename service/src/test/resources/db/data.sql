DELETE
FROM children;
DELETE FROM user;

INSERT INTO children (id, name, user_id)
VALUES (1, 'Jone', 1),
       (2, 'Jack', 1),
       (3, 'Jack2', 1),
       (4, 'Jack', 15),
       (5, 'Billie', 15);

INSERT INTO user (id, name, age, email, deleted)
VALUES (1, 'Jone', 1, 'test1@baomidou.com', '0'),
       (2, 'Jack', 1, 'test2@baomidou.com', '0'),
       (3, 'Jack', 1, 'test2@baomidou.com', '0'),
       (4, 'Jack', 2, 'test2@baomidou.com', '0'),
       (5, 'Jack', 2, 'test2@baomidou.com', '0'),
       (6, 'Jack', 2, 'test2@baomidou.com', '0'),
       (7, 'Jack', 1, 'test2@baomidou.com', '0'),
       (8, 'Jack', 2, 'test2@baomidou.com', '0'),
       (9, 'Jack', 1, 'test2@baomidou.com', '0'),
       (10, 'Jack', 3, 'test2@baomidou.com', '0'),
       (11, 'Jack', 3, 'test2@baomidou.com', '0'),
       (12, 'Jack', 3, 'test2@baomidou.com', '0'),
       (13, 'Jack', 3, 'test2@baomidou.com', '0'),
       (14, 'Jack', 2, 'test2@baomidou.com', '0'),
       (15, 'Tom', 1, 'test3@baomidou.com', '0'),
       (16, 'Sandy', 3, 'test4@baomidou.com', '0'),
       (17, 'Billie', 2, 'test5@baomidou.com', '0');

