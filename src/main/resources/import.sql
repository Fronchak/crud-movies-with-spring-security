INSERT INTO movie (title, synopsis, duration_in_minutes, note) VALUES ('Harry Potter and the Philosopher s Stone', 'Adaptation of the first of J.K. Rowling s popular children s novels about Harry Potter, a boy who learns on his eleventh birthday that he is the orphaned son of two powerful wizards and possesses unique magical powers of his own', 152, 4.5);
INSERT INTO movie (title, synopsis, duration_in_minutes, note) VALUES ('Harry Potter and the Chamber of Secrets', 'A house-elf warns Harry against returning to Hogwarts, but he decides to ignore it. When students and creatures at the school begin to get petrified, Harry finds himself surrounded in mystery.', 161, 4.4);
INSERT INTO movie (title, synopsis, duration_in_minutes, note) VALUES ('Harry Potter and the Prisoner of Azkaban', 'Harry, Ron and Hermoine return to Hogwarts just as they learn about Sirius Black and his plans to kill Harry. However, when Harry runs into him, he learns that the truth is far from reality.', 139, 4.5);

INSERT INTO tb_user (email, password) VALUES ('gabriel@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (email, password) VALUES ('fronchak@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO user_role (id_user, id_role) VALUES (1, 1);
INSERT INTO user_role (id_user, id_role) VALUES (2, 1);
INSERT INTO user_role (id_user, id_role) VALUES (2, 2);