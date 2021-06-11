package Model;

import IO.MyDecompressorInputStream;
import Server.Server;
import View.Main;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import Client.*;
import Server.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final Logger logger = LogManager.getLogger();

    /**
     * Constructor
     * Starting the maze-generating and maze-solving servers (if not started yet)
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
     * In order to generate a maze when needed
     * notifies the observers when finished generating
     * @param row the number of rows that will be in the generated maze (int)
     * @param col the number of columns that will be in the generated maze (int)
     */
    public void generateMaze(int row, int col)
    {
        try {
            Client clientMazeGenerator = new Client(InetAddress.getLocalHost(), 5400,
                    (inFromServer, outToServer) -> {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};

                            logger.info("Client: " + InetAddress.getLocalHost() + " requesting a new Maze of size: (" + row +"," + col + ")");

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
                            logger.error("Exception", e);
                        }
                    });
            clientMazeGenerator.communicateWithServer();
        }
        catch (Exception e)
        {
            logger.error("Exception", e);
        }
        playerRow = 0;
        playerCol = 0;
        solution = null;
        setChanged();
        notifyObservers("Maze Generated");
    }

    /**
     * Gets the current maze that is being displayed
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
     * when he requests it
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

                            logger.info("Client:" + InetAddress.getLocalHost() + " requesting for a Solution");

                            toServer.writeObject(maze);
                            toServer.flush();
                            solution = (Solution) fromServer.readObject();
                        }
                        catch(Exception e)
                        {
                            logger.error("Exception", e);
                        }
                    });
            clientSolveMaze.communicateWithServer();
        }
        catch(Exception e)
        {
            logger.error("Exception", e);
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
     * @throw IOException
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
            logger.info("Client: " + InetAddress.getLocalHost() + " solved the Maze");

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
     * @throw IOException
     */
    public void mouseDragged(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer, ScrollPane scrollPane) throws IOException
    {
        if (maze != null)
        {
            double canvasHeight = mazeDisplayer.getHeight();
            double canvasWidth = mazeDisplayer.getWidth();

            int rows = maze.getMax_rows();
            int cols = maze.getMax_columns();

            double mouseRowPosition = (mouseEvent.getY());
            double mouseColPosition = (mouseEvent.getX());

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            double currentRow = mouseRowPosition / cellHeight;
            double currentCol = mouseColPosition / cellWidth;

            boolean up = false;
            boolean right = false;
            boolean down = false;
            boolean left = false;




            if((playerRow < currentRow) && (playerRow + 1 > currentRow) && (playerCol < currentCol) && (playerCol + 1 > currentCol))
                return; // player didn't move

            if ((playerRow > currentRow) && (currentRow >= playerRow - 2) && (playerCol + 1 >= currentCol) && (playerCol <= currentCol))
            {
                if (legalMove("up"))
                    up = true; // player moved up
            }

            if ((playerCol + 1 < currentCol) && (currentCol <= playerCol + 2) && (playerRow + 1 >= currentRow) && (playerRow <= currentRow))
            {
                if (legalMove("right"))
                    right = true; // player moved right

            }

            if ((playerRow < currentRow) && (currentRow <= playerRow + 2) && (playerCol + 1 >= currentCol) && (playerCol <= currentCol))
            {
                if (legalMove("down"))
                    down = true; // player moved down
            }

            if ((playerCol > currentCol) && (currentCol >= playerCol - 1) && (playerRow + 1 >= currentRow) && (playerRow <= currentRow))
            {
                if (legalMove("left"))
                    left = true; // player moved left
            }


            if(up && right)
                updatePlayerLocation(MovementDirection.RIGHTU);
            else if (up && left)
                updatePlayerLocation(MovementDirection.LEFTU);
            else if (down && right)
                updatePlayerLocation(MovementDirection.RIGHTD);
            else if (down && left)
                updatePlayerLocation(MovementDirection.LEFTD);
            else if (up)
                updatePlayerLocation(MovementDirection.UP);
            else if (right)
                updatePlayerLocation(MovementDirection.RIGHT);
            else if (down)
                updatePlayerLocation(MovementDirection.DOWN);
            else if (left)
                updatePlayerLocation(MovementDirection.LEFT);
        }
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
