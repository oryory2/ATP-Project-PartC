package View;

import Model.MyModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LostController
{
    public static boolean lost = false;

    /**
     * Enter the losing window after losing in hard mode
     * Loss in a hard-mode game occurs when you touch a wall
     * or when you go beyond the maze boundaries
     */
    public void initialize() throws FileNotFoundException
    {
        //play losing music
        Media sound = new Media(new File("resources/Sounds/Lost.mp3").toURI().toString());
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.play();
    }

    /**
     * Go back to the beginning and try to solve the maze again
     * Still in Hard Mode
     * @param actionEvent Clicking the "Try Again" button (ActionEvent)
     */
    public void tryAgain(ActionEvent actionEvent) throws IOException
    {
        lost = true;
        Main.backToMainSolved();
    }

    /**
     * Go back to the beginning and try to solve the maze again
     * This time in easy mode
     * @param actionEvent Clicking the "It's Too Hard" button (ActionEvent)
     */
    public void tooHard(ActionEvent actionEvent) throws IOException
    {
        lost = true;
        MyModel.easyMode = true;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Switched to Easy Mode");
        alert.show();
        Main.backToMainSolved();
    }
}
