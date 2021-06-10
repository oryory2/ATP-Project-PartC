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
        if(flag)
            goBack.setText("Go Back");
    }


    /**
     * going back to the game window from the user guide window
     * @param actionEvent pressing the "Go Back" button (ActionEvent)
     * @throw IOException
     */
    public void goBack(ActionEvent actionEvent) throws IOException
    {
        if(!flag)
        {
            flag = true;
            Main.realStart();
        }
        else
        {
            Main.backToMain();
        }
    }
}
