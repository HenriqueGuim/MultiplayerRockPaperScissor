package academy.mindswap;

public class Game implements Runnable {
    private PlayerHandler player1;
    private PlayerHandler player2;
    private String player1Name;
    private String player2Name;
    private int player1Points = 0;
    private int player2Points = 0;
    private Server server;


    private final int WINING_OBJECTIVE = 3;

    public Game(PlayerHandler[] playerArray, Server server) {
        this.server = server;
        player1 = playerArray[0];
        player2 = playerArray[1];
    }

    private void welcomePlayers() {
        player1.sendMessage("Opponent Found! Let's start.");
        player2.sendMessage("Opponent Found! Let's start.");
        player1Name = player1.getPlayerName();
        player2Name = player2.getPlayerName();
    }

    private void gameProcess() {


        while (player1Points != WINING_OBJECTIVE && player2Points != WINING_OBJECTIVE) {
            sendMessageToPlayers("Please Choose your move");
            MoveType player1move = receiveMove(player1);
            MoveType player2move = receiveMove(player2);
            roundWinner(player1move,player2move);
        }


    }

    private void roundWinner(MoveType player1move, MoveType player2move) {
        if(player1move == player2move){
            sendMessageToPlayers("It's a draw no one get points");
            return;
        }
        if (player1move == MoveType.PAPER && player2move == MoveType.ROCK){
            sendMessageToPlayers(player1Name + " won this round");
            player1Points++;
            return;
        }
        if (player1move == MoveType.SCISSOR && player2move == MoveType.PAPER){
            sendMessageToPlayers(player1Name + " won this round");
            player1Points++;
            return;
        }
        if (player1move == MoveType.ROCK && player2move == MoveType.SCISSOR){
            sendMessageToPlayers(player1Name + " won this round");
            player1Points++;
            return;
        }
        sendMessageToPlayers(player2Name + " won this round");
        player2Points++;
    }

    @Override
    public void run() {
        welcomePlayers();
        gameProcess();
        announceWinner();
        checkWantsPlay();

    }

    private void checkWantsPlay() {
        sendMessageToPlayers("Want to continue playing?");
        player1Points=0;
        player2Points=0;
        endGameResponse(player1);
        endGameResponse(player2);
    }

    private void endGameResponse(PlayerHandler player) {
        String response = player.receiveMessage();
        response.toLowerCase();
        switch (response){
            case "yes": player.sendMessage("Wait while we search for a new opponent");
                        player.endGame();
                        server.findPlayer();
            return;
            case "no": player.sendMessage("Thank you for playing with us.");
                        player.closeSocket();
                        return;
            default: player.sendMessage("Please insert a valid message: Yes or No");
                endGameResponse(player);
        }
    }

    private void announceWinner() {
        if(player1Points > player2Points){
            sendMessageToPlayers(player1Name + " Won the Game");
            return;
        }
        sendMessageToPlayers(player2Name + " Won the Game");
    }

    private void sendMessageToPlayers(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    private MoveType receiveMove(PlayerHandler player) {
        String move = player.receiveMessage();
        move.toLowerCase();

        switch (move) {
            case "rock":
                return MoveType.ROCK;
            case "scissor":
                return MoveType.SCISSOR;
            case "paper":
                return MoveType.PAPER;
            default:
                player.sendMessage("Please insert a valid move");
                return receiveMove(player);

        }
    }
}
