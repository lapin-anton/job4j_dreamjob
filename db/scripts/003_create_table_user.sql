CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email TEXT,
  password TEXT
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);