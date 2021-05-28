package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas
{
    private int[][] maze;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private int playerRow = 0;
    private int playerCol = 0;

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col)
    {
        this.playerRow = row;
        this.playerCol = col;
        draw();
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
        draw();
    }

    public void clear()
    {
        if(this.maze != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight);
        }
        else
        {
            GraphicsContext graphicsContext = getGraphicsContext2D(); // מחלקה שבעזרתה ניתן לצייר על הקנבס
            graphicsContext.clearRect(0,0, 0, 0); // ניקוי הקנבס (במקרה שכבר ציירנו עליו מבוך)
        }

    }
    private void draw()
    {
        if(maze != null)
        {
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
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol()  * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

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
}
