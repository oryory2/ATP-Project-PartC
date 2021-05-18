package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

public class MazeDisplayer extends Canvas
{
    private int[][] maze;

    public void drawMaze(int[][] maze)
    {
        this.maze = maze;
        draw();
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
            graphicsContext.setFill(Color.RED); // מילוי כל הקנבס בצבע אדום (

            for(int i = 0; i < rows; i++)
            {
                for(int j = 0; j < cols; j++)
                {
                    if(maze[i][j] == 1) // אם הוא 1, אז אנו רוצים לצבוע אותו בלבן
                    {
                        double x = j * cellWidth;
                        double y = i * cellHeight;
                        graphicsContext.fillRect(x,y, cellWidth, cellHeight);
                    }
                }
            }
        }
    }
}
