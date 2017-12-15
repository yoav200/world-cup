CREATE TABLE Account (
  id              SERIAL PRIMARY KEY,
  email           VARCHAR UNIQUE,
  password        VARCHAR NOT NULL,
  firstName       VARCHAR,
  lastName        VARCHAR,
  fullName        VARCHAR,
  displayName     VARCHAR,
  gender          VARCHAR,
  location        VARCHAR,
  validatedId     VARCHAR,
  profileImageUrl VARCHAR,
  providerId      VARCHAR NOT NULL,
  country         VARCHAR,
  language        VARCHAR
);


CREATE TABLE Team (
  id              SERIAL PRIMARY KEY,
  name            VARCHAR UNIQUE,
  code            VARCHAR,
  shortName       VARCHAR,
  crestUrl        VARCHAR
);
