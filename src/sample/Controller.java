package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller for main menu, also starts the game */
public class Controller implements Initializable {

    Stage parentStage;

    Controller(Object stage){

        this.parentStage = (Stage) stage;

    };


    //Menu glowne
    @FXML
    private GridPane menu;


    //Instrukcja
    @FXML
    private GridPane menuInstruction;

    @FXML
    private TextArea menuInstructionText;

    //Ustawienia
    @FXML
    private GridPane menuGameSetting;

    @FXML
    private TextField menuSettingsPlayerName;

    @FXML
    private ChoiceBox menuSettingsPlayerCount;
    ObservableList <String> optionSimPlayers = FXCollections.observableArrayList("Brak symulacji", "1 gracz", "2 graczy");

    /** Sets up the text on interface */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        menuInstructionText.setText("Gra jest prowadzona między krupierem reprezentującym kasyno i graczem. Zadaniem gracza jest uzyskanie punktowego wyniku w otrzymanych od krupiera kartach w maksymalnej wysokości 21. Najlepszym układem jest naturalnie właśnie blackjack, czyli 21 punktów.\n" +
                "\n" +
                "W blackjacka gra się talią złożoną z 52 kart, najczęściej tych talii jest więcej, zdarza się, że nawet osiem. Punktacja kart wygląda następująco: karty od 2 do 9 mają wartość wyrażoną swoją wysokością (czyli 5 karo to 5 punktów, ósemka kier to 8 punktów itd.), 10 i figury (walet, dama i król) to 10 punktów, natomiast as punktowany jest za 11 lub 1, w zależności od tego, która ewentualność jest bardziej korzystna dla grającego.\n" +
                "\n" +
                "Na początku rozgrywki gracz i krupier otrzymują po dwie karty po uprzednim postawieniu przez gracza zakładu. Jedna z kart krupiera pozostaje zakryta do końca rozgrywki. Zasady blackjacka w wersji europejskiej nieznacznie się różnią w tym momencie: krupier na początku rozdaje sobie tylko jedną, odkrytą kartę.\n\n" +
                "Po otrzymaniu dwóch pierwszych kart gracz ma do wyboru następujące możliwości:\n" +
                "\n" +
                "* może dobrać kartę, jeżeli uważa, że liczba punktów uzbieranych w dwóch pierwszych kartach jest zbyt niska do wygrania partii (drawAnotherCard),\n" +
                "* może nie dobierać karty i pozostać przy dwóch pierwszych kartach (stand)");

        menuSettingsPlayerCount.setValue("Brak symulacji");
        menuSettingsPlayerCount.setItems(optionSimPlayers);
        menuSettingsPlayerCount.setTooltip(new Tooltip("Wybierz liczbe graczy"));

    }

    //HANDLERY

    @FXML
    private void handlerMenuLeave() {

        System.exit(0);

    }

    @FXML
    private void handlerMenuInstruction() {

    if(menu.isVisible() == true) {
        menu.setVisible(false);
        menuInstruction.setVisible(true);
    } else {
        menu.setVisible(true);
        menuInstruction.setVisible(false);
    } }

    @FXML
    private void handlerMenuNewGame() {

        if(menu.isVisible() == true) {
            menu.setVisible(false);
            menuGameSetting.setVisible(true);
        } else {
            menu.setVisible(true);
            menuGameSetting.setVisible(false);
        } }

    @FXML
    private void handlerStart(){

    String playerName;
    int simulatedPlayers = 0;

    playerName = menuSettingsPlayerName.getText();

    switch ((String)menuSettingsPlayerCount.getValue()){

        case "Brak symulacji": {simulatedPlayers = 0; break;}

        case "1 gracz": {simulatedPlayers = 1; break;}

        case "2 graczy": {simulatedPlayers = 2; break;}
    };

        Parent parent = new Parent() {
        };

        FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
        Stage tableStage = new Stage();
        TableController tableController = new TableController(simulatedPlayers,playerName);
        loader.setController(tableController);
        try {
            parent = loader.load();

        } catch (IOException e){System.out.print(e);};

        tableStage.setTitle("Black Jack 2.0");
        tableStage.setScene(new Scene(parent, 920, 594));
        tableStage.setResizable(true);

        tableStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/res/ikona.PNG")));

        tableStage.setOnCloseRequest(evt -> {
            System.exit(0);

        });


        tableStage.show();
        this.parentStage.hide();

    }

}