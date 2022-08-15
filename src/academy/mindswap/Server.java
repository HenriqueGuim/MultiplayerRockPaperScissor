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

            FindPlayer findPlayer = new FindPlayer();
            new Thread(findPlayer).start();

            AcceptPlayer acceptPlayer = new AcceptPlayer();
            new Thread(acceptPlayer).start();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private class FindPlayer implements Runnable {
        private void findPlayer() {
            PlayerHandler[] playerArray = new PlayerHandler[2];
            int playerCounter = 0;

                while (playerArray[1] == null){
                for (PlayerHandler player : playerList) {
                    if (!player.isOffline() && player != playerArray[0] && !player.isPlaying) {
                        playerArray[playerCounter] = player;
                        playerCounter++;
                    }
                }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("starting a new game");
                Game game = new Game(playerArray);
                game.run();
                Arrays.stream(playerArray).forEach(PlayerHandler::startGame);
                findPlayer();

        }

        @Override
        public void run() {
            findPlayer();
        }
    }




    private class AcceptPlayer implements Runnable {
        private void acceptPlayer() {
            try {
                System.out.println("Waiting for new players.");
                Socket clientSocket = serverSocket.accept();

                PlayerHandler playerHandler = new PlayerHandler(clientSocket);
                playerList.add(playerHandler);

                playerHandler.sendMessage("Welcome to our game server. Please indicate your name");

                //playerHandler.setPlayerName(playerHandler.receiveMessage());


                System.out.println("A new player arrived");
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
