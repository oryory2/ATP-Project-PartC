package View;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;

public class SolvedController
{
    public ImageView imageView;

    /**
     * initializing the Maze-Solved window
     * after the user have solved the maze
     * @throw FileNotFoundException
     */
    public void initialize() throws FileNotFoundException
    {
        FileInputStream in = new FileInputStream("resources/Images/Solved.jpg");
        Image image = new Image(in);
        this.imageView.setImage(image);
        Media sound = new Media(new File("resources/Sounds/solvedMusic.mp3").toURI().toString());
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.setAutoPlay(true);
        Main.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        Main.mediaPlayer.play();

        if(PropertiesController.music)
        {
            Main.mediaPlayer.setMute(true);
        }
    }

    /**
     * going back to the game window
     * after the user have solved the maze
     * @param actionEvent pressing the "Go Back" Button (ActionEvent)
     * @throw IOException
     */
    public void goBack(ActionEvent actionEvent) throws IOException
    {
        Main.solvedLostToMain();
    }
}
