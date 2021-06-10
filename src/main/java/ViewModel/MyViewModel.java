package ViewModel;

import Model.IModel;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import Model.MovementDirection;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class MyViewModel extends Observable implements Observer
{
    private IModel model;


    /**
     * constructor
     * @param model the model field(observable) of this class in this MVVM formation (IModel)
     */
    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }

    /**
     * update an observer on a certain thing that happened
     * @param o the observable of this class that needs to be updated (Observable)
     * @param arg The thing that happened that needs to be updated (Object)
     */
    public void update(Observable o, Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * generates the maze according to user's request
     * send it to this class' model field for implementation
     * @param row the number of rows that will be in the generated maze (int)
     * @param col the number of columns that will be in the generated maze (int)
     */
    public void generateMaze(int row, int col)
    {
        this.model.generateMaze(row, col);
    }

    /**
     * solve the maze according to user's request
     * send it to this class' model field for implementation
     */
    public void solveMaze()
    {
        model.solveMaze();
    }

    /**
     * get the current maze that is being displayed
     * @return the maze field of this class' model field (Maze)
     */
    public Maze getMaze()
    {
        return this.model.getMaze();
    }

    /**
     * set a certain maze as the current maze/maze field of this class' model field
     * @param maze the maze to set as the maze field of this class (Maze)
     */
    public void setMaze(Maze maze)
    {
        this.model.setMaze(maze);
    }

    /**
     * Getter of the solution field of this class' model field
     * @return the solution in the solution field (Solution)
     */
    public Solution getSolution()
    {
        return model.getSolution();
    }

    /**
     * Setter to the solution field of this class' model field
     * @param solution the solution to set to the solution field (Solution)
     */
    public void setSolution(Solution solution)
    {
        this.model.setSolution(solution);
    }

    /**
     * a function used to move the user's character/player within the maze
     * converts the NUMPAD keys into movement directions
     * @param keyEvent the user pressing on of the NUMPAD keys other than NUMPAD5 (KeyEvent)
     * @throw IOException
     */
    public void movePlayer(KeyEvent keyEvent) throws IOException {
        MovementDirection direction;
        switch (keyEvent.getCode())
        {
            case NUMPAD8 -> direction = MovementDirection.UP;
            case NUMPAD9 -> direction = MovementDirection.RIGHTU;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD3 -> direction = MovementDirection.RIGHTD;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case NUMPAD1 -> direction = MovementDirection.LEFTD;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD7 -> direction = MovementDirection.LEFTU;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    /**
     * Return the row in which the player is located
     * @return the Player row field of this class' model field (int)
     */
    public int getPlayerRow()
    {
        return model.getPlayerRow();
    }

    /**
     * Return the column in which the player is located
     * @return the Player column field of this class' model field (int)
     */
    public int getPlayerCol()
    {
        return model.getPlayerCol();
    }

    /**
     * set the player's character in a position
     * @param row the row to set the player in (int)
     * @param col the column to set the player in (int)
     */
    public void setPlayerPosition(int row, int col)
    {
        this.model.setPlayerPosition(0,0);
    }

    /**
     * restart the game
     */
    public void restart()
    {
        this.model.restart();
    }

    /**
     * A function used to move the character
     * according to the drag of the mouse by the user
     * sends it to this class' model field for implementation
     * @param mouseEvent the user dragging the mouse on screen (MouseEvent)
     * @param mazeDisplayer the maze displayer that displays the maze on screen (MazeDisplayer)
     * @param scrollPane the scroll pane that the MazeDisplayeris inside of (ScrollPane)
     * @throw IOException
     */
    public void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer, ScrollPane scrollPane) throws IOException
    {
        this.model.mouseDragged(mouseEvent, mazeDisplayer, scrollPane);
    }
}
