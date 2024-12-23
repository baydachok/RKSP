CREATE TABLE IF NOT EXISTS message_sr (
    id SERIAL PRIMARY KEY,
    text VARCHAR(255),
    createdAt TIMESTAMP,
    author VARCHAR(255)
);