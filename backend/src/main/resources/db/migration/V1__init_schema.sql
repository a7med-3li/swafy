CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID UNIQUE REFERENCES users(id)
);

CREATE TABLE corridor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    route VARCHAR(255),
    price DOUBLE PRECISION
);

CREATE TABLE vbs (
    id BIGSERIAL PRIMARY KEY,
    corridor_id BIGINT NOT NULL REFERENCES corridor(id),
    name VARCHAR(255) NOT NULL UNIQUE,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE driver_profile (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    national_id VARCHAR(255),
    license_number VARCHAR(255),
    wallet_balance NUMERIC(19,2) DEFAULT 0,
    active_corridor_id BIGINT REFERENCES corridor(id),
    is_on_shift BOOLEAN DEFAULT FALSE,
    approval_status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE driver_deposit (
    id BIGSERIAL PRIMARY KEY,
    driver_id UUID REFERENCES driver_profile(id),
    amount DOUBLE PRECISION NOT NULL,
    receive_date TIMESTAMP WITH TIME ZONE,
    is_approved BOOLEAN DEFAULT FALSE,
    is_refunded BOOLEAN DEFAULT FALSE,
    refund_reason VARCHAR(255),
    refund_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE passenger_profile (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    home_stop_id BIGINT,
    ride_balance INTEGER DEFAULT 0,
    sub_expires DATE,
    no_show_count INTEGER DEFAULT 0
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255) UNIQUE,
    latitude VARCHAR(255),
    longitude VARCHAR(255)
);

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
    rating INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    ride_id UUID,
    amount DOUBLE PRECISION,
    paid_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,
    passenger_id UUID NOT NULL,
    plan VARCHAR(30) NOT NULL,
    total_rides INTEGER NOT NULL,
    remaining_rides INTEGER NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(50),
    payment_reference VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    auto_renew BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_subscription_passenger ON subscription(passenger_id);
CREATE INDEX idx_subscription_status ON subscription(status);

CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_ride_passenger_status ON ride(passenger_id, status);
CREATE INDEX idx_ride_driver_status ON ride(driver_id, status);
CREATE INDEX idx_address_search ON address(address);
