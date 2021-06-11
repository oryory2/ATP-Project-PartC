package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements IView, Observer
{

    public MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Label thisPose;
    public Button solveMaze;
    public BorderPane BigBorderPane;
    public javafx.scene.control.ScrollPane scrollPane;
    private boolean ctrlFlag;


    /**
     * constructor
     * Initializing the maze displayer
     */
    public MyViewController()
    {
        this.mazeDisplayer = new MazeDisplayer();
    }

    /**
     * Function that setting this class as an observer of the viewModel
     * draw the maze/solution if exist
     * attach the size of the display game as the size of the window application
     * @param viewModel the observer of this View (viewModel)
     */
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
            //disable the option to solve the maze because no maze has been generated yet
            this.solveMaze.setDisable(true);
        }
        Main.primaryStage.widthProperty().addListener(gg -> {reSizeWindow(); }); // add listener to the window widthProperty
        Main.primaryStage.heightProperty().addListener(gg -> {reSizeWindow(); }); // add listener to the window heightProperty
    }

    /**
     * resize the window when the width or height property has been changed
     */
    private void reSizeWindow()
    {
        this.mazeDisplayer.zoomFlag = true;
        mazeDisplayer.draw(Main.primaryStage.getHeight() - 17 , Main.primaryStage.getWidth() - 140);

        if(this.viewModel.getSolution() != null)
        {
            this.mazeDisplayer.drawSolution(this.viewModel.getSolution());
        }
    }

    /**
     * generating a new maze, and display it on the screen application
     * @param actionEvent pressing on the create new Maze Button (ActionEvent)
     */
    public void generateMaze(ActionEvent actionEvent)
    {
        //Check that rows and columns have been inserted by the user
        if((this.textField_mazeRows.getText().equals("")) || (this.textField_mazeColumns.getText().equals("")))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must fill in the row/column values");
            alert.show();
            this.textField_mazeRows.setText("");
            this.textField_mazeColumns.setText("");
            return;
        }
        //Check that the user has inserted numbers and numbers only to the rows and columns
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

        //check that the maze dimensions are greater than 2
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
            this.viewModel.generateMaze(row, col); // creating the maze
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * after a maze has been generated draw the maze
     * set the player position to 0,0
     * and enable the option of solving the maze
     */
    public void mazeGenerated()
    {
        this.mazeDisplayer.setPlayerPosition(0,0);
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
        this.mazeDisplayer.solution = null;
        this.mazeDisplayer.drawMaze(this.viewModel.getMaze().getMazeArr());
        this.solveMaze.setText("Solve Maze");
        this.mazeDisplayer.clickedCounter = 0;
        this.solveMaze.setDisable(false);
        this.scrollPane.setPannable(false);
    }

    /**
     * solve the maze displayed on screen
     * @param actionEvent user pressing the "Solve Maze" Button (ActionEvent)
     */
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
            this.mazeDisplayer.clickedCounter++; // counter the times the solve button has been clicked by the user

            if(this.mazeDisplayer.clickedCounter % 2 == 1) // Solve
            {
                if(this.mazeDisplayer.solution != null) // there is already existing solution
                {
                    this.mazeDisplayer.drawSolution(this.mazeDisplayer.solution);
                    return;
                }
                this.viewModel.solveMaze(); // solve the maze
            }
            else // UnSolve
            {
                this.mazeDisplayer.solution = null;
                this.viewModel.setSolution(null);
                mazeDisplayer.draw(Main.primaryStage.getHeight() - 17 , Main.primaryStage.getWidth() - 140);
                this.solveMaze.setText("Solve Maze");
            }
        }
    }

    /**
     * draw the solution of the maze on the screen after the user asked for it
     * change the "solve maze" button to "unsolve maze"
     */
    public void mazeSolved()
    {
        this.mazeDisplayer.drawSolution(this.viewModel.getSolution());
        this.solveMaze.setText("Unsolve Maze");
    }

    /**
     * start a new game
     * clear the maze/solution from the screen if needed
     * @param actionEvent pressing on the "New" Menu Item within THE "File" menu (ActionEvent)
     */
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

    /**
     * Load a saved maze
     * clear the current maze/solution from the screen if needed
     * display the loaded maze on the screen
     * and allow the user to play with the new maze
     * @param actionEvent pressing on the "Load" Menu Item within THE "File" menu (ActionEvent)
     */
    public void loadBar(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Maze");
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

    /**
     * Move from the Game window to the properties window
     * @param actionEvent pressing on the "Properties" Menu Item within the "Options" menu (ActionEvent)
     * @throw IOException
     */
    public void propertiesBar(ActionEvent actionEvent) throws IOException
    {
        Main.mainToProperties();
    }

    /**
     * Save the current maze to a file
     * @param actionEvent pressing on the "Save" Menu Item within the "File" menu (ActionEvent)
     */
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

    /**
     * Move from the Game window to the User Guide window
     * @param actionEvent pressing on the "User Guide" Menu Item within the "Help" menu (ActionEvent)
     * @throw IOException
     */
    public void userGuide(ActionEvent actionEvent) throws IOException
    {
        Main.mainToUserGuide();
    }

    /**
     * Move from the Game window to the Application Info window
     * @param actionEvent pressing on the "Application Info" Menu Item within the "About" menu (ActionEvent)
     * @throw IOException
     */
    public void AppInfo(ActionEvent actionEvent) throws IOException
    {
        Main.mainToAppInfo();
    }


    /**
     * Function that responsible to check if the user want to move his player in the app
     * also, check if ctrl is pressed (for the zoom function)
     * @param keyEvent the key that has been pressed in the user keyboard
     * @throw IOException
     */
    public void keyPressed(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = true;
            return;
        }
        this.viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    /**
     * when the user moves the player the function will update its position if needed
     * @param row the row of the new position of the player (int)
     * @param col the column of the new position of the player (int)
     * @throw IOException
     */
    public void playerMoved(int row, int col) throws IOException
    {
        this.mazeDisplayer.setPlayerPosition(row,col);
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
    }

    /**
     * Function that runs when the user solved the maze (restart all the Position values)
     * @param row (int)
     * @param col (int)
     * @throw IOException
     */
    public void playerMovedF(int row, int col) throws IOException
    {
        this.mazeDisplayer.setPlayerPosition(row,col);
        this.mazeDisplayer.setPlayerPosition(0,0);
        Main.mainToSolved();
        this.thisPose.setText("Current Position : (" + mazeDisplayer.getPlayerRow() + "," + mazeDisplayer.getPlayerCol() + ")");
    }

    /**
     * Requests that this Node will get the input focus,
     * and that this Node's top-level ancestor become the focused window.
     * @param mouseEvent clicking on the maze (MouseEvent)
     */
    public void mouseClicked(MouseEvent mouseEvent)
    {
        this.mazeDisplayer.requestFocus();
    }

    public void scrollMouse(ScrollEvent scrollEvent)
    {
        if(this.ctrlFlag){
            this.mazeDisplayer.getOnScroll(scrollEvent);
        }

    }

    /**
     * A function that updates that the control key has been released
     * @param keyEvent user releasing the ctrl button (KeyEvent)
     */
    public void ctrlReleased(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.CONTROL)
        {
            this.ctrlFlag = false;
        }
    }

    /**
     * update this class
     * according to what the observable of this class has notified that happened
     * @param o an observable of this class (Observable)
     * @param arg the thing that the observable has notified (Object)
     */
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

    /**
     * exit the app
     * @param actionEvent pressing on the exit bar menu item exitApp (ActionEvent)
     */
    public void exit(ActionEvent actionEvent)
    {
        Platform.exit();
        System.exit(0);
    }

    /**
     * a function that gets two strings
     * And checks if the content of each string is a number
     * @param row The dimension of the rows to be checked (String)
     * @param col The dimension of the columns to be checked (String)
     * @return whether these are numbers (boolean)
     */
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

    /**
     * a function that moves the player using mouse dragging
     * @param mouseEvent the Position of the mouse after the dragging by the user (MouseEvent)
     * @throw IOException
     */
    public void mouseDragged(MouseEvent mouseEvent) throws IOException
    {
        this.viewModel.mouseDragged(mouseEvent, this.mazeDisplayer, this.scrollPane);
    }
}
