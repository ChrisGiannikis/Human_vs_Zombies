INSERT INTO game("description", "name", "nw_lat", "nw_lng", "se_lat", "se_lng", "state") VALUES ('Description of game 1', 'Call of death', 37.98794095614341, 23.72524260539697, 37.98270577130324, 23.75599586296356, 'REGISTRATION');
INSERT INTO game("description", "name", "nw_lat", "nw_lng", "se_lat", "se_lng", "state") VALUES ('Description of game 2', 'Survival of the Undead', 37.98794095614341, 23.72524260539697, 37.98270577130324, 23.75599586296356, 'IN_PROGRESS');
INSERT INTO game("description", "name", "nw_lat", "nw_lng", "se_lat", "se_lng", "state") VALUES ('Description of game 3', 'Zombie Hunter', 37.98794095614341, 23.72524260539697, 37.98270577130324, 23.75599586296356, 'REGISTRATION');
INSERT INTO game("description", "name", "nw_lat", "nw_lng", "se_lat", "se_lng", "state") VALUES ('Description of game 4', 'Zombies vs Humans', 37.98794095614341, 23.72524260539697, 37.98270577130324, 23.75599586296356, 'COMPLETED');

INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('dsfhd', 'Aris', 'Kardasis');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('dfsgsdf', 'Chris', 'Giannikis');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('lshvgs', 'Tasos', 'Kampe');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('kusydgf', 'Christos', 'Vlachos');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('kasudyfg', 'George',  'Marko');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('suygfga', 'Cristiano', 'Ronaldo');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('aksdcvb', 'Lio', 'Messi');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('laIHBAHS', 'Kylian', 'Mbappe');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('wyetiauf', 'Sergio',  'Ramos');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('asdygg', 'Steven', 'Gerrard');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('asdftaysg', 'David', 'Beckham');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('asudtgax', 'John', 'Terry');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('asiodygfa', 'Giannis', 'Antetokounmpo');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('uycgaaua', 'Damian', 'Lillard');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('alydtqs', 'Khris', 'Middleton');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('laysgasjkh', 'Steph', 'Curry');
INSERT INTO app_user("keycloak_id","first_name", "last_name") VALUES ('skdgfakl', 'Warren', 'West');

INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('JSDGA436JK', true, false, 1, 1);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('GSDFHSDH087', true, false, 1, 2);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('GPSJG9554KM', true, false, 2, 3);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('JKGSAG4324', true, false, 2, 4);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('ERHGWEHHH098', true, false, 3, 5);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('SDFNHGSAG796', true, false, 3, 6);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('DSGJHSFJG65', false, true, 1, 7);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('HDSFHSDH654', false, false, 1, 8);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('DFSHDFHSDH65', false, false, 2, 9);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('HDSHSDFH543', false, true, 2, 10);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('SDFHSDFHDSH64', false, true, 3, 11);
INSERT INTO player("bite_code", "human", "patient_zero", "game_id", "user_id") VALUES ('DHSDFHSDFH645', false, false, 3, 12);

INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (37.983, 23.735, null, 7, 8);
INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (37.986, 23.73, null, 8, 2);
INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (37.986, 23.73, null, 10, 9);
INSERT INTO kill("lat", "lng", "time_of_death", "killer_id", "victim_id") VALUES (37.986, 23.73, null, 11, 12);

INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "lat", "lng", "start_time", "game_id") VALUES ('The human team has received a distress call from a group of survivors trapped in a zombie-infested area.', null, true, false, 'Rescue Mission', 37.984, 23.75, null, 1);
INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "lat", "lng", "start_time", "game_id") VALUES ('The human team is low on resources and must venture into a zombie-infested area to gather supplies. ', null, true, false, 'Supply Run', 37.986, 23.73, null, 2);
INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "lat", "lng", "start_time", "game_id") VALUES ('The human team has developed a cure for the zombie virus and needs to deliver it to a research facility to be mass-produced.', null, true, true, 'Cure Delivery', 37.986, 23.73, null, 1);
INSERT INTO mission("description", "end_time", "human_visible", "zombie_visible", "name", "lat", "lng", "start_time", "game_id") VALUES ('The zombie team has identified a key human target and is moving in to eliminate them.', null, false, true, 'Elimination', 37.986, 23.73, null, 3);

INSERT INTO squad("human", "name", "game_id") VALUES (true, 'Unbeatables', 1);
INSERT INTO squad("human", "name", "game_id") VALUES (false, 'Suicide Squad', 1);
INSERT INTO squad("human", "name", "game_id") VALUES (true, 'Peaky Blinders', 2);
INSERT INTO squad("human", "name", "game_id") VALUES (false, 'Expendables', 3);

INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 1, 1);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('HIGH', 2, 1);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 8, 2);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('NOOB', 7, 2);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 3, 3);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('INTERMEDIATE', 4, 3);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('LEADER', 12, 4);
INSERT INTO squad_member("rank", "player_id", "squad_id") VALUES ('NOOB', 11, 4);

INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 37.986, 23.73, null, 1);
INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 37.986, 23.73, null, 3);
INSERT INTO squad_check_in("end_time", "lat", "lng", "start_time", "squad_member_id") VALUES (null, 37.986, 23.73, null, 8);

INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('GLOBAL', 'Message to All by Human', 1, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('FACTION', 'Message to Humans', 5, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('SQUAD', 'Message to Human Squad', 1, 1);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('GLOBAL', 'Message to All by Zombie', 7, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('FACTION', 'Message to Zombies', 9, null);
INSERT INTO chat("chat_scope", "message", "player_id", "squad_id") VALUES('SQUAD', 'Message to Zombie Squad', 7, 2);