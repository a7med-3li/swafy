CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_role ON users(role);

CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID UNIQUE REFERENCES users(id)
);

CREATE TABLE corridor (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    start_lat DOUBLE PRECISION,
    start_lng DOUBLE PRECISION,
    start_address VARCHAR(255),
    destination_lat DOUBLE PRECISION,
    destination_lng DOUBLE PRECISION,
    destination_address VARCHAR(255),
    price NUMERIC(10,2) NOT NULL
);

CREATE TABLE vbs (
    id BIGSERIAL PRIMARY KEY,
    corridor_id BIGINT NOT NULL REFERENCES corridor(id),
    name VARCHAR(255) NOT NULL UNIQUE,
    vbs_lat DOUBLE PRECISION,
    vbs_lng DOUBLE PRECISION,
    vbs_address VARCHAR(255)
);

CREATE TABLE passengers (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE driver_profile (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    national_id VARCHAR(255),
    license_number VARCHAR(255),
    wallet_balance NUMERIC(19,2),
    active_corridor_id BIGINT REFERENCES corridor(id),
    is_on_shift BOOLEAN NOT NULL DEFAULT FALSE,
    approval_status VARCHAR(20)
);

CREATE TABLE driver_deposit (
    id BIGSERIAL PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES driver_profile(id),
    amount NUMERIC(10,2) NOT NULL,
    receive_date TIMESTAMP WITH TIME ZONE NOT NULL,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    is_refunded BOOLEAN NOT NULL DEFAULT FALSE,
    refund_reason VARCHAR(255),
    refund_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,
    passenger_id UUID NOT NULL REFERENCES passengers(id),
    corridor_id BIGINT NOT NULL REFERENCES corridor(id),
    payment_id BIGINT UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATE NOT NULL
);

CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL UNIQUE REFERENCES subscription(id),
    amount NUMERIC(10,2) NOT NULL,
    payment_method INTEGER NOT NULL,
    payment_reference VARCHAR(255),
    paid_at TIMESTAMP WITH TIME ZONE
);

ALTER TABLE subscription
    ADD CONSTRAINT fk_subscription_payment
    FOREIGN KEY (payment_id) REFERENCES payment(id);

CREATE TABLE ride (
    id UUID PRIMARY KEY,
    passenger_id UUID NOT NULL,
    driver_id UUID,
    corridor_id BIGINT,
    subscription_id BIGINT,
    pickup_vbs_id BIGINT,
    dropoff_vbs_id BIGINT,
    pickup_lat DOUBLE PRECISION,
    pickup_lng DOUBLE PRECISION,
    dropoff_lat DOUBLE PRECISION,
    dropoff_lng DOUBLE PRECISION,
    status VARCHAR(20) NOT NULL,
    estimated_fare NUMERIC(19,2),
    final_fare NUMERIC(19,2),
    pin VARCHAR(4),
    departure_time TIMESTAMP WITH TIME ZONE,
    requested_at TIMESTAMP WITH TIME ZONE,
    accepted_at TIMESTAMP WITH TIME ZONE,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    boarding_confirmed_at TIMESTAMP WITH TIME ZONE,
    no_show_marked_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE ride_rating (
    id BIGSERIAL PRIMARY KEY,
    ride_id UUID,
    rating INTEGER NOT NULL
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255) UNIQUE,
    latitude VARCHAR(255),
    longitude VARCHAR(255)
);
