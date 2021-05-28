package View;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SolvedController
{
    public ImageView imageView;

    public void initialize() throws FileNotFoundException
    {
        FileInputStream in = new FileInputStream("resources/Images/Solved.jpg");
        Image image = new Image(in);
        this.imageView.setImage(image);
    }

    public void goBack(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }
}
