# Simple Chess Server

This project is the take home assignment to sensys gatso. 
It is a simple server to play chess. All the pieces has its moves according to the chess rules, see more at https://en.wikipedia.org/wiki/Chess
Only CHECK and CHECKMATE are implemented, other rules are not done yet. E.g. when a Pawn reach out the end of board it may be changed to another piece chosen by the player.


Only 1 game and 2 players are allowed to play the game. The game make turns between black and with pieces. it's available as single instance, meaning you could use 2 computers/browsers but the game has no knowledge about it. The players are responsible to take turns

## Development
This Project uses spring-boot 3.0.0 and Java 17, so you must have a Java JDK 17+ installed in your machine, in order to build and run the application

A maven wrapper is also provided along with the project, so if you do not have the maven 3.8.6 it will be downloaded and installed on the first run of the command `./mvnw`

By Default the application is set with log level INFO, if you want to see more detailed information you can change the log level at `application.yaml` file. When in DEBUG level, after every move the application will print a board in the logs 

## API
A REST API is available to interact with game. A single url is available to interact with the game and both players use the same URL.
 

### GET /api/status
Get the game status

#### Response
- 200: Return a JSON object the games status
```
{
  "whiteStatus": "NORMAL",
  "blackStatus": "NORMAL",
  "turn": "WHITE",
  "deleted": []
}
```

### GET /api/pieces
Return all available pieces

#### Response
- 200: JSON array with all available the pieces in the board
```
[
    {
        "id": "A1",
        "row": 7,
        "column": 0,
        "color": "WHITE",
        "type": "ROOK"
    },
    {
        "id": "B1",
        "row": 7,
        "column": 1,
        "color": "WHITE",
        "type": "KNIGHT"
    }
]
```

### GET /api/pieces/{ID}
Retrieve piece detail

#### Response
- 200: Return JSON object with piece details. Check out the allowedMoves property, it provides where this piece is allowed to go according to its rules
- 400: if the ID is in wrong format. ID always has 2 chars. E.g. D2
- 404: when square is empty or out of bounds
```
{
    "id": "D2",
    "row": 6,
    "column": 3,
    "color": "WHITE",
    "type": "PAWN",
    "allowedMoves": [
        {
            "id": "D4",
            "row": 4,
            "column": 3
        },
        {
            "id": "D3",
            "row": 5,
            "column": 3
        }
    ]
}
```

### POST /api/moves
Executes a move

#### Request
The properties `from` and `to` receive the point ID

```
{"from": "A2", "to": "A3"}
```

#### Response
- 200: move done, return JSON object with piece.
- 400: if the ID is in wrong format or player tries to execute a invalid move
- 404: when square is empty or out of bounds
- 409: when a player tries to change his opponent piece
```
{
    "id": "A3",
    "row": 5,
    "column": 0,
    "color": "WHITE",
    "type": "PAWN"
}
```

### DELETE /api/game
Reset a game
- 204: game reseted

## Docker
A Dockerfile is provided in the root of the project. You can build a docker image even if you DO NOT have java 17 installed. I built the image in MAC M1, as it is a different platform I'm not sure if it works in intel. No extra argument is needed to run the image
```
docker run -p 8080:8080 {image-id}
```

## Frontend implementation suggestion
- GET /api/pieces and mount the board using the row,column,type and color make sure you keep track square ID. IDs are just pointer to the squares as a regular board
- GET /api/status and get the `turn` to allow player to play
- allow the player click on his piece, get the ID and GET /api/pieces/{ID}. This will give you all the moves allowed and the squares could be highlighted. Keep track of the clicked ID
- when user releases the piece get the square ID and POST /api/moves providing `from` and `to`
- GET /api/status and get the `turn` to allow player to play and the game status
- repeat the last 3 steps until game reaches checkmate
- after game is finished DELETE /api/game and start all over again

