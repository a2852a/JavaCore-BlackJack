package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *Sets up and starts main menu
 *
 * @author  Sebastian Jóźwiak
 * @version 2.1
 */

public class Main extends Application {


    /** Sets up the stage for main menu window */
    @Override
    public void start(Stage menuStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));

        Controller controller = new Controller(menuStage);
        loader.setController(controller);
        Parent parent = loader.load();

        menuStage.setTitle("Black Jack 2.0");
        menuStage.setScene(new Scene(parent, 275, 420));
        menuStage.setResizable(false);

        menuStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/res/ikona.PNG")));

        menuStage.show();

    }

    public static void main(String[] args) {
       Application.launch(args);
    }
}
