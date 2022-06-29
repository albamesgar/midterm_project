CREATE SCHEMA IF NOT EXISTS `midterm`;
USE `midterm` ;

CREATE SCHEMA IF NOT EXISTS `midterm_test`;

DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `third_party`;
DROP TABLE IF EXISTS `savings`;
DROP TABLE IF EXISTS `credit_card`;
DROP TABLE IF EXISTS `checking`;
DROP TABLE IF EXISTS `student_checking`;
DROP TABLE IF EXISTS `admin`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `account_holder`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE IF NOT EXISTS `role` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255),
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`password` VARCHAR(255),
	`username` VARCHAR(255),
	`role_id` BIGINT,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);

CREATE TABLE IF NOT EXISTS `account_holder` (
	`date_of_birth` DATETIME(6),
	`id` BIGINT NOT NULL,
    `primary_address_street` VARCHAR(255),
    `primary_address_home_number` INT,
    `primary_address_city` VARCHAR(255),
    `primary_address_postal_code` INT,
    `primary_address_country` VARCHAR(255),
    `mailing_address_street` VARCHAR(255),
    `mailing_address_home_number` INT,
    `mailing_address_city` VARCHAR(255),
    `mailing_address_postal_code` INT,
    `mailing_address_country` VARCHAR(255),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `account` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`account_type` VARCHAR(255),
	`balance_amount` DECIMAL(19,2),
	`balance_currency` VARCHAR(255),
	`penalty_fee_amount` DECIMAL(19,2),
	`penalty_fee_currency` VARCHAR(255),
	`secret_key` VARCHAR(255),
	`primary_owner_id` BIGINT NOT NULL,
	`secondary_owner_id` BIGINT,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`secondary_owner_id`) REFERENCES `account_holder` (`id`),
	FOREIGN KEY (`primary_owner_id`) REFERENCES `account_holder` (`id`)
);

CREATE TABLE IF NOT EXISTS `admin` (
	`id` BIGINT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `student_checking` (
	`creation_date` DATETIME(6),
	`status` VARCHAR(255),
	`id` BIGINT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `account` (`id`)
);

CREATE TABLE IF NOT EXISTS `checking` (
	`minimum_balance_amount` DECIMAL(19,2),
	`minimum_balance_currency` VARCHAR(255),
	`monthly_maintenance_fee_amount` DECIMAL(19,2),
	`monthly_maintenance_fee_currency` VARCHAR(255),
	`id` BIGINT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `student_checking` (`id`)
);

CREATE TABLE IF NOT EXISTS `credit_card` (
	`credit_limit_amount` DECIMAL(19,2),
	`credit_limit_currency` VARCHAR(255),
	`interest_rate` DECIMAL(19,2),
	`id` BIGINT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `account` (`id`)
);

CREATE TABLE IF NOT EXISTS `savings` (
	`creation_date` DATETIME(6),
	`interest_rate` DECIMAL(19,2),
	`minimum_balance_amount` DECIMAL(19,2),
	`minimum_balance_currency` VARCHAR(255),
	`status` VARCHAR(255),
	`id` BIGINT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`id`) REFERENCES `account` (`id`)
);

CREATE TABLE IF NOT EXISTS `third_party` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`hashed_key` VARCHAR(255),
	`name` VARCHAR(255),
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `transaction` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`amount` DECIMAL(19,2),
	`currency` VARCHAR(255),
	`date_time` DATETIME(6),
	`transaction_type` VARCHAR(255),
	`receiving_account_id` BIGINT,
	`sending_account_id` BIGINT,
	`sending_third_party_id` BIGINT,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`sending_account_id`) REFERENCES `account` (`id`),
	FOREIGN KEY (`receiving_account_id`) REFERENCES `account` (`id`),
	FOREIGN KEY (`sending_third_party_id`) REFERENCES `third_party` (`id`)
);

INSERT INTO role (name) VALUES
("ADMIN"),
("ACCOUNT_HOLDER");
SELECT * FROM role;

INSERT INTO third_party (name,hashed_key) VALUES
("Lia","1414");
SELECT * FROM third_party;

INSERT INTO user(username, password, role_id) VALUES
("Pepa","$2a$10$1aEP.6ZN/1kn7I94Zmm07OJSI2HuN1pyB5A80pEy47FPMOW7RumY.", 1),
("Llll","$2a$10$1aEP.6ZN/1kn7I94Zmm07OJSI2HuN1pyB5A80pEy47FPMOW7RumY.", 1);

INSERT INTO admin(id) VALUES
(1);