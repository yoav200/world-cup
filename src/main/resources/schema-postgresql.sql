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
  code            VARCHAR NOT NULL,
  fifaRanking     INT,
  appearances     INT,
  titles          INT,
  confederation   VARCHAR NOT NULL,
  groupId         VARCHAR NOT NULL
);

CREATE TABLE Match (
  matchId         SERIAL PRIMARY KEY,
  kickoff         TIMESTAMPZ
);

CREATE TABLE GroupMatch (
  matchId         INT   REFERENCES Match (matchId) PRIMARY KEY,
  homeTeam        INT   REFERENCES Team (id),
  awayTeam        INT   REFERENCES Team (id),
  groupId         VARCHAR NOT NULL
);

CREATE TABLE KnockoutMatch (
  matchId         INT   REFERENCES Match (matchId) PRIMARY KEY,
  matchCode       VARCHAR NOT NULL UNIQUE,
  stageId         VARCHAR NOT NULL,
  homeTeamCode    VARCHAR NOT NULL,
  awayTeamCode    VARCHAR NOT NULL
);

CREATE TABLE Bet (
  id          SERIAL PRIMARY KEY,
  desription  VARCHAR NOT NULL,
  type        VARCHAR NOT NULL,
  matchId     INT   REFERENCES Match (matchId),
  stageId     VARCHAR NOT NULL
);

CREATE TABLE KnockoutTeam (
  matchId         INT REFERENCES KnockoutMatch (matchId) PRIMARY KEY,
  homeTeam        INT REFERENCES Team (id),
  awayTeam        INT REFERENCES Team (id)
);

CREATE TABLE MatchResult(
  matchId         INT REFERENCES Match (matchId) PRIMARY KEY,
  homeTeam        INT REFERENCES Team (id),
  awayTeam        INT REFERENCES Team (id),
  homeTeamGoals   INT,
  awayTeamGoals   INT,
  winner          VARCHAR NOT NULL
);

CREATE TABLE UserBet(
  accountId       INT REFERENCES Account (id),
  betId           INT REFERENCES Bet (id),
  homeTeam        INT REFERENCES Team (id),
  awayTeam        INT REFERENCES Team (id),
  homeTeamGoals   INT,
  awayTeamGoals   INT,
  winner          VARCHAR NOT NULL,
  qualifier       REFERENCES Team (id),
  PRIMARY KEY(accountId, betId),
  CONSTRAINT no_duplicate_bet UNIQUE (accountId, betId)
);

CREATE TABLE Qualifiers(
  knockoutTeamCode  VARCHAR PRIMARY KEY,
  teamId            INT REFERENCES Team(id),
  stageId           VARCHAR NOT NULL
);


