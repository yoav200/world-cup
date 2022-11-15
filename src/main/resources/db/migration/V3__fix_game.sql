
-- fix England (15) vs USA (28)  game
UPDATE group_match SET homeTeam = 15, awayTeam = 28 WHERE matchId = 20;

-- fix Canad (25) vs Morocco (3)   game
UPDATE group_match SET homeTeam = 25, awayTeam = 3 WHERE matchId = 42;