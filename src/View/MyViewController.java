package View;

import Server.Configurations;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ISearchable;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import com.sun.prism.paint.Stop;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class MyViewController implements IView
{

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public static Maze maze;
    public static Object[] properties;
    public MazeDisplayer mazeDisplayer;



    public MyViewController()
    {
        Configurations c = Configurations.getInstance();
        MyViewController.properties = (Object[]) (c.LoadProp());
        this.mazeDisplayer = new MazeDisplayer();
    }


    public void generateMaze(ActionEvent actionEvent)
    {
        IMazeGenerator generator = (IMazeGenerator) (MyViewController.properties[1]);

        if((this.textField_mazeRows.getText().equals("")) || (this.textField_mazeColumns.getText().equals("")))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must fill in the row/column values");
            alert.show();
            return;
        }
        if(!(isNumber(this.textField_mazeRows.getText(), this.textField_mazeColumns.getText())))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("row/column values must be integers");
            alert.show();
            return;
        }

        int row = Integer.valueOf(this.textField_mazeRows.getText());
        int col = Integer.valueOf(this.textField_mazeColumns.getText());

        try
        {
            Maze newMaze = generator.generate(row,col);
            MyViewController.maze = newMaze;

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

        this.mazeDisplayer.drawMaze(MyViewController.maze.getMazeArr());
    }

    private boolean isNumber(String row, String col)
    {
        try
        {
            Double.parseDouble(row);
            Double.parseDouble(col);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }


    public void solveMaze(ActionEvent actionEvent)
    {
        if(MyViewController.maze == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze");
            alert.show();
        }
        else
        {
            ISearchingAlgorithm searcher = (ISearchingAlgorithm) MyViewController.properties[2];
            ISearchable searchableMaze = new SearchableMaze(MyViewController.maze);
            Solution sol = searcher.solve(searchableMaze);
        }
    }

    public void exit(ActionEvent actionEvent)
    {
        Platform.exit();
        System.exit(0);
    }

    public void loadBar(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Maze");
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files (.maze)",".maze" ));
        fc.setInitialDirectory(new File("resources\\SavedMaze"));
        File chosen = fc.showOpenDialog(null);
        if(chosen == null)
        {
            return;
        }
        try {
            FileInputStream in = new FileInputStream(chosen);
            ObjectInputStream input = new ObjectInputStream(in);
            Maze newMaze = (Maze) input.readObject();
            MyViewController.maze = newMaze;
            this.mazeDisplayer.drawMaze(maze.getMazeArr());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void newBar(ActionEvent actionEvent)
    {
        this.mazeDisplayer.clear();
        this.textField_mazeRows.setText("");
        this.textField_mazeColumns.setText("");
        MyViewController.maze = null;
    }

    public void propertiesBar(ActionEvent actionEvent) throws IOException
    {
        Main.mainToProperties();
    }

    public void saveBar(ActionEvent actionEvent) throws IOException {
        if(MyViewController.maze == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze to save");
            alert.show();
        }
        else
        {
            Main.mainToSave();
        }
    }

    public void userGuide(ActionEvent actionEvent) throws IOException
    {
        Main.mainToUserGuide();
    }

    public void AppInfo(ActionEvent actionEvent) throws IOException
    {
        Main.mainToAppInfo();
    }
}
