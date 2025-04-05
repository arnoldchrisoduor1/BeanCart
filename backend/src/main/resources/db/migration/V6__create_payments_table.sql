CREATE TABLE payments (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    amount DECIMAL(10, 2) NOT NULL,
    stripe_payment_id VARCHAR(255) NOT NULL,
    stripe_customer_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    refunded BOOLEAN NOT NULL DEFAULT FALSE,
    refund_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_stripe_payment_id ON payments(stripe_payment_id);