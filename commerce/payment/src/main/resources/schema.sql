CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    product_price NUMERIC(10, 2) NOT NULL CHECK (product_price >= 1.00),
    delivery_price NUMERIC(10, 2) NOT NULL CHECK (delivery_price >= 1.00),
    total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 1.00),
    fee_total NUMERIC(10, 2) NOT NULL CHECK (fee_total >= 0.00),
    status VARCHAR(10) NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED'))
);