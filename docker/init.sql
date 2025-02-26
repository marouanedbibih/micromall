-- Create the database
CREATE DATABASE catalog_db;

-- Grant all privileges on the database to the user
GRANT ALL PRIVILEGES ON DATABASE catalog_db TO "user";

-- Connect to the database to grant table-level privileges
\c catalog_db;

-- Grant all privileges on all tables in the database schema
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "user";

-- Grant all privileges on all sequences in the database schema
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "user";

-- Grant all privileges on all functions in the database schema
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "user";

-- Make sure future tables and sequences are also granted the privileges automatically
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO "user";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO "user";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO "user";
