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
import javafx.scene.control.ScrollPane;
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


    /**
     * constructor
     * starting the maze-generating and maze-solving servers
     */
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


    /**
     * A function that uses the maze-generating server from Part B of the project
     * In order to give the user a maze when he requests one
     * notifies the observers when finished generating
     * @param row the number of rows that will be in the generated maze (int)
     * @param col the number of columns that will be in the generated maze (int)
     */
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

    /**
     * get the current maze that is being displayed
     * @return the maze field of this class (Maze)
     */
    public Maze getMaze()
    {
        return maze;
    }

    /**
     * set a certain maze as the current maze/maze field
     * @param maze the maze to set as the maze field of this class (Maze)
     */
    public void setMaze(Maze maze)
    {
        MyModel.maze = maze;
    }

    /**
     * A function that uses the maze-solving server from Part B of the project
     * In order to give the user a solution to the maze that is presented to him
     * when he requests one
     * notifies the observers when finished solving
     */
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


    /**
     * Getter of the solution field
     * @return the solution in the solution field (Solution)
     */
    public Solution getSolution()
    {
        return solution;
    }

    /**
     * Setter to the solution field
     * @param solution the solution to set to the solution field (Solution)
     */
    public void setSolution(Solution solution)
    {
        MyModel.solution = solution;
    }

    /**
     * updating the character's position when the user makes a move
     * notifies the observers when finished
     * @param direction The direction in which the user wants to move his character (MovementDirection)
     * @throws IOException
     */
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


    /**
     * A function designed to check whether a move made by the player is valid
     * @param direction The direction in which the user wants to move his character (String)
     * @return Was the move legal or not (boolean)
     */
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

    /**
     * restart the game
     */
    public void restart()
    {
        maze = null;
        solution = null;
        playerRow = 0;
        playerCol = 0;
    }


    /**
     * set the player's character in some position
     * @param row the row to set the player in (int)
     * @param col the column to set the player in (int)
     */
    public void setPlayerPosition(int row, int col)
    {
        playerRow = row;
        playerCol = col;
    }

    /**
     * Return the row in which the player is located
     * @return the Player row field (int)
     */
    public int getPlayerRow()
    {
        return playerRow;
    }

    /**
     * Return the column in which the player is located
     * @return the Player column field (int)
     */
    public int getPlayerCol()
    {
        return playerCol;
    }

    /**
     * A function used to move the character
     * according to the drag of the mouse by the user
     * @param mouseEvent the user dragging the mouse on screen (MouseEvent)
     * @param mazeDisplayer the maze displayer that displays the maze on screen (MazeDisplayer)
     * @param scrollPane the scroll pane that the MazeDisplayeris inside of (ScrollPane)
     * @throws IOException
     */
    public void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer, ScrollPane scrollPane) throws IOException {
        if (maze != null)
        {
            int row = maze.getMax_rows();
            int col = maze.getMax_columns();
            double canvasHeight = mazeDisplayer.getHeight();
            double canvasWidth = mazeDisplayer.getWidth();

            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;

            double scrollX = scrollPane.getHvalue();
            double scrollY = scrollPane.getVvalue();

            int updatedRow = canvasIndexRow(mouseEvent.getSceneY(), cellHeight, scrollY);
            int updatedCol = canvasIndexCol(mouseEvent.getSceneX(), cellWidth, scrollX);

            if(updatedRow == -2 || updatedCol == -2)
            {
                if(easyMode)
                    return;
                else
                    Main.mainToLost();
            }

            if(updatedRow == -1 || updatedCol == -1)
                return;

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


    /**
     * function used to find where is the player in relation to the maze vertically
     * @param SceneY the entire scene width height (double)
     * @param cellHeight height of each cell within the maze (double)
     * @param scrollY how much did the user scroll vertically (double)
     * @return the row where the player in relation to the maze vertically
     */
    public int canvasIndexRow(double SceneY, double cellHeight, double scrollY)
    {
        if(SceneY < 32.6)
            return -2;

        double start = this.getPlayerRow() * cellHeight + 32.6; // 32
        double end = (this.getPlayerRow() * cellHeight) + cellHeight + 32.6;

        double smallerBound = start - cellHeight;
        double biggerBound = end + cellHeight;

        if(SceneY >= end && SceneY < biggerBound)
            return this.getPlayerRow() + 1;
        if(SceneY < start && SceneY >= smallerBound)
            return this.getPlayerRow() - 1;
        if(SceneY >= start && SceneY < end)
            return this.getPlayerRow();
        return -1;
    }

    /**
     * function used to find where is the player in relation to the maze horizontally
     * @param SceneX the entire scene width (double)
     * @param cellWidth width of each cell within the maze (double)
     * @param scrollX how much did the user scroll horizontally (double)
     * @return the column where the player in relation to the maze horizontally
     */
    public int canvasIndexCol(double SceneX, double cellWidth, double scrollX)
    {
        if(SceneX < 176.54)
            return -2;

        double start = this.getPlayerCol() * cellWidth + 176.54; // 176
        double end = (this.getPlayerCol() * cellWidth) + cellWidth + 176.54;

        double leftBound = start - cellWidth;
        double rightBound = end + cellWidth;

        if(SceneX >= end && SceneX < rightBound)
            return this.getPlayerCol() + 1;
        if(SceneX < start && SceneX >= leftBound)
            return this.getPlayerCol() - 1;
        if(SceneX >= start && SceneX < end)
            return this.getPlayerCol();
        return -1;
    }

    /**
     * assign an observer to this observable
     * @param o the observer to assign to this observable (observer)
     */
    public void assignObserver(Observer o)
    {
        this.addObserver(o);
    }
}
