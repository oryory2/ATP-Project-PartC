package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class MazeDisplayer extends Canvas
{

    public int[][] maze;
    public Solution solution;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private static int playerRow = 0;
    private static int playerCol = 0;
    public int clickedCounter;
    public boolean zoomFlag;


    /**
     * @return the player row field (int)
     */
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * @return the player column field (int)
     */
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * define the position of the player and present it to the user
     * @param row The row in the maze where the player will be (int)
     * @param col The column in the maze where the player will be (int)
     */
    public void setPlayerPosition(int row, int col)
    {
        playerRow = row;
        playerCol = col;
        draw(Main.primaryStage.getHeight() - 100 , Main.primaryStage.getWidth() - 150);
        if(this.solution != null)
        {
            this.drawSolution(this.solution);
        }
    }

    /**
     * set the image of the wall within the mazes
     * @param imageFileNameWall the name of the wall image file (String)
     */
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    /**
     * @return the imageFileNamePlayer field (StringProperty)
     */
    public StringProperty imageFileNamePlayerProperty() {
        return imageFileNamePlayer;
    }

    /**
     * set the image of the wall within the mazes
     * @param imageFileNamePlayer the name of the player image file (String)
     */
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    /**
     * @return the imageFileNameWall field (String)
     */
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }


    /**
     * @return the file name of the imageFileNamePlayer field (String)
     */
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }


    /**
     * A function that receives a maze and draws it to the screen
     * Use this function to redraw
     * uses other drawing functions
     * @param maze two-dimensional array that we will use to draw the maze (int[][])
     */
    public void drawMaze(int[][] maze)
    {
        this.maze = maze;
        this.zoomFlag = true;

        draw(Main.primaryStage.getHeight() - 100 , Main.primaryStage.getWidth() - 150);
        if(this.solution != null)
        {
            this.drawSolution(this.solution);
        }
    }

    /**
     * A function that clears the current displayed maze and his Solution if displayed
     */
    public void clear()
    {
        if(this.maze != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight);
            this.maze = null;
            this.solution = null;
        }
        else
        {
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0, 0, 0);
            this.maze = null;
            this.solution = null;
        }

    }
    /**
     * A function that receives a height and width
     * And draws on the screen a maze with these dimensions
     * In addition, the player and the walls will be drawn
     * @param height the height of the maze(double)
     * @param width the width of the maze(double)
     */
    public void draw(double height, double width)
    {
        if(maze != null)
        {
            if(this.zoomFlag) // check if we need to change the size of the mazeDisplayer window
            {
                this.setHeight(height - 50);
                this.setWidth(width - 50);
            }

            this.zoomFlag = false;

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
        }
    }

    /**
     * A function that draws the walls within a certain maze
     * @param graphicsContext class used to issue draw calls to a Canvas (GraphicsContext)
     * @param cellHeight the height of each cell within the maze (double)
     * @param cellWidth the width of each cell within the maze (double)
     * @param rows the amount of rows in the maze (int)
     * @param cols the amount of columns in the maze (int)
     */
    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.WHITE);

        Image wallImage = null;
        try
        {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if(maze[i][j] == 1)
                {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
        try
        {
            wallImage = new Image(new FileInputStream("resources/Images/Final.png"));
            graphicsContext.drawImage(wallImage, (cols - 1) * cellWidth, (rows - 1) * cellHeight, cellWidth, cellHeight);

        }
        catch (FileNotFoundException e)
        {
            System.out.println("There is no wall image file");
        }

    }

    /**
     * A function that draws the player within a certain maze
     * @param graphicsContext class used to issue draw calls to a Canvas (GraphicsContext)
     * @param cellHeight the height of each cell within the maze (double)
     * @param cellWidth the width of each cell within the maze (double)
     */
    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol()  * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.BLACK);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }


    /**
     * A function that draws a solution for the maze
     * The solution is drawn on the maze itself
     * @param sol the solution to be displayed (Solution)
     */
    public void drawSolution(Solution sol)
    {

        this.solution = sol;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.length;
        int cols = maze[0].length;

        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.setFill(Color.GREEN);


        ArrayList<AState> solutionPath = sol.getSolutionPath();

        for (int i = 0; i < solutionPath.size() - 1; i++)
        {
            Position p = (Position) solutionPath.get(i).getState();
            int thisRow = p.getRowIndex();
            int thisCol = p.getColumnIndex();

            if((this.getPlayerRow() == thisRow) && (this.getPlayerCol() == thisCol))
            {
                continue;
            }
            double x = thisCol * cellWidth;
            double y = thisRow * cellHeight;
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
    }

    /**
     * Pressing the Ctrl key and moving the mouse scroll will zoom in/out to the game board.
     * @param scrollEvent ctrl key + mouse scroll done by the user (ScrollEvent)
     */
    public void getOnScroll(ScrollEvent scrollEvent)
    {

        if (scrollEvent.getDeltaY() < 0) // zoom out
        {
            setHeight(getHeight() / 1.1);
            setWidth(getWidth() / 1.1);
        }
        else
        {
            if(getWidth()>6200 || getHeight()>6200)
            {
                return;
            }
            else // zoom in
            {
                setHeight(getHeight() * 1.1);
                setWidth(getWidth() * 1.1);
            }
        }
        draw(Main.primaryStage.getHeight() - 100 , Main.primaryStage.getWidth() - 150);
        if(this.solution != null)
        {
            this.drawSolution(this.solution);
        }
    }
}
