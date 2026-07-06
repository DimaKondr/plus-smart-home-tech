CREATE TABLE IF NOT EXISTS delivery (
    delivery_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    delivery_state VARCHAR(20) NOT NULL CHECK (delivery_state IN ('CREATED', 'IN_PROGRESS',
                                               'DELIVERED', 'FAILED', 'CANCELLED')),
    delivery_weight NUMERIC(6, 3) NOT NULL CHECK (delivery_weight >= 1),
    delivery_volume NUMERIC(6, 1) NOT NULL CHECK (delivery_volume >= 1),
    fragile BOOLEAN NOT NULL,
    from_country VARCHAR(50) NOT NULL,
    from_city VARCHAR(50) NOT NULL,
    from_street VARCHAR(50) NOT NULL,
    from_house VARCHAR(20) NOT NULL,
    from_flat VARCHAR(20) NOT NULL,
    to_country VARCHAR(50) NOT NULL,
    to_city VARCHAR(50) NOT NULL,
    to_street VARCHAR(50) NOT NULL,
    to_house VARCHAR(20) NOT NULL,
    to_flat VARCHAR(20) NOT NULL
);