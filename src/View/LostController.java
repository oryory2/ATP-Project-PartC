package View;

import Model.MyModel;
import javafx.event.ActionEvent;
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


    public void initialize() throws FileNotFoundException
    {
        Media sound = new Media(new File("resources/Sounds/Lost.mp3").toURI().toString());
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.play();
    }

    public void tryAgain(ActionEvent actionEvent) throws IOException
    {
        lost = true;
        Main.backToMainSolved();
    }

    public void tooHard(ActionEvent actionEvent) throws IOException
    {
        lost = true;
        MyModel.easyMode = true;
        Main.backToMainSolved();
    }
}
