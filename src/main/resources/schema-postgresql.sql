CREATE TABLE account (
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

CREATE TABLE team (
  id              SERIAL PRIMARY KEY,
  name            VARCHAR UNIQUE,
  code            VARCHAR NOT NULL,
  fifaRanking     INT,
  appearances     INT,
  titles          INT,
  confederation   VARCHAR NOT NULL,
  groupId         VARCHAR NOT NULL
);

CREATE TABLE match (
  matchId         SERIAL PRIMARY KEY,
  kickoff         TIMESTAMPZ
);

CREATE TABLE group_match (
  matchId         INT   REFERENCES match (matchId) PRIMARY KEY,
  homeTeam        INT   REFERENCES team (id),
  awayTeam        INT   REFERENCES team (id),
  groupId         VARCHAR NOT NULL
);

CREATE TABLE knockout_match (
  matchId         INT   REFERENCES match (matchId) PRIMARY KEY,
  matchCode       VARCHAR NOT NULL UNIQUE,
  stageId         VARCHAR NOT NULL,
  homeTeamCode    VARCHAR NOT NULL,
  awayTeamCode    VARCHAR NOT NULL
);

CREATE TABLE bet (
  id          SERIAL PRIMARY KEY,
  desription  VARCHAR NOT NULL,
  type        VARCHAR NOT NULL,
  matchId     INT   REFERENCES match (matchId),
  stageId     VARCHAR NOT NULL
);

CREATE TABLE knockout_team (
  matchId         INT REFERENCES knockout_match (matchId) PRIMARY KEY,
  homeTeam        INT REFERENCES team (id),
  awayTeam        INT REFERENCES team (id)
);

CREATE TABLE match_result(
  matchId         INT REFERENCES match (matchId) PRIMARY KEY,
  homeTeam        INT REFERENCES team (id),
  awayTeam        INT REFERENCES team (id),
  homeTeamGoals   INT,
  awayTeamGoals   INT
);

CREATE TABLE user_bet(
  accountId       INT REFERENCES account (id),
  betId           INT REFERENCES bet (id),
  homeTeam        INT REFERENCES team (id),
  awayTeam        INT REFERENCES team (id),
  homeTeamGoals   INT,
  awayTeamGoals   INT
  qualifier       INT REFERENCES team (id),
  PRIMARY KEY(accountId, betId),
  CONSTRAINT no_duplicate_bet UNIQUE (accountId, betId)
);

CREATE TABLE qualifier(
  knockoutTeamCode  VARCHAR PRIMARY KEY,
  teamId            INT REFERENCES team(id),
  stageId           VARCHAR NOT NULL
);


