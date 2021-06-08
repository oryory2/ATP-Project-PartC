package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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



    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

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


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public StringProperty imageFileNamePlayerProperty() {
        return imageFileNamePlayer;
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

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

    public void clear()
    {
        if(this.maze != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();

            GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight);
            this.maze = null;
            this.solution = null;
        }
        else
        {
            GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
            graphicsContext.clearRect(0,0, 0, 0); // ניקוי הקנבס (במקרה שכבר ציירנו עליו מבוך)
            this.maze = null;
            this.solution = null;
        }

    }
    public void draw(double height, double width)
    {
        if(maze != null)
        {
            if(this.zoomFlag)
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

            GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight); // ניקוי הקנבס (במקרה שכבר ציירנו עליו מבוך)
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
        }
    }


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


    public void drawSolution(Solution sol)
    {

        this.solution = sol;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.length;
        int cols = maze[0].length;

        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
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

    public void getOnScroll(ScrollEvent scrollEvent)
    {

        if (scrollEvent.getDeltaY() < 0)
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
            else
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
