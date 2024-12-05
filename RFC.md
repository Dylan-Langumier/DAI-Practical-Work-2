# Define an application protocol

## BattleShip

### Overview
This protocol supports a simple multiplayer only Battleship game over the network. The server knows both player's boards, but each player only stores his own.
To keep things simple, everything is a dialogue.


### Transport protocol
We are going to be using TCP over IP, sending text information.
Port 6433 is going to be used by default but it can be configured at launch. Messages separated by end of line characters.
the clients initiates and close the communication.

### Messages
- JOIN:\<name>
- PLACE:\<shiptype>:\<x>:\<y>:\<Orientation>
- WAIT
- GAME_STARTED:\<adversary name>
- PLAY:\<x>:\<y>
- FEEDBACK:\<HIT/MISS>
- GAME_OVER:\<WIN/LOSE>
- ERROR:\<message>

### Example

Client starts asks the user it's name and sends  
JOIN:Zoiberg

Server then asks the client to place first ship with  
PLACE:DESTROYER

Client replies  
PLACE:DESTROYER:G:5:TOP

And so on until all ships are placed. Then server sends  
WAIT

When a second player is ready server sends  
START_GAME:TheProfessor

When a player needs to play server sends  
PLAY:A:5  

And player replies with it's move  
PLAY:J-7

And server gives feedback   
FEEDBACK:HIT 

... ...  

 At the end of the game, or when a player disconnected, server sends  
 GAME_OVER:WIN  