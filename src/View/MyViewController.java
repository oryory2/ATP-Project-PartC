package View;

import Model.MyModel;
import Server.Configurations;
import ViewModel.MyViewModel;
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
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer
{

    public MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Label thisPose;
    public Button solveMaze;
    public BorderPane BigBorderPane;
    public Pane canvas;
    private boolean ctrlFlag;


    public MyViewController()
    {
        this.mazeDisplayer = new MazeDisplayer();
    }

    public void setViewModel(MyViewModel viewModel)
    {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
        if(viewModel.getMaze() != null)
        {
            if(LostController.lost)
            {
                this.viewModel.setPlayerPosition(0,0);
                this.mazeDisplayer.setPlayerPosition(0,0);
                LostController.lost = false;
            }
            this.mazeDisplayer.drawMaze(this.viewModel.getMaze().getMazeArr());
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
        if((this.textField_mazeRows.getText().equals("")) || (this.textField_mazeColumns.getText().equals("")))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must fill in the row/column values");
            alert.show();
            this.textField_mazeRows.setText("");
            this.textField_mazeColumns.setText("");
            return;
        }
        if(!(isNumber(this.textField_mazeRows.getText(), this.textField_mazeColumns.getText())))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("row/column values must be integers");
            alert.show();
            this.textField_mazeRows.setText("");
            this.textField_mazeColumns.setText("");
            return;
        }

        int row = Integer.parseInt(this.textField_mazeRows.getText());
        int col = Integer.parseInt(this.textField_mazeColumns.getText());

        if((row < 2) || (col < 2))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("One or more of the supplied sizes are not legal! \nRow and Column values must be at least 2");
            alert.show();
            this.textField_mazeRows.setText("");
            this.textField_mazeColumns.setText("");
            return;
        }
        try
        {
            this.viewModel.generateMaze(row, col);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void mazeGenerated()
    {
        this.mazeDisplayer.setPlayerPosition(0,0);
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
        this.mazeDisplayer.solution = null;
        this.mazeDisplayer.drawMaze(this.viewModel.getMaze().getMazeArr());
        this.solveMaze.setText("Solve Maze");
        this.mazeDisplayer.clickedCounter = 0;
        this.solveMaze.setDisable(false);
        mazeDisplayer.widthProperty().bind(canvas.widthProperty());
        mazeDisplayer.heightProperty().bind((canvas.heightProperty()));
    }


    public void solveMaze(ActionEvent actionEvent)
    {
        if(this.viewModel.getMaze() == null)
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
                this.viewModel.solveMaze();
            }
            else
            {
                this.mazeDisplayer.solution = null;
                this.viewModel.setSolution(null);
                this.mazeDisplayer.drawMaze(this.viewModel.getMaze().getMazeArr());
                this.solveMaze.setText("Solve Maze");
            }
        }
    }

    private void mazeSolved()
    {
        this.mazeDisplayer.drawSolution(this.viewModel.getSolution());
        this.solveMaze.setText("Unsolve Maze");
    }

    public void newBar(ActionEvent actionEvent)
    {
        this.mazeDisplayer.clear();
        this.textField_mazeRows.setText("");
        this.textField_mazeColumns.setText("");
        this.viewModel.restart();
        this.viewModel.setSolution(null);
        this.solveMaze.setDisable(true);
        this.thisPose.setText("");
        this.solveMaze.setText("Solve Maze");
        this.mazeDisplayer.clickedCounter = 0;
        Main.mediaPlayer.stop();
        Media sound = new Media(new File("resources/Sounds/mainMusic.mp3").toURI().toString());
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.play();
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
            this.viewModel.restart();
            this.viewModel.setMaze(newMaze);
            this.mazeDisplayer.solution = null;
            this.mazeDisplayer.setPlayerPosition(0,0);
            this.textField_mazeRows.setText("");
            this.textField_mazeColumns.setText("");
            this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
            if(this.mazeDisplayer.clickedCounter % 2 == 1)
            {
                this.mazeDisplayer.clickedCounter++;
                this.solveMaze.setText("Solve Maze");
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze successfully loaded");
            alert.show();
            this.mazeDisplayer.drawMaze(this.viewModel.getMaze().getMazeArr());
            this.solveMaze.setDisable(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void propertiesBar(ActionEvent actionEvent) throws IOException
    {
        Main.mainToProperties();
    }

    public void saveBar(ActionEvent actionEvent) throws IOException {
        if(this.viewModel.getMaze() == null)
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
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = true;
            return;
        }
        this.viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void playerMoved(int row, int col) throws IOException
    {
        this.mazeDisplayer.setPlayerPosition(row,col);
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
    }

    public void playerMovedF(int row, int col) throws IOException
    {
        this.mazeDisplayer.setPlayerPosition(row,col);
        this.mazeDisplayer.setPlayerPosition(0,0);
        Main.mainToSolved();
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
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

    public void ctrlReleased(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = false;
        }
    }

    public void update(Observable o, Object arg)
    {
        String change = (String) arg;
        try
        {
            switch (change)
            {
                case "Maze Generated" -> mazeGenerated();
                case "Player Moved" -> playerMoved(this.viewModel.getPlayerRow(), this.viewModel.getPlayerCol());
                case "Player MovedF" -> playerMovedF(this.viewModel.getPlayerRow(), this.viewModel.getPlayerCol());
                case "Maze Solved" -> mazeSolved();
                default -> System.out.println("Not implemented change: " + change);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent)
    {
        Platform.exit();
        System.exit(0);
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

    public void mouseDragged(MouseEvent mouseEvent) throws IOException
    {
        this.viewModel.mouseDragged(mouseEvent, this.mazeDisplayer);
    }
}
