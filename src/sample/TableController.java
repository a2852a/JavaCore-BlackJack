package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/** Controller for game table - what user can do */
public class TableController extends Thread  implements Initializable {

    Game Game;

    TableController(int symulatedPlayers, String playerName) {
        this.Game = new Game(symulatedPlayers, playerName, this);

    }



    //--------------

    //player[0] - gracz
    @FXML
    private Label player0Name;

    @FXML
    private GridPane player0Slots;

    @FXML
    private Label pointsPlayer0;

    //player[1] - krupier

    @FXML
    private Label player1Name;

    @FXML
    private GridPane player1Slots;

    @FXML
    private Label pointsPlayer1;

    //player[2] i  player[3]

    @FXML
    private Label player2Name;

    @FXML
    private GridPane player2Slots;

    @FXML
    private Label pointsPlayer2;


    @FXML
    private Label player3Name;

    @FXML
    private GridPane player3Slots;

    @FXML
    private Label pointsPlayer3;

    @FXML
    private Group objGroupPlayer2;

    @FXML
    private Group objGroupPlayer3;

    //-----------------
    // Sterowanie

    @FXML
    private Group cardSlotGroup;

    @FXML
    private Group objGroupEndGame;

    @FXML
    private Label gameScore;

    @FXML
    private Button buttonLeave;

    @FXML
    private Button buttonReset;

    @FXML
    private Button buttonHit;

    @FXML
    private Button buttonStand;

    //-----------------
    // Zwyciezcy

    @FXML GridPane victorsList;

    Label[] getAllPlayerPointsAsArray(){
        Label label[] = {this.pointsPlayer0, this.pointsPlayer1, this.pointsPlayer2, this.pointsPlayer3};
        return label;
    }


    /** Sets up text snd visibility of panes based on game settings */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    this.player0Name.setText(this.Game.getPlayerName());

    if(this.Game.getSimulatedPlayersCount() == 1){
        objGroupPlayer2.setVisible(true);
    }

    if(this.Game.getSimulatedPlayersCount() == 2){
        objGroupPlayer2.setVisible(true);
        objGroupPlayer3.setVisible(true);
    }

    }

    @FXML
    void handlerHit(){

        this.Game.getPlayer(0).hit();

        }

    @FXML
    void handlerStand(){

        this.Game.getPlayer(0).stand();
        this.toogleControls(false);
    }


    @FXML
    void handlerReset(){

         try{ this.join();}
        catch(InterruptedException e){System.out.println(e);}

        for(int i = 0 ; i < 8 ; i++) {

            ( (ImageView) player0Slots.getChildren().get(i) ).setImage(null);
            ( (ImageView) player1Slots.getChildren().get(i) ).setImage(null);
            ( (ImageView) player2Slots.getChildren().get(i) ).setImage(null);
            ( (ImageView) player3Slots.getChildren().get(i) ).setImage(null);

        }

        for(int i = 0 ; i < 4 ; i++){

             this.getAllPlayerPointsAsArray()[i].setText("0");
            this.getAllPlayerPointsAsArray()[i].setTextFill(Color.WHITE);

        }

        this.Game = new Game(this.Game.getSimulatedPlayersCount(),this.Game.getPlayerName(), this);


        this.toogleControls(true);
        objGroupEndGame.setVisible(false);

        ((Label) victorsList.getChildren().get(0)).setText("");
        ((Label) victorsList.getChildren().get(1)).setText("");
        ((Label) victorsList.getChildren().get(2)).setText("");

    }

    @FXML
    void handlerLeave(){

        System.exit(0);

    }


    //OPEROWANIE UI

    void croupierToogleHidden(boolean option){ //Wlaczenie/wylaczenie zakrycia krupiera

        if(option == true){

            this.Game.setBackupImg(1,( (ImageView) player1Slots.getChildren().get( Game.getCurrentSlot(1)-1 ) ).getImage()); //backup obrazka
            ( (ImageView) player1Slots.getChildren().get( Game.getCurrentSlot(1)-1 ) ).setImage(new Image("res/cards_png/rewers.jpg"));
            pointsPlayer1.setText("??");

        }else {
            ( (ImageView) player1Slots.getChildren().get( Game.getCurrentSlot(1)-1 ) ).setImage(this.Game.getBackupImg(1));
            pointsPlayer1.setText(Integer.toString(Game.getScore(1)));
        }

    }





    void makeUIChanges(int index, Image image , boolean playerLost){

        //Pokazanie nowej karty w slocie
        if(image != null ) {
            GridPane tempGridPane = ((GridPane) this.cardSlotGroup.getChildren().get(index));
            int tempCardSlot = this.Game.getPlayer(index).getCurrentCardSlot() -1; //Slot karty wskazuje na nastepny el. do ktorego zostanie wlozona karta
            ((ImageView) tempGridPane.getChildren().get(tempCardSlot)).setImage(image);
        }
        //Aktualizacja punktow
        this.getAllPlayerPointsAsArray()[index].setText(Integer.toString(this.Game.getScore(index)));

        //Aktualizacja statusu gracza (czy przegral)
        if(playerLost == true){
            if(index == 0) toogleControls(false);
            this.getAllPlayerPointsAsArray()[index].setTextFill(Color.RED);

        }

    }

    void makeFinalUiChanges(boolean whoWon){

        if(whoWon){
            gameScore.setTextFill(Color.BLUE);
            gameScore.setText("Wygrales!");
        }else{
            gameScore.setTextFill(Color.RED);
            gameScore.setText("Przegrales!");
        }
        int tempIndex = 0;
        String tempWinners[] = this.Game.getwinners();
        for(int i = 0 ; i < tempWinners.length ; i++)
        ((Label) victorsList.getChildren().get(tempIndex++)).setText(tempWinners[i]);
        this.objGroupEndGame.setVisible(true);
    }

    void toogleControls(boolean toogle){

        if(toogle == true){

            this.buttonHit.setVisible(true);
            this.buttonStand.setVisible(true);

        }else {

            this.buttonHit.setVisible(false);
            this.buttonStand.setVisible(false);
        }
    }




}
