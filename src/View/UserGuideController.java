package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class UserGuideController
{
    private static boolean flag = false;
    public Button goBack;

    /**
     * starting the user guide window after coming from the game
     */
    public void initialize()
    {
        if(flag == true)
            goBack.setText("Go Back");
    }


    /**
     * going back to the game window from the user guide window
     * @param actionEvent pressing the "Go Back" button (ActionEvent)
     * @throws IOException
     */
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
