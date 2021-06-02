# GoClient
This projects implements the game of Go for two people in a network. It uses multithreading and the server-client model, thus two players can connect to the game, and the first one to do so also turns on the server in a separate thread. The players can choose the size of the board(7x7, 9x9, 13x13 and 19x19) and the colour they want, but the one to complete these fields will be the first player, and the second one will wait for him. Then they proceed to play a normal game of Go, and when one of the players decides that he has no other moves they can resign and the game ends. For the GUI I used Java.Swing. In the future I want to resolve some bugs (as there seems to be some problems with the syncronizations), I would like to add an option to see all the previous moves and an analytics frame at the end. Also I want to allow a client to play with a bot from an engine like KataGo.

## How to use
1. Build and run the program twice for the two players
2. Write your name and if you were the first to connect choose game settings
3. Begin the game: place the piece where you want and when you find a suitable place click *Submit move*. (To learn how to play ypou can visit this website: https://online-go.com/learn-to-play-go )
4. Wait for your opponent to move and so on
5. If you don't have any other move click on *Resign*

## Project Structure and Technologies
- **Server package** : contains all the packages and classes for the server part of the project. It creates new threads for each connected client, and oversees comunication between them. 
  1. *Game* class is a voaltile, shared class between the players' threads through which they share informations about the game state.
  2. *GameLogic* class impelments the idea of the game, checks the validity of the moves and sees if any chain has been captured. I uses the syncronized methods (placePiece and getPermissionToMove) to make sure that each player moves orderly.
  3. *GoClientThread* is the class that implements the thread that comunicates directly with the client, and then makes Game and GameLogic compute the information
- **Client package** : contains the *gui* package and implements the comunication between the user and the server through the interface, made with Swing.
  1. *newgame* package makes the first frame that the user sees, and the settings one. It uses the JLayer Component to add the background and other components like JPanel, JLabel, JButton to create the interface.
  2. *game* package implements the game itself. It creates the board, *GoBoard* class, where I check, within the posibilities of the client, for valid moves, and which I redraw after every move. It also has the *GoInfo* class that shows informations about the players, like name, colour and how many pieces they captures, and also contains the *Submit Move* button which communicates to the server move and acts accordingly to the answer, and the *Resign* JButton which ends the game and oppens the *ScoreFrame*
  3. *score* package contains the *ScoreFrame* on which the winner is displayed.
  4. *GoClient* class opens the socket between the server and the client.

### Creator
Valeria Izvoreanu, 2B4
