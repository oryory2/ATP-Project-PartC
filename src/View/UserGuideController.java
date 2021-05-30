package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class UserGuideController
{
    private static boolean flag = false;
    public Button goBack;

    public void initialize()
    {
        if(flag == true)
            goBack.setText("Go Back");
    }


    public void goBack(ActionEvent actionEvent) throws IOException
    {
        if(flag == false)
        {
            flag = true;
            Main.realStart();
            return;
        }
        else
        {
            Main.backToMain();
        }
    }
}
