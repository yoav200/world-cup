CREATE TABLE Account (
  id        SERIAL PRIMARY KEY,
  username  VARCHAR UNIQUE,
  password  VARCHAR NOT NULL,
  firstName VARCHAR NOT NULL,
  lastName  VARCHAR NOT NULL,
  email  VARCHAR UNIQUE
);