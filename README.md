# Humans vs. Zombies

### Team Members
  Backend Team
* Anastasios Kamperopoulos
* Christos Giannikis
* Aris Kardasis
  Frontend Team
* George Markozanis
* Christos Vlachos

## Installation Instructions
To run the HvZ backend
To run the HvZ backend project locally:
* Clone repo:
    * with https: git clone https://github.com/ChrisGiannikis/Human_vs_Zombies.git
    * with ssh: git clone git@github.com:ChrisGiannikis/Human_vs_Zombies.git
* Open the project in an IDE, like intelliJ
* Run the  file HumanVsZombiesApplication

## Project Explanation

Humans vs. Zombies (HvZ) is a game of tag played at schools, camps, neighborhoods, libraries, and conventions around the world. The game simulates the exponential spread of a fictional zombie infection through a population.  
The game is played as follows:
* The game begins with one or more “Original Zombies” (OZ) or patient zero.
The purpose of the OZ is to infect human players by tagging them. Once tagged,
a human becomes a zombie for the remainder of the game.
* Human players are able to defend themselves against the zombie horde using Nerf
weapons and clean, rolled-up socks which may be thrown to stun an unsuspecting
zombie.
* Many variants of the game rules exist which introduce additional rules and activities into the game; for example, the Rhodes University variant introduces a
web-based game map where markers appear showing the location of missions and
supplies. The appearance of supplies forces humans to leave their safe zones and
risk being turned.  

While HvZ is a highly physical and active game, the administrators would have difficulty communicating with all of the players and keeping track of the game and individual player states without help. For this case, you are tasked with designing and implementing such a system that will enable players to manage their own state in accordance with the rules of the game and leave the administrators free to focus on improving the game itself.  

For this case, candidates are expected to design and implement a software solution for managing the state and communication of one or more concurrent games of HvZ.  
The main components of the system are as follows:
* A static, single-page, frontend using a modern web framework.
* A RESTful API service, through which the frontend may interact with the
database.
* A suitable database.





### API Documentation
##  Game endpoints
GET:
```
/games                                              Returns a list of all games.
/games/<game_id>                                    Returns a specific game.
/games/<game_id>/chats                              Returns a list of chat messages in a specific game.
```

POST:
```
/games                                              Creates a new game.
/games/<game_id>/chats                              Send a new chat message.
```
PUT:
```
/games/<game_id>                                    Updates a game. Admin only.
```

DELETE:
```
/games/<game_id>                                    Deletes a game. Admin only.
```

#### Player endpoints
GET:
```
/games/<game_id>/players                            Get a list of all players.
/games/<game_id>/players/<player_id>               Returns a specific player object.
```
POST:
```
/games/<game_id>/players                            Registers a user for a game. 
```

PUT:
```
/games/<game_id>/players/<player_id>                Updates a player object Admin only.
```
DELETE:
```
/games/<game_id>/players/<player_id>                Deletes a player. Admin only.
```

#### Kill endpoints
GET:
```
/games/<game_id>/kills                              Get a list of all kills.
/games/<game_id>/kills/<kill_id>                    Returns a specific kill object.
```

POST:
```
/games/<game_id>/kills                              Creates a kill by looking up the victim by the spicified bite code.
```

PUT:
```
/games/<game_id>/kills/<kill_id>                    Updates a kill. Killer and admin only.
```

DELETE:
```
/games/<game_id>/kills/<kill_id>                    Deletes a kill. admin only.
```

#### Squad endpoins
GET:
```
/games/<game_id>/squads                             Get a list of all squads.
/games/<game_id>/squads/<squad_id>                  Returns a specific squad.
/games/<game_id>/squads/<squad_id>/chats            Returns a list of all chat messages.
/games/<game_id>/squads/<squad_id>/checkins         Get a list of squad checkin markers.
```

POST:
```
/games/<game_id>/squads                             Creates a squad.
/games/<game_id>/squads/<squad_id>  /join           Creates a squad member.
/games/<game_id>/squads/<squad_id>/chats            Send a new chat message addressed to a particular squad.
/games/<game_id>/squads/<squad_id>/checkins         Creates a squad checkin.
```

PUT:
```
/games/<game_id>/squads/<squad_id>                  Updates a squad. Admin only.
```

DELETE:
```
/games/<game_id>/squads/<squad_id>                  Deletes a squad. Admin only.
```

#### Mission endpoins
```
GET:
/games/<game_id>/missions                           Get a list of all missions.
/games/<game_id>/missions/<mission_id>              Returns a specific mission.
```

POST:
```
/games/<game_id>/missions                           Creates a mission.
```

PUT:
```
/games/<game_id>/missions/<missions_id>             Updates a mision. Admin only.
```

DELETE:
```
/games/<game_id>/missions/<mission_id>              Deletes a mission. Admin only.
```

***FURTHER DOCUMENTATION CAN BE FOUND INSIDE CONTROLLER CLASSES***

### Tools
* Java
* IntelliJ for coding
* pgAdmin for handling the database
* Postgres SQL
* Postman for API testing 
* Docker for container
* Azure for deployment
* Swagger for API documentation



