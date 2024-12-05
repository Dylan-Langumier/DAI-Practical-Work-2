# DAI Practical work 2: Multiplayer Battleship

## Description 
Simple and classic Battleship game playable by two people over the network.
An application protocol will be created and as well as both client and server applications.  
It will be entierly implemented in java and released as a docker container.

## Install with docker
Instant
```bash
docker run --rm ghcr.io/rp2709/bam <arguments>
```

## Server
Waits for two players (clients) to connect and starts a game between them.  

## Client
Connects to a server and communicates player's actions. It also displays the game and allows the player to input their moves.

## Packaging the app with docker
See [package with docker](./docker.md)