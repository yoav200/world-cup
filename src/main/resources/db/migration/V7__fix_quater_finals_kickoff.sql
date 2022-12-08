
-- fix quarter finals game kickoff


UPDATE  knockout_match SET homeTeamCode = 'WINNER_ROS4', awayTeamCode= 'WINNER_ROS3' WHERE matchId = 59;


-- QUARTER_FINAL NETHERLANDS vs ARGENTINA
UPDATE match SET kickoff = '2022-12-09 19:00:00+00' WHERE matchId = 57;
UPDATE bet SET locktime = '2022-12-09 19:00:00+00' WHERE id = 57;
-- QUARTER_FINAL CROATIA vs BRAZIL
UPDATE match SET kickoff = '2022-12-09 15:00:00+00' WHERE matchId = 58;
UPDATE bet SET locktime = '2022-12-09 15:00:00+00' WHERE id = 58;
-- QUARTER_FINAL FRANCE vs ENGLAND - note wrong home team!
UPDATE match SET kickoff = '2022-12-10 19:00:00+00' WHERE matchId = 59;
UPDATE bet SET locktime = '2022-12-10 19:00:00+00' WHERE id = 59;
-- QUARTER_FINAL MOROCCO vs PORTUGAL
UPDATE match SET kickoff = '2022-12-10 15:00:00+00' WHERE matchId = 60;
UPDATE bet SET locktime = '2022-12-10 15:00:00+00' WHERE id = 60;