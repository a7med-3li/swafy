CREATE TABLE IF NOT EXISTS fare_configs (
      id BIGSERIAL PRIMARY KEY,
      vehicle_type VARCHAR(30) NOT NULL,
      city VARCHAR(50) NOT NULL,
      base_fare NUMERIC(10,2) NOT NULL,
      per_km_rate NUMERIC(10,2) NOT NULL,
      per_minute_rate NUMERIC(10,2) NOT NULL,
      minimum_fare NUMERIC(10,2) NOT NULL,
      waiting_rate_per_minute NUMERIC(10,2) NOT NULL,
      surge_multiplier NUMERIC(4,2) NOT NULL DEFAULT 1.0,
      active BOOLEAN NOT NULL DEFAULT true,
      effective_from TIMESTAMP NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_fare_configs_lookup
    ON fare_configs (vehicle_type, city, active, effective_from DESC);

-- seed initial rate for Beni Suef
INSERT INTO fare_configs
(vehicle_type, city, base_fare, per_km_rate, per_minute_rate,
 minimum_fare, waiting_rate_per_minute, surge_multiplier, active, effective_from)
VALUES
    ('CAR', 'BENI_SUEF', 7.00, 2.75, 0.30, 15.00, 0.50, 1.0, true, now());
