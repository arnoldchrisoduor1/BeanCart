CREATE TABLE reviews (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    shop_id UUID NOT NULL REFERENCES shops(id),
    buyer_id UUID NOT NULL REFERENCES users(id),
    order_item_id UUID NOT NULL REFERENCES order_items(id),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_reviews_product_id ON reviews(product_id);
CREATE INDEX idx_reviews_shop_id ON reviews(shop_id);
CREATE INDEX idx_reviews_buyer_id ON reviews(buyer_id);