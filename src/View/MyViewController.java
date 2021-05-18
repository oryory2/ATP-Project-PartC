package View;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MyViewController implements IView
{
    private IMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;


    public void generateMaze(ActionEvent actionEvent)
    {
        if(this.generator == null)
            this.generator = new MyMazeGenerator();


        if((this.textField_mazeRows.getText() == "") || (this.textField_mazeColumns.getText() == ""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must fill in the row/column values");
            alert.show();
            return;
        }

        int row = Integer.valueOf(this.textField_mazeRows.getText());
        int col = Integer.valueOf(this.textField_mazeColumns.getText());

        try
        {
            Maze newMaze = this.generator.generate(row,col);

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

        //this.mazeDisplayer.drawMaze(maze);
    }


    public void solveMaze(ActionEvent actionEvent)
    {
    }
}
