-- Create refdata schema and price table
CREATE SCHEMA IF NOT EXISTS refdata;

CREATE TABLE IF NOT EXISTS refdata.price (
    id SERIAL PRIMARY KEY,
    ticker VARCHAR(32),
    name VARCHAR(255),
    exchange_short VARCHAR(128),
    exchange_long VARCHAR(255),
    mic_code VARCHAR(64),
    currency VARCHAR(8),
    price NUMERIC(34, 8),
    day_high NUMERIC(34, 8),
    day_low NUMERIC(34, 8),
    day_open NUMERIC(34, 8),
    week52_high NUMERIC(34, 8),
    week52_low NUMERIC(34, 8),
    market_cap NUMERIC(34, 8),
    previous_close_price NUMERIC(34, 8),
    previous_close_price_time TIMESTAMP WITH TIME ZONE,
    day_change NUMERIC(34, 8),
    volume BIGINT,
    is_extended_hours_price BOOLEAN,
    last_trade_time TIMESTAMP WITH TIME ZONE,
    source VARCHAR(255) NOT NULL,
    pulled_at TIMESTAMP WITH TIME ZONE NOT NULL
);
