package academy.mindswap;

import java.io.*;
import java.net.Socket;

public class Player {
    Socket socket;
    BufferedReader serverReader;
    BufferedReader consoleReader;
    BufferedWriter serverWriter;

    public static void main(String[] args) {
        Player player = new Player();
        player.setConsoleReader();
        player.handleServer();

    }



    private void handleServer(){
        setServer();
        createServerComms();
        ServerListner serverListner = new ServerListner();
        new Thread(serverListner).start();
        serverWriter();
        close();
    }

    private void serverWriter() {
        try {
            serverWriter.write(consoleReader.readLine());
            serverWriter.newLine();
            serverWriter.flush();
            serverWriter();
        } catch (IOException e) {
            return;
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void createServerComms() {
        try {
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setServer() {
        String hostName;
        int portNumber;
        try {
            System.out.println("Please indicate the hostName of the server.");
            hostName = consoleReader.readLine();
            System.out.println("Please indicate the port number.");
            portNumber = Integer.parseInt(consoleReader.readLine());

            socket = new Socket(hostName,portNumber);
        } catch (IOException e) {
            System.out.println("Seems like the server is down. Try another one!");
            setServer();
        }
    }

    private void setConsoleReader() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private class ServerListner implements Runnable{
        private void listenServer() {
            try {
                String message = serverReader.readLine();
                if (message == null){
                    serverWriter.close();
                    return;
                }
                System.out.println(message);
                listenServer();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        @Override
        public void run() {
            listenServer();
        }
    }
}
