package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.media.AudioClip;

import java.io.IOException;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class ControllerSt
{
    @FXML
    public Button backButton;


    @FXML
    public MenuItem soundOn;
    @FXML
    public MenuItem soundOff;
    private AudioClip audioClip=new AudioClip(getClass().getResource("Res/music.mp3").toExternalForm());

    boolean play = true;

    //Plays the song when the song is not playing and the sound on is clicked
    @FXML
    private void soundOn()
    {
        final Task task = new Task() {

            @Override
            public Object call() {

                audioClip.setVolume(0.5f);
                audioClip.setCycleCount(INDEFINITE);
                audioClip.play();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        play = true;
    }

    //Stops the song which is playing by clicking the sound off item
    @FXML
    private void soundOff()
    {
        audioClip.stop();
        play = false;
    }
}
