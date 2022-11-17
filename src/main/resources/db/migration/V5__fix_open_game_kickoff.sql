
-- fix first game kickoff

UPDATE match
    SET kickoff = '2022-11-20 16:00:00+00'
WHERE matchId = 1;


UPDATE bet
    SET locktime = '2022-11-20 16:00:00+00'
WHERE id = 1;