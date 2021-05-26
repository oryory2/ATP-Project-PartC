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
    private IMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Maze maze;
    public Object[] properties;
    public MazeDisplayer mazeDisplayer;
    private int fileCounter;



    public MyViewController()
    {
        Configurations c = Configurations.getInstance();
        this.properties = (Object[]) (c.LoadProp());
        this.mazeDisplayer = new MazeDisplayer();
        /*String fileName = System.getProperty("resources");
        File folder = new File(fileName);
        File[] listOfFiles = folder.listFiles();
        int maxMazeCounter = 0;
        for(int i = 0; i < listOfFiles.length; ++i)
        {
            if (listOfFiles[i].getName().contains("maze"))
            {
                maxMazeCounter++;
            }
        }
        this.fileCounter = maxMazeCounter;*/
    }


    public void generateMaze(ActionEvent actionEvent)
    {
        this.generator = (IMazeGenerator)(this.properties[1]);

        if((this.textField_mazeRows.getText() == "") || (this.textField_mazeColumns.getText() == ""))
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
            Maze newMaze = this.generator.generate(row,col);
            this.maze = newMaze;
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

        this.mazeDisplayer.drawMaze(maze.getMazeArr());
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
        if(this.maze == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze");
            alert.show();
            return;
        }
        else
        {
            ISearchingAlgorithm searcher = (ISearchingAlgorithm) this.properties[2];
            ISearchable searchableMaze = new SearchableMaze(this.maze);
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
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files (.maze)", ".maze"));
        fc.setInitialDirectory(new File("resources"));
        File chosen = fc.showOpenDialog(null);
        if(chosen == null)
        {
            return;
        }
        try {
            FileInputStream in = new FileInputStream(chosen);
            ObjectInputStream input = new ObjectInputStream(in);
            Maze newMaze = (Maze) input.readObject();
            this.maze = newMaze;
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
        this.maze = null;
    }

    public void propertiesBar(ActionEvent actionEvent) throws IOException
    {
        Main.mainToProperties();
    }

    public void saveBar(ActionEvent actionEvent)
    {
        if(this.maze == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze to save");
            alert.show();
            return;
        }
        else
        {
            try
            {
                String fileName = "resources/" + this.fileCounter + ".maze";
                FileOutputStream out = new FileOutputStream(fileName);
                ObjectOutputStream output = new ObjectOutputStream(out);
                output.writeObject(this.maze);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
