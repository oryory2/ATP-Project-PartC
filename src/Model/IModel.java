package Model;

import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Observer;

public interface IModel
{
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void setMaze(Maze maze);
    void solveMaze();
    Solution getSolution();
    void setSolution(Solution solution);
    void updatePlayerLocation(MovementDirection direction) throws IOException;
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void restart();
    void setPlayerPosition(int row, int col);
    void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer) throws IOException;
}
