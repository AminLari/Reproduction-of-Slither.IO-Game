package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    public Label label1;
    @FXML
    public Button settingsButton;
    @FXML
    public AnchorPane myPane;
    @FXML
    public Button changeSkinButton;
    @FXML
    public Button playButton;
    @FXML
    public Button quitButton;

    public Label nickname;
    public TextField nickNameText;

    public void ChangeLabel(ActionEvent actionEvent) {
        label1.setText("Button Clicked!");
    }

    @FXML
    void initialize()
    {

        nickNameText.setOnAction(event -> {
            try
            {
                BufferedWriter bw = new BufferedWriter(new FileWriter("name.txt"));
                bw.write(getName());
                bw.close();
                System.out.println(getName());
            }
            catch (IOException ex){}
        });


        settingsButton.setOnAction(event -> {
            FXMLLoader loader2=new FXMLLoader(getClass().getResource("settings.fxml"));
            Stage stage2=new Stage(StageStyle.UNDECORATED);
            stage2.initOwner(myPane.getScene().getWindow());
            stage2.initModality(Modality.WINDOW_MODAL);
            try
            {
                stage2.setScene(new Scene((loader2.load())));
                stage2.setTitle("Settings");
                ControllerSt controller2=loader2.getController();
                controller2=loader2.getController();
                stage2.show();
                controller2.backButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        stage2.close();
                    }
                });

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            stage2.setTitle("Settings");
            stage2.show();
        });
        changeSkinButton.setOnAction(event -> {
            FXMLLoader loader2=new FXMLLoader(getClass().getResource("changeSkin.fxml"));
            Stage stage3=new Stage(StageStyle.UNDECORATED);
            stage3.initOwner(myPane.getScene().getWindow());
            stage3.initModality(Modality.WINDOW_MODAL);
            try
            {
                stage3.setScene(new Scene((loader2.load())));
                ChangeSkin controller3=loader2.getController();
                controller3=loader2.getController();
                stage3.setTitle("Change Skin");
                stage3.show();
                controller3.backskin.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        stage3.close();
                    }
                });

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            stage3.setTitle("Change Skin");
            stage3.show();
        });
        playButton.setOnAction(event -> {
            FXMLLoader loader3=new FXMLLoader(getClass().getResource("game.fxml"));
            Stage stage4=new Stage();
            stage4.initOwner(myPane.getScene().getWindow());
            stage4.initModality(Modality.WINDOW_MODAL);
            try
            {
                stage4.setScene(new Scene((loader3.load())));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            stage4.setTitle("Game");
            stage4.show();
        });
        quitButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                Quit(event);
            }
        });


    }
    public void Quit(ActionEvent e){
        Alert alert=new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are You Sure You Want To Quit?");
        ButtonType Y=new ButtonType("Yes");
        ButtonType N=new ButtonType("No");
        alert.getButtonTypes().setAll(Y,N);

        Optional<ButtonType> r=alert.showAndWait();
        if(r.get()==Y){
            Stage G=(Stage) (myPane.getScene().getWindow());
            G.close();
        }
        else if (r.get()==N){}


    }


    String getName()
    {
        return String.format(nickNameText.getText());
    }
}