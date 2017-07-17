CREATE DATABASE IF NOT EXISTS `security-example`;

DROP TABLE `user_roles`;
DROP TABLE `users`;

CREATE TABLE IF NOT EXISTS `users` (
    `username` VARCHAR(50)   NOT NULL PRIMARY KEY,
    `password` VARCHAR(500)  NOT NULL,
    `enabled`  TINYINT(1)
);

CREATE TABLE IF NOT EXISTS `user_roles` (
    `username`  VARCHAR(50) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_roles_users FOREIGN KEY(username) REFERENCES users(username)
);

INSERT INTO `users` VALUES ('user', 'password', 1);
INSERT INTO `user_roles` VALUES ('user', 'USER');