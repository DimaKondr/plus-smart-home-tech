CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_src VARCHAR(512),
    quantity_state VARCHAR(6) NOT NULL CHECK (quantity_state IN ('ENDED', 'FEW', 'ENOUGH', 'MANY')),
    product_state VARCHAR(10) NOT NULL CHECK (product_state IN ('ACTIVE', 'DEACTIVATE')),
    product_category VARCHAR(8) NOT NULL CHECK (product_category IN ('LIGHTING', 'CONTROL', 'SENSORS')),
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 1.00)
);