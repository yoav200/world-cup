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
  matchId         REFERENCES Match (matchId),
  homeTeam        REFERENCES Team (id),
  awayTeam        REFERENCES Team (id),
  groupId         VARCHAR NOT NULL
);

CREATE TABLE KnockoutMatch (
  matchId         REFERENCES Match (matchId),
  matchCode       VARCHAR NOT NULL,
  stageId         VARCHAR NOT NULL,
  homeTeamCode    VARCHAR NOT NULL,
  awayTeamCode    VARCHAR NOT NULL
);

CREATE TABLE Bet (
  id          SERIAL PRIMARY KEY,
  desription  VARCHAR NOT NULL,
  type        VARCHAR NOT NULL,
  matchId     REFERENCES Match (matchId),
  stageId     VARCHAR NOT NULL
);

CREATE TABLE KnockoutTeam (
  matchId         REFERENCES KnockoutMatch (matchId) PRIMARY KEY,
  homeTeam        REFERENCES Team (id),
  awayTeam        REFERENCES Team (id)
);

CREATE TABLE MatchResult(
  matchId         REFERENCES Match (matchId) PRIMARY KEY,
  homeTeamGoals   INT,
  awayTeamGoals   INT,
  winner          VARCHAR NOT NULL
);

CREATE TABLE UserBet(
  accountId       REFERENCES Account (id),
  betId           REFERENCES Bet (id),
  homeTeamGoals   INT,
  awayTeamGoals   INT,
  winner          VARCHAR NOT NULL
);

CREATE TABLE Qualifiers(
  teamId       REFERENCES Team(id),
  stageId      VARCHAR NOT NULL
);


