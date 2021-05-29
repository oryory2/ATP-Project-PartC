package View;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;

public class SolvedController
{
    public ImageView imageView;

    public void initialize() throws FileNotFoundException
    {
        FileInputStream in = new FileInputStream("resources/Images/Solved.jpg");
        Image image = new Image(in);
        this.imageView.setImage(image);
        Media sound = new Media(new File("resources/Sounds/solvedMusic.mp3").toURI().toString());
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.play();
    }

    public void goBack(ActionEvent actionEvent) throws IOException
    {
        Main.backToMainSolved();
    }
}
