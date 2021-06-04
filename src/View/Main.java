package View;

import Model.IModel;
import Model.MyModel;
import Server.Configurations;
import Server.Server;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.*;
import Server.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application
{
    public static Stage primaryStage;
    public static MediaPlayer mediaPlayer;
    public static IModel model;
    public static MyViewModel myViewModel;
    public static MyViewController myViewController;



    public void start(Stage primaryStage) throws Exception
    {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("UserGuide.fxml"));
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }

    public static void realStart() throws IOException
    {
        Media sound = new Media(new File("resources/Sounds/mainMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        Main.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
        model = new MyModel();
        myViewModel = new MyViewModel(model);
        myViewController = fxmlLoader.getController();
        myViewController.setViewModel(myViewModel);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void backToMain() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }

    public static void mainToProperties() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("Properties.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToSave() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Save.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToUserGuide() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("UserGuide.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToAppInfo() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("AppInfo.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToSolved() throws IOException
    {
        Main.mediaPlayer.stop();
        Parent root = FXMLLoader.load(Main.class.getResource("Solved.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void backToMainSolved() throws IOException
    {
        Media sound = new Media(new File("resources/Sounds/mainMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
    }
}
