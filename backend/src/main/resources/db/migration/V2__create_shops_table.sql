CREATE TABLE shops (
    id UUID PRIMARY KEY,
    seller_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT 'Brand new coffee shop',
    logo_url VARCHAR(512) DEFAULT 'https://robohash.org/shop',
    address TEXT,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_shops_seller_id ON shops(seller_id);