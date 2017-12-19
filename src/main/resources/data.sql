

INSERT INTO Match (matchId, kickoff) VALUES
  (1, '2018-06-14 15:00:00+00'),
  (2, '2018-06-15 12:00:00+00'),
  (3, '2018-06-15 15:00:00+00'),
  (4, '2018-06-15 18:00:00+00'),
  (5, '2018-06-16 10:00:00+00'),
  (6, '2018-06-16 13:00:00+00'),
  (7, '2018-06-16 16:00:00+00'),
  (8, '2018-06-16 19:00:00+00'),
  (9, '2018-06-17 12:00:00+00'),
  (10, '2018-06-17 15:00:00+00'),
  (11, '2018-06-17 18:00:00+00'),
  (12, '2018-06-18 12:00:00+00'),
  (13, '2018-06-18 15:00:00+00'),
  (14, '2018-06-18 18:00:00+00'),
  (15, '2018-06-19 12:00:00+00'),
  (16, '2018-06-19 15:00:00+00'),
  (17, '2018-06-19 18:00:00+00'),
  (18, '2018-06-20 12:00:00+00'),
  (19, '2018-06-20 15:00:00+00'),
  (20, '2018-06-20 18:00:00+00'),
  (21, '2018-06-21 12:00:00+00'),
  (22, '2018-06-21 15:00:00+00'),
  (23, '2018-06-21 18:00:00+00'),
  (24, '2018-06-22 12:00:00+00'),
  (25, '2018-06-22 15:00:00+00'),
  (26, '2018-06-22 18:00:00+00'),
  (27, '2018-06-23 12:00:00+00'),
  (28, '2018-06-23 15:00:00+00'),
  (29, '2018-06-23 18:00:00+00'),
  (30, '2018-06-24 12:00:00+00'),
  (31, '2018-06-24 15:00:00+00'),
  (32, '2018-06-24 18:00:00+00'),
  (33, '2018-06-25 14:00:00+00'),
  (34, '2018-06-25 14:00:00+00'),
  (35, '2018-06-25 18:00:00+00'),
  (36, '2018-06-25 18:00:00+00'),
  (37, '2018-06-26 14:00:00+00'),
  (38, '2018-06-26 14:00:00+00'),
  (39, '2018-06-26 18:00:00+00'),
  (40, '2018-06-26 18:00:00+00'),
  (41, '2018-06-27 14:00:00+00'),
  (42, '2018-06-27 14:00:00+00'),
  (43, '2018-06-27 18:00:00+00'),
  (44, '2018-06-27 18:00:00+00'),
  (45, '2018-06-28 14:00:00+00'),
  (46, '2018-06-28 14:00:00+00'),
  (47, '2018-06-28 18:00:00+00'),
  (48, '2018-06-28 18:00:00+00'),
  (49, '2018-06-30 14:00:00+00'),
  (50, '2018-06-30 18:00:00+00'),
  (51, '2018-07-01 14:00:00+00'),
  (52, '2018-07-01 18:00:00+00'),
  (53, '2018-07-02 14:00:00+00'),
  (54, '2018-07-02 18:00:00+00'),
  (55, '2018-07-03 14:00:00+00'),
  (56, '2018-07-03 18:00:00+00'),
  (57, '2018-07-06 14:00:00+00'),
  (58, '2018-07-06 18:00:00+00'),
  (59, '2018-07-07 14:00:00+00'),
  (60, '2018-07-07 18:00:00+00'),
  (61, '2018-07-10 18:00:00+00'),
  (62, '2018-07-11 18:00:00+00'),
  (63, '2018-07-14 14:00:00+00'),
  (64, '2018-07-15 15:00:00+00');

INSERT INTO Team (id, name, code, appearances, titles, fifaRanking, confederation, groupId) VALUES
  -- Africa
  (1, 'EGYPT', 'egy', 2 , 0, 31, 'AFRICA', 'A'),
  (2, 'MOROCCO', 'mar', 4 , 0, 40, 'AFRICA','B'),
  (3, 'NIGERIA', 'nga', 5 , 0, 50, 'AFRICA', 'D'),
  (4, 'SENEGAL', 'sen', 1 , 0, 23, 'AFRICA', 'H'),
  (5, 'TUNISIA', 'tun', 4 , 0, 27, 'AFRICA', 'G'),
  --Asia
  (6, 'AUSTRALIA', 'aus', 4 , 0, 39, 'ASIA', 'C'),
  (7, 'IR IRAN', 'irn', 4 , 0, 32, 'ASIA','B'),
  (8, 'JAPAN', 'jpn', 5 , 0, 55, 'ASIA', 'H'),
  (9, 'KOREA REPUBLIC', 'kor', 9 , 0, 59, 'ASIA', 'F'),
  (10, 'SAUDI ARABIA', 'ksa', 4 , 0, 63, 'ASIA', 'A'),
  -- Europe
  (11, 'BELGIUM', 'bel', 12 , 0, 5, 'EUROPE', 'G'),
  (12, 'CROATIA', 'cro', 4 , 0, 17, 'EUROPE', 'D'),
  (13, 'DENMARK', 'den', 4 , 0, 12, 'EUROPE', 'C'),
  (14, 'ENGLAND', 'eng', 14 , 1, 15, 'EUROPE', 'G'),
  (15, 'FRANCE', 'fra', 14 , 1, 9, 'EUROPE', 'C'),
  (16, 'GERMANY', 'ger', 18 , 4, 1, 'EUROPE', 'F'),
  (17, 'ICELAND', 'isl', 0 , 0, 22, 'EUROPE', 'D'),
  (18, 'POLAND', 'pol', 7 , 0, 7, 'EUROPE', 'H'),
  (19, 'PORTUGAL', 'por', 6 , 0, 3, 'EUROPE','B'),
  (20, 'RUSSIA', 'rus', 10 , 0, 65, 'EUROPE', 'A'),
  (21, 'SERBIA', 'srb', 11 , 0, 37, 'EUROPE', 'E'),
  (22, 'SPAIN', 'esp', 14 , 1, 6, 'EUROPE','B'),
  (23, 'SWEDEN', 'swe', 11 , 0, 18, 'EUROPE', 'F'),
  (24, 'SWITZERLAND', 'sui', 10 , 0, 8, 'EUROPE', 'E'),
  -- North, Central America and Caribbean
  (25, 'COSTA RICA', 'crc', 4 , 0, 26, 'NORTH_CENTRAL_AMERICA_AND_CARIBBEAN', 'E'),
  (26, 'MEXICO', 'mex', 15 , 0, 16, 'NORTH_CENTRAL_AMERICA_AND_CARIBBEAN', 'F'),
  (27, 'PANAMA', 'pan', 0 , 0, 56, 'NORTH_CENTRAL_AMERICA_AND_CARIBBEAN', 'G'),
  -- South America
  (28, 'ARGENTINA', 'arg', 16 , 2, 4, 'SOUTH_AMERICA', 'D'),
  (29, 'BRAZIL', 'bra', 20 , 5, 2, 'SOUTH_AMERICA', 'E'),
  (30, 'COLOMBIA', 'col', 5 , 0, 13, 'SOUTH_AMERICA', 'H'),
  (31, 'PERU', 'per', 4 , 0, 11, 'SOUTH_AMERICA', 'C'),
  (32, 'URUGUAY', 'uru', 12 , 2, 21, 'SOUTH_AMERICA', 'A');


INSERT INTO GroupMatch (matchId, homeTeam, awayTeam, groupId) VALUES
  (1, 20, 10, 'A'),
  (2, 1, 32, 'A'),
  (3, 2, 7, 'B'),
  (4, 19, 22, 'B'),
  (5, 15, 6, 'C'),
  (6, 28, 17, 'D'),
  (7, 31, 13, 'C'),
  (8, 12, 3, 'D'),
  (9, 25, 21, 'E'),
  (10, 16, 26, 'F'),
  (11, 29, 24, 'E'),
  (12, 23, 9, 'F'),
  (13, 11, 27, 'G'),
  (14, 5, 14, 'G'),
  (15, 30, 8, 'H'),
  (16, 18, 4, 'H'),
  (17, 20, 1, 'A'),
  (18, 19, 2, 'B'),
  (19, 32, 10, 'A'),
  (20, 7, 22, 'B'),
  (21, 13, 6, 'C'),
  (22, 15, 31, 'C'),
  (23, 28, 12, 'D'),
  (24, 29, 25, 'E'),
  (25, 3, 17, 'D'),
  (26, 21, 24, 'E'),
  (27, 11, 5, 'G'),
  (28, 9, 26, 'F'),
  (29, 16, 23, 'F'),
  (30, 14, 27, 'G'),
  (31, 8, 4, 'H'),
  (32, 18, 30, 'H'),
  (33, 32, 20, 'A'),
  (34, 10, 1, 'A'),
  (35, 7, 19, 'B'),
  (36, 22, 2, 'B'),
  (37, 13, 15, 'C'),
  (38, 6, 31, 'C'),
  (39, 3, 28, 'D'),
  (40, 17, 12, 'D'),
  (41, 9, 16, 'F'),
  (42, 26, 23, 'F'),
  (43, 21, 29, 'E'),
  (44, 24, 25, 'E'),
  (45, 8, 18, 'H'),
  (46, 4, 30, 'H'),
  (47, 27, 5, 'G'),
  (48, 14, 11, 'G');

INSERT INTO KnockoutMatch(matchId, matchCode, stageId, homeTeamCode, awayTeamCode) VALUES
  (49, 'ROS1', 'ROUND_OF_16', 'WINNER_GROUP_C', 'RUNNER_UP_GROUP_D'),
  (50, 'ROS2', 'ROUND_OF_16', 'WINNER_GROUP_A', 'RUNNER_UP_GROUP_B' ),
  (51, 'ROS3', 'ROUND_OF_16', 'WINNER_GROUP_B', 'RUNNER_UP_GROUP_A'),
  (52, 'ROS4', 'ROUND_OF_16', 'WINNER_GROUP_D', 'RUNNER_UP_GROUP_C'),
  (53, 'ROS5', 'ROUND_OF_16', 'WINNER_GROUP_E', 'RUNNER_UP_GROUP_F'),
  (54, 'ROS6', 'ROUND_OF_16', 'WINNER_GROUP_G', 'RUNNER_UP_GROUP_H'),
  (55, 'ROS7', 'ROUND_OF_16', 'WINNER_GROUP_F', 'RUNNER_UP_GROUP_E'),
  (56, 'ROS8', 'ROUND_OF_16', 'WINNER_GROUP_H', 'RUNNER_UP_GROUP_G'),
  (57, 'QF1', 'QUARTER_FINAL','WINNER_ROS1','WINNER_ROS2'),
  (58, 'QF2', 'QUARTER_FINAL','WINNER_ROS5','WINNER_ROS6'),
  (59, 'QF3', 'QUARTER_FINAL','WINNER_ROS3','WINNER_ROS4'),
  (60, 'QF4', 'QUARTER_FINAL','WINNER_ROS7','WINNER_ROS8'),
  (61, 'SF1', 'SEMI_FINAL','WINNER_QF1','WINNER_QF2'),
  (62, 'SF2', 'SEMI_FINAL', 'WINNER_QF3','WINNER_QF4'),
  (63, 'TP', 'THIRD_PLACE','LOSER_SF1','LOSER_SF2'),
  (64, 'F', 'FINAL', 'WINNER_SF1','WINNER_SF2');

INSERT INTO Bet (id, desription, matchId, stageId, type) VALUES
  (1, 'Match 1 Result',1,'GROUP', 'MATCH'),
  (2, 'Match 2 Result',2,'GROUP', 'MATCH'),
  (3, 'Match 3 Result',3,'GROUP', 'MATCH'),
  (4, 'Match 4 Result',4,'GROUP', 'MATCH'),
  (5, 'Match 5 Result',5,'GROUP', 'MATCH'),
  (6, 'Match 6 Result',6,'GROUP', 'MATCH'),
  (7, 'Match 7 Result',7,'GROUP', 'MATCH'),
  (8, 'Match 8 Result',8,'GROUP', 'MATCH'),
  (9, 'Match 9 Result',9,'GROUP', 'MATCH'),
  (10, 'Match 10 Result',10,'GROUP', 'MATCH'),
  (11, 'Match 11 Result',11,'GROUP', 'MATCH'),
  (12, 'Match 12 Result',12,'GROUP', 'MATCH'),
  (13, 'Match 13 Result',13,'GROUP', 'MATCH'),
  (14, 'Match 14 Result',14,'GROUP', 'MATCH'),
  (15, 'Match 15 Result',15,'GROUP', 'MATCH'),
  (16, 'Match 16 Result',16,'GROUP', 'MATCH'),
  (17, 'Match 17 Result',17,'GROUP', 'MATCH'),
  (18, 'Match 18 Result',18,'GROUP', 'MATCH'),
  (19, 'Match 19 Result',19,'GROUP', 'MATCH'),
  (20, 'Match 20 Result',20,'GROUP', 'MATCH'),
  (21, 'Match 21 Result',21,'GROUP', 'MATCH'),
  (22, 'Match 22 Result',22,'GROUP', 'MATCH'),
  (23, 'Match 23 Result',23,'GROUP', 'MATCH'),
  (24, 'Match 24 Result',24,'GROUP', 'MATCH'),
  (25, 'Match 25 Result',25,'GROUP', 'MATCH'),
  (26, 'Match 26 Result',26,'GROUP', 'MATCH'),
  (27, 'Match 27 Result',27,'GROUP', 'MATCH'),
  (28, 'Match 28 Result',28,'GROUP', 'MATCH'),
  (29, 'Match 29 Result',29,'GROUP', 'MATCH'),
  (30, 'Match 30 Result',30,'GROUP', 'MATCH'),
  (31, 'Match 31 Result',31,'GROUP', 'MATCH'),
  (32, 'Match 32 Result',32,'GROUP', 'MATCH'),
  (33, 'Match 33 Result',33,'GROUP', 'MATCH'),
  (34, 'Match 34 Result',34,'GROUP', 'MATCH'),
  (35, 'Match 35 Result',35,'GROUP', 'MATCH'),
  (36, 'Match 36 Result',36,'GROUP', 'MATCH'),
  (37, 'Match 37 Result',37,'GROUP', 'MATCH'),
  (38, 'Match 38 Result',38,'GROUP', 'MATCH'),
  (39, 'Match 39 Result',39,'GROUP', 'MATCH'),
  (40, 'Match 40 Result',40,'GROUP', 'MATCH'),
  (41, 'Match 41 Result',41,'GROUP', 'MATCH'),
  (42, 'Match 42 Result',42,'GROUP', 'MATCH'),
  (43, 'Match 43 Result',43,'GROUP', 'MATCH'),
  (44, 'Match 44 Result',44,'GROUP', 'MATCH'),
  (45, 'Match 45 Result',45,'GROUP', 'MATCH'),
  (46, 'Match 46 Result',46,'GROUP', 'MATCH'),
  (47, 'Match 47 Result',47,'GROUP', 'MATCH'),
  (48, 'Match 48 Result',48,'GROUP', 'MATCH'),
  (49, 'ROS1 Match Result',49,'ROS', 'MATCH'),
  (50, 'ROS2 Match Result',50,'ROS', 'MATCH'),
  (51, 'ROS3 Match Result',51,'ROS', 'MATCH'),
  (52, 'ROS4 Match Result',52,'ROS', 'MATCH'),
  (53, 'ROS5 Match Result',53,'ROS', 'MATCH'),
  (54, 'ROS6 Match Result',54,'ROS', 'MATCH'),
  (55, 'ROS7 Match Result',55,'ROS', 'MATCH'),
  (56, 'ROS8 Match Result',56,'ROS', 'MATCH'),
  (57, 'QF1 Match Result',57,'QF', 'MATCH'),
  (58, 'QF2 Match Result',58,'QF', 'MATCH'),
  (59, 'QF3 Match Result',59,'QF', 'MATCH'),
  (60, 'QF4 Match Result',60,'QF', 'MATCH'),
  (61, 'SF1 Match Result',61,'SF', 'MATCH'),
  (62, 'SF2 Match Result',62,'SF', 'MATCH'),
  (63, 'Third Place Match Result',63,'TP', 'MATCH'),
  (64, 'Final Match Result',64,'F', 'MATCH');

INSERT INTO Bet (id, desription, stageId, type) VALUES
  (65, 'Number 1 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (66, 'Number 2 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (67, 'Number 3 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (68, 'Number 4 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (69, 'Number 5 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (70, 'Number 6 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (71, 'Number 7 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (72, 'Number 8 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (73, 'Number 9 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (74, 'Number 10 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (75, 'Number 11 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (76, 'Number 12 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (77, 'Number 13 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (78, 'Number 14 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (79, 'Number 15 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (80, 'Number 16 Qualifier to Round Of 16','ROS', 'QUALIFIER'),
  (81, 'Number 1 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (82, 'Number 2 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (83, 'Number 3 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (84, 'Number 4 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (85, 'Number 5 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (86, 'Number 6 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (87, 'Number 7 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (88, 'Number 8 Qualifier to Quarter final','QF', 'QUALIFIER'),
  (89, 'Number 5 Qualifier to Semi final','SF', 'QUALIFIER'),
  (90, 'Number 6 Qualifier to Semi final','SF', 'QUALIFIER'),
  (91, 'Number 5 Qualifier to Semi final','SF', 'QUALIFIER'),
  (92, 'Number 6 Qualifier to Semi final','SF', 'QUALIFIER'),
  (93, 'Number 1 Qualifier to Final','F', 'QUALIFIER'),
  (94, 'Number 2 Qualifier to Final','F', 'QUALIFIER'),
  (95, 'Third Place','TP', 'QUALIFIER'),
  (96, 'Winner','W', 'QUALIFIER');