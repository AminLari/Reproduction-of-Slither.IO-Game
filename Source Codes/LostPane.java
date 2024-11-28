package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LostPane {

    @FXML
    public AnchorPane lostpane;
    @FXML
    public Button lostpanebackButton;

    @FXML
    void initialize(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("LostPane.fxml"));
        LostPane lostPane =loader.getController();
        Stage stage=new Stage(StageStyle.UNDECORATED);
        stage.initOwner(lostPane.lostpane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        try
        {
            stage.setScene(new Scene((loader.load())));
            FXMLLoader l=new FXMLLoader(getClass().getResource("game.fxml"));
            Game controller=l.getController();

            lostpanebackButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent event) {
                    stage.close();
                    Stage G=(Stage) (controller.gameScene.getScene().getWindow());
                    G.close();

                }
            });

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        stage.show();
    }


}
