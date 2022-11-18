
-- fix  qualifiers bet locktime

UPDATE bet
    SET locktime = '2022-11-20 16:00:00+00'
WHERE id > 64;