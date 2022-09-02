CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email varchar(512),
  password TEXT
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);
ALTER TABLE users ADD COLUMN name varchar(128);