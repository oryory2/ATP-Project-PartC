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
    public static Maze maze;
    public static Solution solution;
    public static int playerRow;
    public static int playerCol;
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

    public void setMaze(Maze maze)
    {
        MyModel.maze = maze;
    }

    public void solveMaze()
    {
        ISearchingAlgorithm searcher = (ISearchingAlgorithm) this.properties[2];
        ISearchable searchableMaze = new SearchableMaze(maze);
        solution = searcher.solve(searchableMaze);
        setChanged();
        notifyObservers("Maze Solved");
    }

    public Solution getSolution()
    {
        return solution;
    }

    public void setSolution(Solution solution)
    {
        MyModel.solution = solution;
    }

    public void updatePlayerLocation(MovementDirection direction)
    {
        int row = playerRow;
        int col = playerCol;

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

        playerRow = row;
        playerCol = col;

        setChanged();
        if((row == this.getMaze().getMax_rows() - 1) && (col == this.getMaze().getMax_columns() - 1))
        {
            notifyObservers("Player MovedF");
            maze = null;
            solution = null;
            playerCol = 0;
            playerRow = 0;
            return;
        }
        notifyObservers("Player Moved");
    }


    public boolean legalMove(String direction)
    {
        int thisRow = playerRow;
        int thisCol = playerCol;

        if(direction.equals("up"))
        {
            if((thisRow - 1 < 0) || (maze.getMazeArr()[thisRow - 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("up right"))
        {
            if((thisRow - 1 < 0) || (thisCol + 1 >= maze.getMax_columns()) || (maze.getMazeArr()[thisRow - 1][thisCol + 1] == 1))
                return false;
            if((maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("right"))
        {
            if((thisCol + 1 >= maze.getMax_columns()) || (maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down right"))
        {
            if((thisRow + 1 >= maze.getMax_rows()) || (thisCol + 1 >= maze.getMax_columns()) || (maze.getMazeArr()[thisRow + 1][thisCol + 1] == 1))
                return false;
            if((maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (maze.getMazeArr()[thisRow][thisCol + 1] == 1))
                return false;
        }
        else if (direction.equals("down"))
        {
            if((thisRow + 1 >= maze.getMax_rows()) || (maze.getMazeArr()[thisRow + 1][thisCol] == 1))
                return false;
        }
        else if (direction.equals("down left"))
        {
            if((thisRow + 1 >= maze.getMax_rows()) || (thisCol - 1 < 0) || (maze.getMazeArr()[thisRow + 1][thisCol - 1] == 1))
                return false;
            if((maze.getMazeArr()[thisRow + 1][thisCol] == 1) && (maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("left"))
        {
            if((thisCol - 1 < 0) || (maze.getMazeArr()[thisRow][thisCol - 1] == 1))
                return false;
        }
        else if (direction.equals("up left"))
        {
            if((thisRow - 1 < 0) || (thisCol - 1 < 0) || (maze.getMazeArr()[thisRow - 1][thisCol - 1] == 1))
                return false;
            if((maze.getMazeArr()[thisRow - 1][thisCol] == 1) && (maze.getMazeArr()[thisRow][thisCol - 1] == 1))
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
