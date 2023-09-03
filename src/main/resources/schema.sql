CREATE TABLE payment
(
    payment_id BINARY(16) PRIMARY KEY,
    request_id VARCHAR(255) NOT NULL,
    merchant_id VARCHAR(255) NOT NULL,
    amount NUMERIC NOT NULL,
    currency VARCHAR(3) NOT NULL,
    card_holder VARCHAR(255) NOT NULL,
    card_number VARCHAR(255) NOT NULL,
    expiry_date VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description varchar(100),
    authorisation_code varchar(8)
);
