---
marp: true
---
<!--
theme: gaia
size: 16:9
paginate: true
author: 'Dylan Langumier and Raphael Perret' 
title: 'BAM'
description: 'Battleship Added Multiplayer'
url: 
footer: '**HEIG-VD** - DAI Course 2024-2025'
style: |
    :root {
        --color-background: #fff;
        --color-foreground: #333;
        --color-highlight: #f96;
        --color-dimmed: #888;
        --color-headings: #7d8ca3;
    }
    blockquote {
        font-style: italic;
    }
    table {
        width: 100%;
    }
    th:first-child {
        width: 15%;
    }
    h1, h2, h3, h4, h5, h6 {
        color: var(--color-headings);
    }
    h2, h3, h4, h5, h6 {
        font-size: 1.5rem;
    }
    h1 a:link, h2 a:link, h3 a:link, h4 a:link, h5 a:link, h6 a:link {
        text-decoration: none;
    }
    section:not([class=lead]) > p, blockquote {
        text-align: justify;
    }
headingDivider: 4
-->

# Presentation of DAI project : BAM
<!--
_class: lead
_paginate: false
-->

[fig1]:Images/Hit_and_reply.png
[fig2]:Images/Guess_and_Hit.png
[fig3]:Images/start.png

Dylan Langumier & Raphael Perret

## Our project

Simple multiplayer only command line Battleship game

![bg right:40%][fig1]

## Usage

**Server**
```bash
java -jar target/Battleship-1.0.0.jar server (--port <port>) (--max_games <maxGames>)
```

**Client**
```bash
java -jar target/Battleship-1.0.0.jar client --host <host> (--port <port>)
```

## Example 1
Start server using default port
```bash
java -jar target/Battleship-1.0.0.jar server
[SERVER] : Starting
[SERVER] : Listening on port 6433, Allowing up to 10 simultaneous games
```

Start client with server's address and defaul port
```bash
java -jar target/Battleship-1.0.0.jar client --host localhost
Trying to connect to host localhost at port 6433
[Client] : starting
Choose a name
```

## Example 1 
On the server console we get
```bash
[Player@/127.0.0.1] : Connected
```
The player can now follow instructions first choosing a name, then placing he/her's ships. After waiting for another player the game starts.

## Code
**Dependencies**
- [picocli](https://picocli.info/)
- [java io](https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html)

![bg right:60%][fig2]

## Code
- commands : picocli stuff
- client : mostly the network client and protocole messages to be received
- server : server network code
- gameclasses : only game/protocol related code

![bg right:30%][fig3]