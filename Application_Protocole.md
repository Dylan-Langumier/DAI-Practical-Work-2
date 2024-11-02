# Define an application protocol

## BattleShip

### Overview
This protocol supports a simple multiplayer only Battleship game over the network. The server will be handling each player's actual boards.
It will therefor send the board to the player for the client to render. However, the "estimation" of the opponent's board is handled by  the client.



### Transport protocol
We are going to be using TCP over IP, sending text information.
Port 42069 is going to be used by default but it can be configured at launch. Messages separated by end of line characters.
the clients initiates and close the communication.

### Messages
REQUEST_GAME:\<board>  
OK   
GAME_STARTED  
YOUR_TURN:\<board>  
PLAY:\<position>  
ERROR:\<message>  
FEEDBACK:\<hit/miss>  
GAME_OVER:\<result>  
ABANDON  
QUIT

### Examples

Player one requests a game to the server with it's starting positions  
REQUEST_GAME:  
0100000000,  
0100000000,  
0100000000,  
0022220000,     
0000000000,  
4000030000,  
4000030000,  
4000000000,  
4000000000,  
4000000000\n

Server approves  
OK

Another player requests a game  
REQUEST_GAME:  
1110000000,  
0000000020,  
0000000020,  
0000000020,     
0000000020,  
0300000000,  
0300000000,  
0000000000,  
0000444440,  
0000000000\n

Server tells both clients their game has started   
GAME_STARTED

Server tells the first player to play  
YOUR_TURN:player's board

Player one plays  
PLAY:A,10

Server approves and asks player 2 to play  
FEEDBACK:miss  
YOUR_TURN:player two board  

Player two plays  
PLAY:E,5

Same again  
FEEDBACK:miss  
YOUR_TURN:player one board  

Player one  
PLAY:A,10

Server refuses operation and asks again  
ERROR:Space already attacked  
YOUR_TURN:player one board  

... ...