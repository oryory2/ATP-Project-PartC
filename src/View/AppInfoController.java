package View;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class AppInfoController
{

    public void GoBack(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }

    public void PrimInfo(ActionEvent actionEvent)
    {
        try {
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Prim%27s_algorithm").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void DFSInfo(ActionEvent actionEvent)
    {
        try {
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Depth-first_search").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void BFSInfo(ActionEvent actionEvent)
    {
        try {
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Breadth-first_search").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void bestInfo(ActionEvent actionEvent)
    {
        try {
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Best-first_search").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }
}

