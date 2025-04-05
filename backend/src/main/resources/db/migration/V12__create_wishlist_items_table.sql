CREATE TABLE wishlist_items (
    id UUID PRIMARY KEY,
    wishlist_id UUID NOT NULL REFERENCES wishlists(id),
    product_id UUID NOT NULL REFERENCES products(id),
    added_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_wishlist_items_wishlist_id ON wishlist_items(wishlist_id);
CREATE INDEX idx_wishlist_items_product_id ON wishlist_items(product_id);
CREATE UNIQUE INDEX idx_wishlist_items_unique ON wishlist_items(wishlist_id, product_id);