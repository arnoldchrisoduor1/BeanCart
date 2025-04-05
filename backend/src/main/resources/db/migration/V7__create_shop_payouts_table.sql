CREATE TABLE shop_payouts (
    id UUID PRIMARY KEY,
    shop_id UUID NOT NULL REFERENCES shops(id),
    amount DECIMAL(10, 2) NOT NULL,
    stripe_transfer_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_shop_payouts_shop_id ON shop_payouts(shop_id);