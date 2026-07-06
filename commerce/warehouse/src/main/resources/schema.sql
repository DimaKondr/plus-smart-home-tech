CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fragile BOOLEAN NOT NULL,
    width NUMERIC(6, 1) NOT NULL CHECK (width >= 1),
    height NUMERIC(6, 1) NOT NULL CHECK (height >= 1),
    depth NUMERIC(6, 1) NOT NULL CHECK (depth >= 1),
    weight NUMERIC(6, 3) NOT NULL CHECK (weight >= 1)
);

CREATE TABLE IF NOT EXISTS warehouse_stocks (
    product_id UUID PRIMARY KEY REFERENCES products (product_id),
    quantity BIGINT NOT NULL CHECK (quantity >= 1)
);

CREATE TABLE IF NOT EXISTS order_assemblies (
    order_id UUID PRIMARY KEY,
    delivery_id UUID NOT NULL
);