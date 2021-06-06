package Model;

import IO.MyDecompressorInputStream;
import Server.Configurations;
import Server.Server;
import View.Main;
import View.MazeDisplayer;
import View.MyViewController;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.search.ISearchable;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import Client.*;
import Server.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel
{
    public static Maze maze;
    public static Solution solution;
    public static int playerRow;
    public static int playerCol;
    public static Server mazeGeneratingServer;
    public static Server solveSearchProblemServer;
    public static boolean easyMode = true;


    public MyModel()
    {
        if(mazeGeneratingServer == null)
        {
            mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
            mazeGeneratingServer.start();
        }
        if(solveSearchProblemServer == null)
        {
            solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
            solveSearchProblemServer.start();
        }
    }

    public void generateMaze(int row, int col) {

        try {
            Client clientMazeGenerator = new Client(InetAddress.getLocalHost(), 5400,
                    (inFromServer, outToServer) -> {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};
                            toServer.writeObject(mazeDimensions);
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject();
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[(row * col) + 12];
                            is.read(decompressedMaze);
                            maze = new Maze(decompressedMaze);
                            toServer.close();
                            fromServer.close();
                        } catch (Exception e)
                        {
                           e.printStackTrace();
                        }
                    });
            clientMazeGenerator.communicateWithServer();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        try {
            Client clientSolveMaze = new Client(InetAddress.getLocalHost(), 5401,
                    (inFromServer, outToServer) -> {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            toServer.writeObject(maze);
                            toServer.flush();
                            solution = (Solution) fromServer.readObject();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    });
            clientSolveMaze.communicateWithServer();
        }
        catch(Exception ignored)
        {
            ignored.printStackTrace();
        }
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

    public void updatePlayerLocation(MovementDirection direction) throws IOException
    {
        int row = playerRow;
        int col = playerCol;

        switch (direction) {
            case UP : // up
                if(!(this.legalMove("up")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                row -= 1;
                break;

            case RIGHTU : // up right
                if(!(this.legalMove("up right")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                row -= 1 ;
                col += 1;
                break;

            case RIGHT : // right
                if(!(this.legalMove("right")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                col += 1;
                break;

            case RIGHTD : // down right
                if(!(this.legalMove("down right")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                row += 1 ;
                col += 1;
                break;

            case DOWN : // down
                if(!(this.legalMove("down")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                row += 1;
                break;

            case LEFTD : // down left
                if(!(this.legalMove("down left")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                row += 1 ;
                col -= 1;
                break;

            case LEFT : // left
                if(!(this.legalMove("left")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
                col -= 1;
                break;

            case LEFTU : // up left
                if(!(this.legalMove("up left")))
                {
                    if(easyMode)
                        return;
                    else
                        Main.mainToLost();
                }
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

    public void restart()
    {
        maze = null;
        solution = null;
        playerRow = 0;
        playerCol = 0;
    }


    public void setPlayerPosition(int row, int col)
    {
        playerRow = row;
        playerCol = col;
    }

    public int getPlayerRow()
    {
        return playerRow;
    }

    public int getPlayerCol()
    {
        return playerCol;
    }

    public void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer) throws IOException {
        if (maze != null)
        {
            int row = maze.getMax_rows();
            int col = maze.getMax_columns();
            double canvasHeight = mazeDisplayer.getHeight();
            double canvasWidth = mazeDisplayer.getWidth();

            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;

            int updatedRow = canvasIndexRow(mouseEvent.getSceneY(), cellHeight);
            int updatedCol = canvasIndexCol(mouseEvent.getSceneX(), cellWidth);

            if (!((updatedRow == this.getPlayerRow()) && (updatedCol == this.getPlayerCol())))
            {
                if((updatedRow == this.getPlayerRow() - 1) && (updatedCol == this.getPlayerCol()))
                {
                    if(!(legalMove("up")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() - 1, this.getPlayerCol());
                }
                else if ((updatedRow == this.getPlayerRow() - 1) && (updatedCol == this.getPlayerCol() + 1))
                {
                    if(!(legalMove("up right")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() - 1, this.getPlayerCol() + 1);
                }
                else if ((updatedRow == this.getPlayerRow()) && (updatedCol == this.getPlayerCol() + 1))
                {
                    if(!(legalMove("right")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow(), this.getPlayerCol() + 1);
                }
                else if ((updatedRow == this.getPlayerRow() + 1) && (updatedCol == this.getPlayerCol() + 1))
                {
                    if(!(legalMove("down right")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() + 1, this.getPlayerCol() + 1);
                }
                else if ((updatedRow == this.getPlayerRow() + 1) && (updatedCol == this.getPlayerCol()))
                {
                    if(!(legalMove("down")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() + 1, this.getPlayerCol());
                }
                else if ((updatedRow == this.getPlayerRow() + 1) && (updatedCol == this.getPlayerCol() - 1))
                {
                    if(!(legalMove("down left")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() + 1, this.getPlayerCol() - 1);
                }
                else if ((updatedRow == this.getPlayerRow()) && (updatedCol == this.getPlayerCol() - 1))
                {
                    if(!(legalMove("left")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow(), this.getPlayerCol() - 1);
                }
                else if ((updatedRow == this.getPlayerRow() - 1) && (updatedCol == this.getPlayerCol() - 1))
                {
                    if(!(legalMove("up left")))
                    {
                        if(easyMode)
                            return;
                        else
                            Main.mainToLost();
                    }
                    setPlayerPosition(this.getPlayerRow() - 1, this.getPlayerCol() - 1);
                }
            }
            setChanged();
            if((this.getPlayerRow() == this.getMaze().getMax_rows() - 1) && (this.getPlayerCol() == this.getMaze().getMax_columns() - 1))
            {
                notifyObservers("Player MovedF");
                maze = null;
                solution = null;
                playerCol = 0;
                playerRow = 0;
            }
            notifyObservers("Player Moved");
        }
    }

    public int canvasIndexRow(double SceneY, double cellHeight)
    {
        double start = this.getPlayerRow() * cellHeight + 32; // 32
        double end = (this.getPlayerRow() * cellHeight) + cellHeight + 32;
        if(SceneY >= end)
            return this.getPlayerRow() + 1;
        if(SceneY < start)
            return this.getPlayerRow() - 1;
        return this.getPlayerRow();
    }

    public int canvasIndexCol(double SceneX, double cellWidth)
    {
        double start = this.getPlayerCol() * cellWidth + 176; // 176
        double end = (this.getPlayerCol() * cellWidth) + cellWidth + 176;
        if(SceneX >= end)
            return this.getPlayerCol() + 1;
        if(SceneX < start)
            return this.getPlayerCol() - 1;
        return this.getPlayerCol();
    }

    public void assignObserver(Observer o)
    {
        this.addObserver(o);
    }
}
