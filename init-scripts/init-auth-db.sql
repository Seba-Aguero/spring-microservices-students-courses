CREATE DATABASE IF NOT EXISTS `msvc-auth`;
USE `msvc-auth`;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`),
  UNIQUE KEY `UK_email` (`email`)
);

CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_role_id` (`role_id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);


INSERT IGNORE INTO `roles` (`name`) VALUES ('ROLE_USER');
INSERT IGNORE INTO `roles` (`name`) VALUES ('ROLE_ADMIN');