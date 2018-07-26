package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/** Provides vars and methods for operating game itself - conclusion, list of winner etc. */
public class Game {

    private int simulatedPlayers;
    private String playerName;
    private int deckCount;

    private Player player[];

    volatile private int playersServed;

    private String winners[];

    private CardGenerator cardGenerator;

    private TableController tableController;


    /** Sets up game settings based on passed vars */
    Game(int simulatedPlayers, String playerName, TableController tableController) {

        this.tableController = tableController;

        this.playersServed = 1 + simulatedPlayers;

        this.deckCount = 5;
        this.simulatedPlayers = simulatedPlayers;
        this.playerName = playerName;

        this.player = new Player[4];
        for (int i = 0; i < 2 + simulatedPlayers; i++) player[i] = new Player(i, tableController);


        winners = new String[3];
        for (int i = 0; i < 1 + simulatedPlayers; i++) winners[i] = new String();


        this.cardGenerator = new CardGenerator(this.deckCount);

        this.player[1].start();

        if (player[2] != null) {
            this.player[2].start();
        }
        if (player[3] != null) {
            this.player[3].start();
        }

    }


    public int getScore(int index) { //do kasacji

        return player[index].getScore();

    }

    public int getCurrentSlot(int index) {


        return player[index].getCurrentCardSlot();
    }

    /** Sets backup image (handling overflow of displayed cards) */
    public void setBackupImg(int index, Image img) {

        player[index].setBackupImg(img);

    }

    public Image getBackupImg(int index) {

        return player[index].getBackupImg();

    }

    /** Method called by players when they stand or exceed 21 points */
    public void backOff() { // Zapobieganie zablokowania gry przez dodatkowych graczy
        synchronized (this) {
            this.playersServed--;
        }

    }

    public Player getPlayer(int index) {
        return this.player[index];
    }

    /** evaluate if player've won the game */
    boolean evaluateGame() {
        int tempIndex = 0;
        String tempName = "";
        boolean playerWon = false;
        for (int i = 0; i < 2 + simulatedPlayers; i++) {
            switch (i) {
                case 0: {
                    tempName = this.playerName;
                    break;
                }
                case 2: {
                    tempName = "Gracz 2";
                    break;
                }
                case 3: {
                    tempName = "Gracz 3";
                    break;
                }
            }
            if (isCroupier(i)){continue;} // krupiera nie sprawdzamy
            else if (
                    ((this.getScore(i) >= this.getScore(1)) && (this.getScore(i) <= 21))
                            || ((this.getScore(1) > 21 && (this.getScore(i)) <= 21))
                    ) {


                System.out.println(tempIndex);


                this.winners[tempIndex++] = tempName;

                if (i == 0) playerWon = true;
            }
        }

        return playerWon;
    }

    public String[] getwinners() {
        return this.winners;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public boolean checkIfEveryoneLost(Label label[]) {

        for (int i = 0; i < 2 + this.simulatedPlayers; i++) {
            if (isCroupier(i)) continue;
            else if (label[i].getTextFill() != Color.RED) return false;
        }
        return true;

    }

    private boolean isCroupier(int i) {
        return i == 1;
    }

    public int getSimulatedPlayersCount() {
        return this.simulatedPlayers;
    }

    public synchronized int getPlayersServed() {
            return this.playersServed;
    }


    public CardGenerator getCardGenerator() {
        return cardGenerator;
    }

}
