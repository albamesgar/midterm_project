DROP SCHEMA IF EXISTS midterm;
CREATE SCHEMA midterm;
USE midterm;

DROP SCHEMA IF EXISTS midterm_test;
CREATE SCHEMA midterm_test;
-- USE midterm_test;

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
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE user(
	id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    role_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE account_holder(
	id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    role_id BIGINT,
    date_of_birth DATE,
    street VARCHAR(255),
    door INT,
    city VARCHAR(255),
    postal_code INT,
    country VARCHAR(255),
    mailing_address VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE admin(
	id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    role_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE third_party(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    hashed_key VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE account(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    status VARCHAR(7),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE checking(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    secret_key INT,
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    minimum_balance_amount DECIMAL DEFAULT 250,
    minimum_balance_currency VARCHAR(255),
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    monthly_maintenance_fee_amount DECIMAL DEFAULT 12,
    monthly_maintenance_fee_currency VARCHAR(255),
    creation_date DATE,
    status VARCHAR(7),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE student_checking(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    secret_key INT,
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    creation_date DATE,
    status VARCHAR(7),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE savings(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    secret_key INT,
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    minimum_balance_amount DECIMAL DEFAULT 1000,
    minimum_balance_currency VARCHAR(255),
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    creation_date DATE,
    interest_rate DECIMAL DEFAULT 0.0025,
    status VARCHAR(7),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE credit_card(
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    credit_limit_amount DECIMAL DEFAULT 100,
    credit_limit_currency VARCHAR(255),
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    interest_rate DECIMAL DEFAULT 0.2,
    status VARCHAR(7),
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

