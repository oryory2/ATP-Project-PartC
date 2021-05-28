package View;

import Server.Configurations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main extends Application
{
    public static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
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

    public static void mainToUserGuide() throws IOException {
        MyViewController.properties = Configurations.getInstance().LoadProp();
        Parent root = FXMLLoader.load(Main.class.getResource("UserGuide.fxml"));
        Scene scene = new Scene(root,600,450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainToAppInfo() throws IOException {
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
