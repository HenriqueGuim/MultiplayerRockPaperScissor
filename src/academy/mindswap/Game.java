package academy.mindswap;

public class Game implements Runnable{
    private PlayerHandler player1;
    private PlayerHandler player2;
    private String player1Name;
    private String player2Name;

    private final int WINING_OBJECTIVE = 3;
    public Game(PlayerHandler[] playerArray) {
        player1 = playerArray[0];
        player2 = playerArray[1];
    }

    private void welcomePlayers(){
        player1.sendMessage("Opponent Found! Let's start.");
        player2.sendMessage("Opponent Found! Let's start.");
    }

    private void gameProcess(){
        int player1Points= 0;
        int player2Points= 0;

        while (player1Points != WINING_OBJECTIVE || player2Points != WINING_OBJECTIVE){

        }


    }

    @Override
    public void run() {
        welcomePlayers();
        gameProcess();
        //announceWinner();
        //freePlayers();

    }
}
