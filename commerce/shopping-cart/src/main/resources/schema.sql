CREATE TABLE IF NOT EXISTS shopping_carts (
    shopping_cart_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS shopping_cart_items (
    shopping_cart_id UUID NOT NULL REFERENCES shopping_carts(shopping_cart_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (shopping_cart_id, product_id)
);