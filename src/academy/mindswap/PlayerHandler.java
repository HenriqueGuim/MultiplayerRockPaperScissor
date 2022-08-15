package academy.mindswap;

import java.io.*;
import java.net.Socket;

public class PlayerHandler  {
    private Socket playerSocket;
    BufferedReader reader;
    BufferedWriter writer;
    boolean isPlaying = false;
    String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerHandler(Socket clientSocket) {
        playerSocket = clientSocket;
        startBuffers();
    }

    public boolean isOffline(){
        if (playerSocket == null){
            return true;
        }
        return playerSocket.isClosed();
    }
    private void startBuffers(){
        try {
            reader = new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String receiveMessage(){
        String message;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void startGame(){
        isPlaying = true;
    }
    public void endGame(){
        isPlaying = false;
    }
}
