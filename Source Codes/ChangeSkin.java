package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ChangeSkin {
    public Button backskin;

    //This controller just returns a value that with it in the game.java
    //We assign colors for changing the color of the main snake
    @FXML
    private void WColor(ActionEvent actionEvent) throws IOException {
        write("0");
    }

    @FXML
    private void YColor(ActionEvent actionEvent) throws IOException {
        write("1");
    }

    @FXML
    private void RColor(ActionEvent actionEvent) throws IOException {
        write("2");
    }

    @FXML
    private void CColor(ActionEvent actionEvent) throws IOException {
        write("3");
    }

    //Writes the value that should be returned in a file
    private void write(String x) {
        try {
            FileWriter writer = new FileWriter("names.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(x);
            bufferedWriter.close();
        }catch (Exception ignored){}

    }
}