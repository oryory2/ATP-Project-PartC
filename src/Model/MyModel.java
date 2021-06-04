package Model;

import Server.Configurations;
import View.MyViewController;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.search.ISearchable;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel
{
    private Maze maze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    public Object[] properties;

    public MyModel()
    {
        Configurations c = Configurations.getInstance();
        this.properties = (Object[]) (c.LoadProp());
    }

    public void generateMaze(int row, int col)
    {
        IMazeGenerator generator = (IMazeGenerator) (this.properties[1]); // צור לקוח שמבקש ליצור מייז
        maze = generator.generate(row,col);
        playerRow = 0;
        playerCol = 0;
        solution = null;
        setChanged();
        notifyObservers("Maze Generated");
    }

    public Maze getMaze()
    {
        return maze;
    }

    public void solveMaze()
    {
        ISearchingAlgorithm searcher = (ISearchingAlgorithm) this.properties[2];
        ISearchable searchableMaze = new SearchableMaze(this.maze);
        solution = searcher.solve(searchableMaze);
        setChanged();
        notifyObservers("Maze Solved");
    }

    public Solution getSolution()
    {
        return solution;
    }

    public void updatePlayerLocation(MovementDirection direction)
    {
        int row = this.playerRow;
        int col = this.playerCol;

        switch (direction) {
            case UP : // up
                if(!(this.legalMove("up")))
                    return;
                row -= 1;
                break;

            case RIGHTU : // up right
                if(!(this.legalMove("up right")))
                    return;
                row -= 1 ;
                col += 1;
                break;

            case RIGHT : // right
                if(!(this.legalMove("right")))
                    return;
                col += 1;
                break;

            case RIGHTD : // down right
                if(!(this.legalMove("down right")))
                    return;
                row += 1 ;
                col += 1;
                break;

            case DOWN : // down
                if(!(this.legalMove("down")))
                    return;
                row += 1;
                break;

            case LEFTD : // down left
                if(!(this.legalMove("down left")))
                    return;
                row += 1 ;
                col -= 1;
                break;

            case LEFT : // left
                if(!(this.legalMove("left")))
                    return;
                col -= 1;
                break;

            case LEFTU : // up left
                if(!(this.legalMove("up left")))
                    return;
                row -= 1 ;
                col -= 1;
                break;

            default:
                return;
        }

        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("Player Moved");


    }


    public boolean legalMove(String direction)
    {
        int thisRow = this.playerRow;
        int thisCol = this.playerCol;

        if(direction.equals("up"))
        {
            if((thisRow - 1 < 0) || (this.maze.getMazeArr()[thisRow - 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("up right"))
        {
            if((thisRow - 1 < 0) || (thisCol + 1 >= this.maze.getMax_columns()) || (this.maze.getMazeArr()[thisRow - 1][thisCol + 1] == 1))
                return false;
            if((this.maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (this.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("right"))
        {
            if((thisCol + 1 >= this.maze.getMax_columns()) || (this.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down right"))
        {
            if((thisRow + 1 >= this.maze.getMax_rows()) || (thisCol + 1 >= this.maze.getMax_columns()) || (this.maze.getMazeArr()[thisRow + 1][thisCol + 1] == 1))
                return false;
            if((this.maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (this.maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down"))
        {
            if((thisRow + 1 >= this.maze.getMax_rows()) || (this.maze.getMazeArr()[thisRow + 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("down left"))
        {
            if((thisRow + 1 >= this.maze.getMax_rows()) || (thisCol - 1 < 0) || (this.maze.getMazeArr()[thisRow + 1][thisCol - 1] == 1))
                return false;
            if((this.maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (this.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("left"))
        {
            if((thisCol - 1 < 0) || (this.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("up left"))
        {
            if((thisRow - 1 < 0) || (thisCol - 1 < 0) || (this.maze.getMazeArr()[thisRow - 1][thisCol - 1] == 1))
                return false;
            if((this.maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (this.maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }

        return true;
    }



    public int getPlayerRow()
    {
        return playerRow;
    }

    public int getPlayerCol()
    {
        return playerCol;
    }

    public void assignObserver(Observer o)
    {
        this.addObserver(o);
    }
}
