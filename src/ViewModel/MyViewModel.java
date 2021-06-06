package ViewModel;

import Model.IModel;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.util.Observable;
import java.util.Observer;
import Model.MovementDirection;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class MyViewModel extends Observable implements Observer
{
    private IModel model;


    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }

    public void update(Observable o, Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int row, int col)
    {
        this.model.generateMaze(row, col);
    }

    public void solveMaze()
    {
        model.solveMaze();
    }

    public Maze getMaze()
    {
        return this.model.getMaze();
    }

    public void setMaze(Maze maze)
    {
        this.model.setMaze(maze);
    }

    public Solution getSolution()
    {
        return model.getSolution();
    }

    public void setSolution(Solution solution)
    {
        this.model.setSolution(solution);
    }

    public void movePlayer(KeyEvent keyEvent)
    {
        MovementDirection direction;
        switch (keyEvent.getCode()){
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

    public int getPlayerRow()
    {
        return model.getPlayerRow();
    }

    public int getPlayerCol()
    {
        return model.getPlayerCol();
    }

    public void setPlayerPosition(int row, int col)
    {
        this.model.setPlayerPosition(0,0);
    }

    public void restart()
    {
        this.model.restart();
    }

    public void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer)
    {
        this.model.mouseDragged(mouseEvent, mazeDisplayer);
    }
}
