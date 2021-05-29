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
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyViewController implements IView
{

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public static Maze maze;
    public static Object[] properties;
    public MazeDisplayer mazeDisplayer;
    public Label thisPose;
    public Button solveMaze;
    private boolean ctrlFlag;


    public MyViewController()
    {
        Configurations c = Configurations.getInstance();
        MyViewController.properties = (Object[]) (c.LoadProp());
        this.mazeDisplayer = new MazeDisplayer();
    }

    public void initialize()
    {
        if(MyViewController.maze != null)
        {
            this.mazeDisplayer.drawMaze(MyViewController.maze.getMazeArr());
            this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
            this.solveMaze.setDisable(false);
        }
        else
        {
            this.solveMaze.setDisable(true);
        }


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

        int row = Integer.parseInt(this.textField_mazeRows.getText());
        int col = Integer.parseInt(this.textField_mazeColumns.getText());

        try
        {
            MyViewController.maze = generator.generate(row,col);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
        this.mazeDisplayer.setPlayerPosition(0,0);
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
        this.mazeDisplayer.solution = null;
        this.mazeDisplayer.drawMaze(MyViewController.maze.getMazeArr());
        this.solveMaze.setText("Solve Maze");
        this.mazeDisplayer.clickedCounter = 0;
        this.solveMaze.setDisable(false);
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
            alert.setContentText("There is no existing maze to Solve");
            alert.show();
        }
        else
        {
            this.mazeDisplayer.clickedCounter++;

            if(this.mazeDisplayer.clickedCounter % 2 == 1)
            {
                if(this.mazeDisplayer.solution != null)
                {
                    this.mazeDisplayer.drawSolution(this.mazeDisplayer.solution);
                    return;
                }
                ISearchingAlgorithm searcher = (ISearchingAlgorithm) MyViewController.properties[2];
                ISearchable searchableMaze = new SearchableMaze(MyViewController.maze);
                Solution sol = searcher.solve(searchableMaze);
                this.mazeDisplayer.drawSolution(sol);
                this.solveMaze.setText("Unsolve Maze");
            }
            else
            {
                this.mazeDisplayer.solution = null;
                this.mazeDisplayer.drawMaze(MyViewController.maze.getMazeArr());
                this.solveMaze.setText("Solve Maze");
            }
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
            this.mazeDisplayer.setPlayerPosition(0,0);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze successfully loaded");
            alert.show();
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
        this.solveMaze.setDisable(true);
        this.thisPose.setText("");
        this.solveMaze.setText("Solve Maze");
        this.mazeDisplayer.clickedCounter = 0;
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

    public void keyPressed(KeyEvent keyEvent) throws IOException {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

        switch (keyEvent.getCode()) {
            case NUMPAD8 : // up
                if(!(this.legalMove("up")))
                    return;
                row -= 1;
                break;

            case NUMPAD9 : // up right
                if(!(this.legalMove("up right")))
                    return;
                row -= 1 ;
                col += 1;
                break;

            case NUMPAD6 : // right
                if(!(this.legalMove("right")))
                    return;
                col += 1;
                break;

            case NUMPAD3 : // down right
                if(!(this.legalMove("down right")))
                    return;
                row += 1 ;
                col += 1;
                break;

            case NUMPAD2 : // down
                if(!(this.legalMove("down")))
                    return;
                row += 1;
                break;

            case NUMPAD1 : // down left
                if(!(this.legalMove("down left")))
                    return;
                row += 1 ;
                col -= 1;
                break;

            case NUMPAD4 : // left
                if(!(this.legalMove("left")))
                    return;
                col -= 1;
                break;

            case NUMPAD7 : // up left
                if(!(this.legalMove("up left")))
                    return;
                row -= 1 ;
                col -= 1;
                break;

            default:
                return;
        }

        this.mazeDisplayer.setPlayerPosition(row,col);
        if((row == MyViewController.maze.getMax_rows() - 1) && (col == MyViewController.maze.getMax_columns() - 1))
        {
            this.mazeDisplayer.setPlayerPosition(0,0);
            Main.mainToSolved();
        }
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
        keyEvent.consume();
    }

    public boolean legalMove(String direction)
    {
        int thisRow = this.mazeDisplayer.getPlayerRow();
        int thisCol = this.mazeDisplayer.getPlayerCol();

        if(direction.equals("up"))
        {
            if((thisRow - 1 < 0) || (MyViewController.maze.getMazeArr()[thisRow - 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("up right"))
        {
            if((thisRow - 1 < 0) || (thisCol + 1 >= MyViewController.maze.getMax_columns()) || (MyViewController.maze.getMazeArr()[thisRow - 1][thisCol + 1] == 1))
                return false;
            if((MyViewController.maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (MyViewController.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("right"))
        {
            if((thisCol + 1 >= MyViewController.maze.getMax_columns()) || (MyViewController.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down right"))
        {
            if((thisRow + 1 >= MyViewController.maze.getMax_rows()) || (thisCol + 1 >= MyViewController.maze.getMax_columns()) || (MyViewController.maze.getMazeArr()[thisRow + 1][thisCol + 1] == 1))
                return false;
            if((MyViewController.maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (MyViewController.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down"))
        {
            if((thisRow + 1 >= MyViewController.maze.getMax_rows()) || (MyViewController.maze.getMazeArr()[thisRow + 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("down left"))
        {
            if((thisRow + 1 >= MyViewController.maze.getMax_rows()) || (thisCol - 1 < 0) || (MyViewController.maze.getMazeArr()[thisRow + 1][thisCol - 1] == 1))
                return false;
            if((MyViewController.maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (MyViewController.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("left"))
        {
            if((thisCol - 1 < 0) || (MyViewController.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("up left"))
        {
            if((thisRow - 1 < 0) || (thisCol - 1 < 0) || (MyViewController.maze.getMazeArr()[thisRow - 1][thisCol - 1] == 1))
                return false;
            if((MyViewController.maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (MyViewController.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }

        return true;
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        this.mazeDisplayer.requestFocus();
    }

    public void scrollMouse(ScrollEvent scrollEvent)
    {
        if(this.ctrlFlag)
            this.mazeDisplayer.getOnScroll(scrollEvent);
    }


    public void ctrlPressed(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = true;
        }
    }

    public void ctrlReleased(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = false;
        }
    }
}
