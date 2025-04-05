CREATE TABLE products (
    id UUID PRIMARY KEY,
    shop_id UUID NOT NULL REFERENCES shops(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0.0,
    image_url VARCHAR(512),
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category VARCHAR(100),
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_products_shop_id ON products(shop_id);
CREATE INDEX idx_products_category ON products(category);