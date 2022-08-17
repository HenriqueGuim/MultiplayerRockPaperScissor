package academy.mindswap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<PlayerHandler> playerList;


    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(8080);

    }



    private void startServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            playerList = new ArrayList<PlayerHandler>();
            System.out.println("Server is Running");

            AcceptPlayer acceptPlayer = new AcceptPlayer();
            new Thread(acceptPlayer).start();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


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







    private class AcceptPlayer implements Runnable {
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

        @Override
        public void run() {
            acceptPlayer();
        }
    }
}
