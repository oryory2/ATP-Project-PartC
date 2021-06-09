package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveController {
    public TextField fileName;
    public Button confirm;
    public Button cancel;


    /**
     * confirming saving the maze into a file
     * @param actionEvent actionEvent pressing the "Confrim" button (ActionEvent)
     * @throws IOException
     */
    public void confirm(ActionEvent actionEvent) throws IOException
    {
        if(this.fileName.getText() == "")
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //user will enter the filename to save the maze to
            alert.setContentText("You must first insert a fileName!");
            alert.show();
            return;
        }
        try
        {
            String fileName = "resources\\SavedMaze\\"  + this.fileName.getText() + ".maze";
            FileOutputStream out = new FileOutputStream(fileName);
            ObjectOutputStream output = new ObjectOutputStream(out);
            output.writeObject(Main.model.getMaze());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Maze successfully saved");
        alert.show();

        Main.backToMain();

    }

    /**
     * cancel the maze saving and go back to the maze
     * @param actionEvent pressing the "Cancel" button (ActionEvent)
     * @throws IOException
     */
    public void cancel(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }
}
