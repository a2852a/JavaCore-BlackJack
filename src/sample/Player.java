package sample;

import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.Random;



/** Provides basic AI control over the threads (players and croupier), and their definition */
public class Player extends Thread {

    private int playerIndex;

    private int currentCardSlot;
    private Image lastCardBackup;
    private int score;

    private TableController tableController;

    private Random numberGenerator = new Random();

    Player(int index, TableController tableController) {


        this.playerIndex = index;
        this.tableController = tableController;
    }

    /** Method where AI threads run */
    @Override
    public void run() {

        //Krupier
        if (this.playerIndex == 1) {

            Platform.runLater( //wyrazenie lambda 0 parametrow zwroc zawartosc (kompilator sam rozpoznaje potrzebne parametry
                    () -> {

                        this.hit();
                        this.hit();

                        this.hit(0);
                        this.hit(0);

                        if(this.tableController.Game.getSimulatedPlayersCount() >= 1){this.hit(2); this.hit(2);}
                        if(this.tableController.Game.getSimulatedPlayersCount()  > 1){this.hit(3); this.hit(3);}

                        this.tableController.croupierToogleHidden(true);
                    }
            );

            this.waitForOthers(600);

            Platform.runLater(
                    () -> {
                        this.tableController.croupierToogleHidden(false);
                    }
            );

            this.sleepFor(600);

            //rozpocznij algorytm gry
            while (true) {

                if (this.tableController.Game.getScore(this.playerIndex) >= 17
                        || (this.tableController.Game.checkIfEveryoneLost(this.tableController.getAllPlayerPointsAsArray()) == true))
                    break;

                Platform.runLater(
                        () -> {
                            this.hit();
                        }
                );
                this.sleepFor();

            }
            //Ocen gre
            Platform.runLater(
                    () -> {
                        //System.out.println("Ostatnia prosta");
                        TableController tempTC = tableController;
                        tempTC.makeFinalUiChanges(tempTC.Game.evaluateGame());

                    }
            );

            //Gracze symulowani
        } else {
            //Reszta graczy
            this.sleepFor(600);
            while (true) {
                if (this.tableController.Game.getScore(playerIndex) >= 17) {
                    if(this.tableController.Game.getScore(playerIndex) > 21) break;
                    this.stand();
                    break;
                }

                Platform.runLater(
                        () -> {
                            this.hit();
                        }
                );
                this.sleepFor();

            }
        }

        //Koniec watku

    }

    //OPERACJE NA KARTACH

    /** Returns number of the slot where next card will appear */
    public void getNextCardSlot() {
        currentCardSlot++;
        if (currentCardSlot == 8) currentCardSlot = 0;
    }

    public int getCurrentCardSlot() {
        return currentCardSlot;
    }

    /** Checks card "value" */
    private void countCards(Card card) {

        switch (card.getShape()) {

            case 0: {
                if (this.score + 11 > 21) this.score += 1;
                else this.score += 11;
                break;
            }
            case 10: {
                this.score += 10;
                break;
            }
            case 11: {
                this.score += 10;
                break;
            }
            case 12: {
                this.score += 10;
                break;
            }
            default: {
                this.score += card.getShape() + 1;
                break;
            }

        }
    }

    private Image drawAnotherCard() {
        Game game = this.tableController.Game;

        Card card = game.getCardGenerator().drawCard();
        this.countCards(card);
        this.getNextCardSlot();


        return game.getCardGenerator().karta2Image(card);
        //return null;
    }

    private boolean isPlayerAbove21(int index) {
        if (this.getScore() > 21) {
            this.tableController.Game.backOff();
            return true;
        } else return false;
    }

    /** Hit move - current thread */
    public void hit() { //Wyciagnij karte dla gracza o indexie obiektu
        this.tableController.makeUIChanges(this.playerIndex, this.drawAnotherCard(), false);
        this.tableController.makeUIChanges(this.playerIndex, null, this.isPlayerAbove21(this.playerIndex));
    }
    /** Hit move - for player pointed by index */
    public void hit(int index) { //Wyciagnij karte dla gracza o podanym indexie (Dla krupiera)
        Player tempPlayer = this.tableController.Game.getPlayer(index);
        this.tableController.makeUIChanges(index, tempPlayer.drawAnotherCard(), false);
        this.tableController.makeUIChanges(index, null, this.isPlayerAbove21(index));
    }
    /** stand move */
    public void stand() {
        Game game = this.tableController.Game;
        this.tableController.Game.backOff();
    }

    //GETTERY SETTERY

    public int getScore() {
        return score;
    }

    public void setBackupImg(Image img) {
        this.lastCardBackup = img;
    }

    public Image getBackupImg() {
        return this.lastCardBackup;
    }




    //OCZEKIWANIE

    /** Method called by croupier when he awaits till every player stands */
    private void waitForOthers(int interval) {
        //Sleep w petli - "optymalizacja"
        while (this.tableController.Game.getPlayersServed() > 0) {
            try {
                sleep(interval);
            } catch (InterruptedException e) {
                System.out.println();
            }
        }
        ;
        try {
            sleep(interval);
        } catch (InterruptedException e) {
            System.out.println();
        }
    }

    private void sleepFor(int howLong){

        try {
            sleep(howLong);
        } catch (InterruptedException e) {
            System.out.println();
        }

    }

    private void sleepFor(){
        try {
            sleep(this.numberGenerator.nextInt(500)+800);
        } catch (InterruptedException e) {
            System.out.println();
        }

    }

    }



