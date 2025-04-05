CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) DEFAULT 'no_firstname',
    last_name VARCHAR(100) DEFAULT 'no_lastname',
    profile_url VARCHAR(100) DEFAULT 'https://robohash.org/bean'
    role VARCHAR(50)  NOT NULL DEFAULT 'buyer',
    is_verified BOOLEAN DEFAULT FALSE
    verification_code VARCHAR(255) DEFAULT 'no_code'
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);