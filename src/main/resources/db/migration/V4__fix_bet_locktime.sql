
-- fix bet locktime

UPDATE bet
    SET locktime = match.kickoff
FROM match match
    WHERE id = match.matchid;