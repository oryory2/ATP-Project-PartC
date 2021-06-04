package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.util.Observable;
import java.util.Observer;
import Model.MovementDirection;
import javafx.scene.input.KeyEvent;


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

    public Solution getSolution()
    {
        return model.getSolution();
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

}
