CREATE TABLE driver_transaction (
    id BIGSERIAL PRIMARY KEY,
    driver_profile_id UUID NOT NULL REFERENCES driver_profile(id),
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    balance_before NUMERIC(10,2) NOT NULL,
    balance_after NUMERIC(10,2) NOT NULL,
    reference VARCHAR(255),
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_driver_transaction_profile ON driver_transaction(driver_profile_id);

ALTER TABLE driver_deposit RENAME COLUMN driver_id TO driver_profile_id;
