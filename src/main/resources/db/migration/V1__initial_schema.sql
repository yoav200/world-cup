--
-- PostgreSQL database dump
--
-- Started on 2022-10-17 18:19:09


CREATE TABLE public.account (
    id bigint NOT NULL,
    createdat timestamp without time zone,
    email character varying(45) NOT NULL,
    enabled boolean,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    locked boolean,
    password character varying(64) NOT NULL,
    status character varying(255),
    updatedatetime timestamp without time zone
);


CREATE TABLE public.bet (
    id bigint NOT NULL,
    description character varying(255),
    locktime timestamp without time zone,
    matchid bigint,
    stageid character varying(255),
    type character varying(255)
);


CREATE TABLE public.confirmationtoken (
    id bigint NOT NULL,
    confirmedat timestamp without time zone,
    createdat timestamp without time zone,
    expiresat timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    updatedatetime timestamp without time zone,
    usages integer NOT NULL,
    account_id bigint NOT NULL
);


CREATE TABLE public.group_match (
    groupid character varying(255),
    matchid bigint NOT NULL,
    awayteam bigint,
    hometeam bigint
);


CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE public.knockout_match (
    awayteamcode character varying(255),
    hometeamcode character varying(255),
    matchcode character varying(255),
    stageid character varying(255),
    matchid bigint NOT NULL
);


CREATE TABLE public.knockout_team (
    matchid bigint NOT NULL,
    awayteam bigint,
    hometeam bigint
);


CREATE TABLE public.match (
    matchid bigint NOT NULL,
    kickoff timestamp without time zone,
    status integer
);


CREATE SEQUENCE public.match_matchid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE TABLE public.match_result (
    matchid bigint NOT NULL,
    awayteamgoals integer,
    hometeamgoals integer,
    matchqualifier integer,
    awayteam bigint,
    hometeam bigint
);


CREATE TABLE public.qualifier (
    knockoutteamcode character varying(255) NOT NULL,
    stageid character varying(255),
    teamid bigint
);

CREATE TABLE public.ranking (
    id bigint NOT NULL,
    date timestamp without time zone,
    points integer,
    accountid bigint
);


CREATE SEQUENCE public.ranking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE public.team (
    id bigint NOT NULL,
    appearances integer,
    code character varying(255),
    confederation character varying(255),
    fifaranking integer,
    groupid character varying(255),
    name character varying(255),
    titles integer
);


CREATE TABLE public.user_bet (
    awayteamgoals integer,
    hometeamgoals integer,
    knockoutteamcode integer,
    accountid bigint NOT NULL,
    betid bigint NOT NULL,
    awayteam bigint,
    hometeam bigint,
    qualifier bigint
);


ALTER TABLE ONLY public.match ALTER COLUMN matchid SET DEFAULT nextval('public.match_matchid_seq'::regclass);


ALTER TABLE ONLY public.ranking ALTER COLUMN id SET DEFAULT nextval('public.ranking_id_seq'::regclass);


ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.bet
    ADD CONSTRAINT bet_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.confirmationtoken
    ADD CONSTRAINT confirmationtoken_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.group_match
    ADD CONSTRAINT group_match_pkey PRIMARY KEY (matchid);


ALTER TABLE ONLY public.knockout_match
    ADD CONSTRAINT knockout_match_pkey PRIMARY KEY (matchid);


ALTER TABLE ONLY public.knockout_team
    ADD CONSTRAINT knockout_team_pkey PRIMARY KEY (matchid);


ALTER TABLE ONLY public.match
    ADD CONSTRAINT match_pkey PRIMARY KEY (matchid);


ALTER TABLE ONLY public.match_result
    ADD CONSTRAINT match_result_pkey PRIMARY KEY (matchid);


ALTER TABLE ONLY public.qualifier
    ADD CONSTRAINT qualifier_pkey PRIMARY KEY (knockoutteamcode);


ALTER TABLE ONLY public.ranking
    ADD CONSTRAINT ranking_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.confirmationtoken
    ADD CONSTRAINT uk_5yeek6arig5t6anh1oo2s5sar UNIQUE (token);


ALTER TABLE ONLY public.account
    ADD CONSTRAINT uk_q0uja26qgu1atulenwup9rxyr UNIQUE (email);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT user_bet_pkey PRIMARY KEY (accountid, betid);


ALTER TABLE ONLY public.knockout_match
    ADD CONSTRAINT fk2eke8kdffthylcl4kegigx6xs FOREIGN KEY (matchid) REFERENCES public.match(matchid);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT fk4b8yu2ukr1o5ppteascwio75j FOREIGN KEY (accountid) REFERENCES public.account(id);


ALTER TABLE ONLY public.knockout_team
    ADD CONSTRAINT fk5nbjpb11du7q7434sldmrjeoy FOREIGN KEY (awayteam) REFERENCES public.team(id);


ALTER TABLE ONLY public.ranking
    ADD CONSTRAINT fk6qv7r3hti60dasjj793iki7fe FOREIGN KEY (accountid) REFERENCES public.account(id);


ALTER TABLE ONLY public.group_match
    ADD CONSTRAINT fk7gb0gnocoyfphvsnvy9h8e8c FOREIGN KEY (matchid) REFERENCES public.match(matchid);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT fk7huvf4ughpqdylrx07d8e549o FOREIGN KEY (hometeam) REFERENCES public.team(id);


ALTER TABLE ONLY public.knockout_team
    ADD CONSTRAINT fkb16c9podhm48u07yhrv0y1f8w FOREIGN KEY (hometeam) REFERENCES public.team(id);


ALTER TABLE ONLY public.group_match
    ADD CONSTRAINT fkgn9rhtdt2qq2tmnft32wi1tms FOREIGN KEY (hometeam) REFERENCES public.team(id);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT fkh05slrqce9psiu1a78uakk0f6 FOREIGN KEY (qualifier) REFERENCES public.team(id);


ALTER TABLE ONLY public.qualifier
    ADD CONSTRAINT fkh86wg757nympdrhkpcb81bfn0 FOREIGN KEY (teamid) REFERENCES public.team(id);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT fkj2oy612nryr9l2tqsu0a85eh3 FOREIGN KEY (betid) REFERENCES public.bet(id);


ALTER TABLE ONLY public.match_result
    ADD CONSTRAINT fkj744my4jm4bgb9oixruj2nwj1 FOREIGN KEY (hometeam) REFERENCES public.team(id);


ALTER TABLE ONLY public.match_result
    ADD CONSTRAINT fkkp7p9tgpthbbkq1qli88d49qk FOREIGN KEY (awayteam) REFERENCES public.team(id);


ALTER TABLE ONLY public.user_bet
    ADD CONSTRAINT fkl4edlaqbn3x4gt4tpm5kix7t7 FOREIGN KEY (awayteam) REFERENCES public.team(id);


ALTER TABLE ONLY public.group_match
    ADD CONSTRAINT fknmhppe72nbkrqlha9tmsvfmq1 FOREIGN KEY (awayteam) REFERENCES public.team(id);


ALTER TABLE ONLY public.confirmationtoken
    ADD CONSTRAINT fkp8s3u5mts4v2ohshjshifm29v FOREIGN KEY (account_id) REFERENCES public.account(id);


-- Completed on 2022-10-17 18:19:09
--
-- PostgreSQL database dump complete
--

