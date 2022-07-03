DROP SCHEMA IF EXISTS midterm;
CREATE SCHEMA midterm;
USE midterm;

DROP SCHEMA IF EXISTS midterm_test;
CREATE SCHEMA midterm_test;

DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS credit_card;
DROP TABLE IF EXISTS savings;
DROP TABLE IF EXISTS student_checking;
DROP TABLE IF EXISTS checking;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS third_party;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS account_holder;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE user(
	id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    role_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE account_holder(
	id BIGINT NOT NULL,
    date_of_birth DATE,
    primary_address_street VARCHAR(255),
    primary_address_home_number INT,
    primary_address_city VARCHAR(255),
    primary_address_postal_code INT,
    primary_address_country VARCHAR(255),
    mailing_address_street VARCHAR(255),
    mailing_address_home_number INT,
    mailing_address_city VARCHAR(255),
    mailing_address_postal_code INT,
    mailing_address_country VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE admin(
	id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE third_party(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    hashed_key VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE account(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL(19,2),
    balance_currency VARCHAR(255),
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    penalty_fee_amount DECIMAL(19,2),
    penalty_fee_currency VARCHAR(255),
    secret_key VARCHAR(255),
    creation_date DATE,
    status VARCHAR(7),
    account_type VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE checking(
	id BIGINT NOT NULL,
    minimum_balance_amount DECIMAL(19,2) DEFAULT 250,
    minimum_balance_currency VARCHAR(255),
    monthly_maintenance_fee_amount DECIMAL(19,2) DEFAULT 12,
    monthly_maintenance_fee_currency VARCHAR(255),
    last_time_maintenance_fee_applied DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
);

CREATE TABLE student_checking(
	id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
);

CREATE TABLE savings(
	id BIGINT NOT NULL,
    minimum_balance_amount DECIMAL(19,2) DEFAULT 1000,
    minimum_balance_currency VARCHAR(255),
    interest_rate DECIMAL(11,5) DEFAULT 0.0025,
    last_time_interest_applied DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
);

CREATE TABLE credit_card(
	id BIGINT NOT NULL,
    credit_limit_amount DECIMAL(19,2) DEFAULT 100,
    credit_limit_currency VARCHAR(255),
    interest_rate DECIMAL(11,5) DEFAULT 0.2,
    last_time_interest_applied DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
);

CREATE TABLE transaction(
	id BIGINT NOT NULL AUTO_INCREMENT,
    transaction_type VARCHAR(255),
    transaction_date DATETIME,
    amount DECIMAL(19,2),
    currency VARCHAR(255),
    sending_account_id BIGINT,
    receiving_account_id BIGINT,
    third_party_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (sending_account_id) REFERENCES account(id),
    FOREIGN KEY (receiving_account_id) REFERENCES account(id),
    FOREIGN KEY (third_party_id) REFERENCES third_party(id)
);

INSERT INTO role (name) VALUES
("ADMIN"),
("ACCOUNT_HOLDER");

INSERT INTO user(username, password, role_id) VALUES
("Lua","$2a$10$IPv2SNSsFeueY67KnCl/YOBK2NVEgVm/qvF0Ou2nF8sovt30ydV8i", 1);

INSERT INTO admin(id) VALUES
(1);
