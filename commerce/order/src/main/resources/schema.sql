CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL,
    payment_id UUID NOT NULL,
    delivery_id UUID NOT NULL,
    state VARCHAR(30) NOT NULL CHECK (state IN ('NEW', 'ON_PAYMENT', 'ON_DELIVERY', 'DONE', 'DELIVERED', 'ASSEMBLED',
                                'PAID', 'COMPLETED', 'DELIVERY_FAILED', 'ASSEMBLY_FAILED', 'PAYMENT_FAILED',
                                'PRODUCT_RETURNED', 'CANCELED')),
    delivery_weight NUMERIC(6, 3) NOT NULL CHECK (delivery_weight >= 1),
    delivery_volume NUMERIC(6, 1) NOT NULL CHECK (delivery_volume >= 1),
    fragile BOOLEAN NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 1.00),
    product_price NUMERIC(10, 2) NOT NULL CHECK (product_price >= 1.00),
    delivery_price NUMERIC(10, 2) NOT NULL CHECK (delivery_price >= 1.00),
    username VARCHAR(255) NOT NULL,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    house VARCHAR(20) NOT NULL,
    flat VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    order_id UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id)
);