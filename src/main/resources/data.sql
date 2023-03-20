INSERT INTO game("description", "name", "nw_lat", "nw_lng", "rules", "se_lat", "se_lng", "state") VALUES ('Description of game 1', 'Name Game1', 32.4, 24.5, 'Rules of Game 1', 54.3, 12.3, 'REGISTRATION');
INSERT INTO game("description", "name", "nw_lat", "nw_lng", "rules", "se_lat", "se_lng", "state") VALUES ('Description of game 2', 'Name Game2', 31.4, 64.5, 'Rules of Game 2', 44.2, 13.3, 'IN_PROGRESS');
INSERT INTO game("description", "name", "nw_lat", "nw_lng", "rules", "se_lat", "se_lng", "state") VALUES ('Description of game 3', 'Name Game3', 89.4, 53.5, 'Rules of Game 3', 14.3, 102.3, 'COMPLETED');

INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Aris', false, 'Kardasis');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Chris', false, 'Giannikis');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Tasos', false, 'Kampe');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Christos', false, 'Vlachos');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('George', false, 'Marko');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Cristiano', false, 'Ronaldo');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Lio', false, 'Messi');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Kylian', false, 'Mbappe');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Sergio', false, 'Ramos');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Steven', false, 'Gerrard');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('David', false, 'Beckham');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('John', false, 'Terry');
INSERT INTO app_user("first_name", "is_administrator", "last_name") VALUES ('Warren', true, 'West');

INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('JSDGA436JK', true, false, 1, 1);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('GSDFHSDH087', true, false, 1, 2);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('GPSJG9554KM', true, false, 2, 3);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('JKGSAG4324', true, false, 2, 4);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('ERHGWEHHH098', true, false, 3, 5);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('SDFNHGSAG796', true, false, 3, 6);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('DSGJHSFJG65', false, true, 1, 7);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('HDSFHSDH654', false, false, 1, 8);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('DFSHDFHSDH65', false, false, 2, 9);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('HDSHSDFH543', false, true, 2, 10);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('SDFHSDFHDSH64', false, true, 3, 11);
INSERT INTO player("bite_code", "is_human", "is_patient_zero", "game_id", "user_id") VALUES ('DHSDFHSDFH645', false, false, 3, 12);

INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (124.4, 14.5, null, 7, 1);
INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (154.4, 24.5, null, 10, 4);
INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (144.4, 54.5, null, 11, 6);

INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "start_time", "game_id") VALUES ('Mission 1 description', null, true, false, 'Mission 1 name', null, 1);
INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "start_time", "game_id") VALUES ('Mission 2 description', null, false, true, 'Mission 2 name', null, 2);
INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "start_time", "game_id") VALUES ('Mission 3 description', null, true, true, 'Mission 3 name', null, 3);

INSERT INTO squad("is_human", "name", "game_id") VALUES (true, 'Squad 1 name', 1);
INSERT INTO squad("is_human", "name", "game_id") VALUES (false, 'Squad 2 name', 1);
INSERT INTO squad("is_human", "name", "game_id") VALUES (true, 'Squad 3 name', 2);
INSERT INTO squad("is_human", "name", "game_id") VALUES (false, 'Squad 4 name', 3);

INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 1, 1);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('HIGH', 2, 1);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 8, 2);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('NOOB', 7, 2);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 3, 3);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('INTERMEDIATE', 4, 3);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 12, 4);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('NOOB', 11, 4);

INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 23.6, 234.5, null, 1);
INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 65.6, 12.5, null, 3);
INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 87.6, 54.5, null, 8);

INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('GLOBAL', 'Message to All by Human', 1, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('FACTION', 'Message to Humans', 5, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('SQUAD', 'Message to Human Squad', 1, 1);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('GLOBAL', 'Message to All by Zombie', 7, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('FACTION', 'Message to Zombies', 9, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('SQUAD', 'Message to Zombie Squad', 7, 2);