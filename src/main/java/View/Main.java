package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;


public class Main extends Application
{
    public static Stage primaryStage;
    public static MediaPlayer mediaPlayer;
    public static IModel model;
    public static MyViewModel myViewModel;
    public static MyViewController myViewController;


    /**
     * starting the app
     * @param primaryStage the primary stage in our case the User Guide(Stage)
     */
    public void start(Stage primaryStage) throws Exception
    {
        //System.out.println(System.getProperty("java.io.tmpdir"));
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("UserGuide.fxml"));
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }

    /**
     * starting the game itself after reading the user guide
     */
    public static void realStart() throws IOException
    {
        Media sound = new Media(new File("resources/Sounds/mainMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
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

    /**
     * returns to the game window itself from anywhere other than the game window, and the Solved/Lost windows
     */
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

    /**
     * Moves from the game window to the Properties window
     */
    public static void mainToProperties() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("Properties.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Moves from the game window to the Save window
     */
    public static void mainToSave() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Save.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Moves from the game window to the User Guide window
     */
    public static void mainToUserGuide() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("UserGuide.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Moves from the game window to the Application Info window
     */
    public static void mainToAppInfo() throws IOException
    {
        Parent root = FXMLLoader.load(Main.class.getResource("AppInfo.fxml"));
        Scene scene = new Scene(root,644,540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Moves to the Solved window after the user has solved the maze
     */
    public static void mainToSolved() throws IOException
    {
        Main.mediaPlayer.stop();
        Parent root = FXMLLoader.load(Main.class.getResource("Solved.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Moves from the game window to the Losing window
     * after losing the game in hard mode
     */
    public static void mainToLost() throws IOException {
        Main.mediaPlayer.stop();
        Parent root = FXMLLoader.load(Main.class.getResource("Lost.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Returns to the game itself from Solved/Lost windows
     * remembering that the maze is solved according to the user's request
     * and continues to present the solution
     */
    public static void solvedLostToMain() throws IOException
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

    /**
     * Stops running the App
     */
    public void stop()
    {
        if (mediaPlayer != null)
        {
            MyModel.mazeGeneratingServer.stop();
            MyModel.solveSearchProblemServer.stop();
        }
    }
}
