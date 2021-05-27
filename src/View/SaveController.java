package View;

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


    public void confirm(ActionEvent actionEvent) throws IOException
    {
        if(this.fileName.getText() == "")
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must first insert a fileName!");
            alert.show();
            return;
        }
        try
        {
            String fileName = "resources\\SavedMaze\\"  + this.fileName.getText() + ".maze";
            FileOutputStream out = new FileOutputStream(fileName);
            ObjectOutputStream output = new ObjectOutputStream(out);
            output.writeObject(MyViewController.maze);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Main.backToMain();

    }

    public void cancel(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }
}
