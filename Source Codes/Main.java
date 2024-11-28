// BP- Final Project
/* Code by:
    Mohammad Amin Lari - 9623096
    Sina Mahmoudi - 9623098
    Shima Naseri - 9623108
 */

package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.*;
import javafx.stage.StageStyle;

import java.net.URL;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.println("Program Started...");

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Slither.io");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();

        //Plays music when the program starts to work
        final Task task=new Task() {
            @Override
            protected Object call() throws Exception {
                int s=INDEFINITE;
                AudioClip audioClip=new AudioClip(getClass().getResource("Res/music.mp3").toExternalForm());
                audioClip.setVolume(0.5f);
                audioClip.setCycleCount(s);
                audioClip.play();
                return null;
            }
        };
        Thread thread=new Thread(task);
        thread.start();
    }

    public static void main(String[] args) {

        launch(args);
    }

}
