package academy.mindswap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//create Javadoc to all classes and methods
/**
 * This class is the main class of the server. It creates a server socket and
 * listens for clients. When a client connects, a new thread is created to handle
 * the client.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Server {
    private ServerSocket serverSocket;
    private List<PlayerHandler> playerList;

    

    /**
     * Create a server.
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(8080);

    }


    /**
     * Start the server.
     * @param portNumber The port number to use.
     * If the port is in use, an exception is thrown.
     * If the port is not in use, the server is started.
     * Is created an arraylist of playerhandlers to keep track of all the players in the server and to keep track of the players that are playing.
     *
     */

    private void startServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            playerList = new ArrayList<PlayerHandler>();
            System.out.println("Server is Running");

            acceptPlayer();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * finds two players that are online and starts the game.
     *
     */

        public void findPlayer() {
            PlayerHandler[] playerArray = new PlayerHandler[2];
            int playerCounter = 0;


                for (PlayerHandler player : playerList) {
                    if (!player.isOffline() && player != playerArray[0] && !player.isPlaying()) {
                        playerArray[playerCounter] = player;
                        playerCounter++;
                    }
                }

                if(playerArray[1] != null) {
                    System.out.println("starting a new game");
                    Game game = new Game(playerArray, this);
                    new Thread(game).start();
                    Arrays.stream(playerArray).forEach(PlayerHandler::startGame);
                }

        }

        /**
         * Accepts a player and adds it to the playerlist. 
         *
         */
    private void acceptPlayer() {
        try {
            System.out.println("Waiting for new players.");
            Socket clientSocket = serverSocket.accept();

            PlayerHandler playerHandler = new PlayerHandler(clientSocket);

            playerHandler.sendMessage("Please indicate your name");

            playerHandler.setPlayerName(playerHandler.receiveMessage());

            playerHandler.sendMessage("Be welcome " + playerHandler.getPlayerName());
            playerHandler.sendMessage("Please wait for another player");

            playerList.add(playerHandler);
            System.out.println("A new player arrived");
            findPlayer();
            acceptPlayer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
