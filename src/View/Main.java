package View;

import Server.Configurations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application
{
    public static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
        //playMusic();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    public void playMusic()
    {
/*        File path = new File ("resources\\Sounds\\mainMusic.mp3");
        MediaPlayer m = new MediaPlayer(new Media(path.toURI().toString()));
        m.play();*/
        Media sound = new Media(getClass().getResource("resources\\Sounds\\mainMusic.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public static void backToMain() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("MyView.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToProperties() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("Properties.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToSave() throws IOException {
        MyViewController.properties = Configurations.getInstance().LoadProp();
        Parent root = FXMLLoader.load(Main.class.getResource("Save.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToUserGuide() throws IOException
    {
        MyViewController.properties = Configurations.getInstance().LoadProp();
        Parent root = FXMLLoader.load(Main.class.getResource("UserGuide.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToAppInfo() throws IOException
    {
        MyViewController.properties = Configurations.getInstance().LoadProp();
        Parent root = FXMLLoader.load(Main.class.getResource("AppInfo.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToSolved() throws IOException {
        MyViewController.properties = Configurations.getInstance().LoadProp();
        Parent root = FXMLLoader.load(Main.class.getResource("Solved.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
