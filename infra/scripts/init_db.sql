-- init_db.sql: initial schema and seed data for Swafy

-- Create extensions or schemas if needed
-- Example: create a simple users table
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Insert a demo user (change or remove for production)
INSERT INTO users (username, email) VALUES ('demo', 'demo@swafy.local');
