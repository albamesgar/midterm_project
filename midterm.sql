DROP SCHEMA IF EXISTS midterm;
CREATE SCHEMA midterm;
USE midterm;

DROP SCHEMA IF EXISTS midterm_test;
CREATE SCHEMA midterm_test;
-- USE midterm_test;

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
    home_number INT,
    city VARCHAR(255),
    postal_code INT,
    country VARCHAR(255),
    mailing_address VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE admin(
	id BIGINT,
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
    balance_amount DECIMAL,
    balance_currency VARCHAR(255),
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    penalty_fee_amount DECIMAL DEFAULT 40,
    penalty_fee_currency VARCHAR(255),
    status VARCHAR(7),
    account_type VARCHAR(255),
    secret_key INT,
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
    account_type VARCHAR(255),
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
    account_type VARCHAR(255),
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
    account_type VARCHAR(255),
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
    account_type VARCHAR(255),
    secret_key INT,
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
);

CREATE TABLE transaction(
	id BIGINT NOT NULL AUTO_INCREMENT,
    date_time DATETIME,
    transaction_type VARCHAR(255),
    amount DECIMAL,
    currency VARCHAR(255),
    sending_account_id BIGINT,
    receiving_account_id BIGINT,
    sending_third_party_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (sending_account_id) REFERENCES account(id),
    FOREIGN KEY (receiving_account_id) REFERENCES account(id),
    FOREIGN KEY (sending_third_party_id) REFERENCES account(id)
);

INSERT INTO role (name) VALUES
("ADMIN"),
("ACCOUNT_HOLDER");

SELECT * FROM role;

INSERT INTO account_holder(username, password, role_id, date_of_birth, street, home_number, city, postal_code,country, mailing_address) VALUES
("Alba","$2a$10$1aEP.6ZN/1kn7I94Zmm07OJSI2HuN1pyB5A80pEy47FPMOW7RumY.", 2, "1997-03-29", "canelones", 25, "Badalona", 08917, "Spain", "alba@gmail.com");

SELECT * FROM account_holder;

INSERT INTO admin(username, password, role_id) VALUES
("Pepa","$2a$10$1aEP.6ZN/1kn7I94Zmm07OJSI2HuN1pyB5A80pEy47FPMOW7RumY.", 1),
("Llll","$2a$10$1aEP.6ZN/1kn7I94Zmm07OJSI2HuN1pyB5A80pEy47FPMOW7RumY.", 1);
SELECT * FROM admin;

INSERT INTO third_party (name,hashed_key) VALUES
("Lia","1414");
SELECT * FROM third_party;