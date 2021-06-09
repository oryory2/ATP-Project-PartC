package View;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class AppInfoController
{

    /**
     * returns to the main window after pressing the goBack button
     * @param actionEvent pressing the goBack (ActionEvent)
     */
    public void GoBack(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }

    /**
     * sends the user to the Wikipedia site after clicking on the white circle next to MyMazeGenerator
     * The link is an explanation of the Prim algorithm used to create the mazes
     * @param actionEvent pressing the white circle next to MyMazeGenerator (ActionEvent)
     */
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

    /**
     * sends the user to the Wikipedia site after clicking on the white circle next to DepthFirstSearch
     * The link is an information about the DFS algorithm that can be used to solve the maze
     * @param actionEvent pressing the white circle next to DepthFirstSearch (ActionEvent)
     */
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

    /**
     * sends the user to the Wikipedia site after clicking on the white circle next to BreadthFirstSearch
     * The link is an information about the BFS algorithm that can be used to solve the maze
     * @param actionEvent pressing the white circle next to BreadthFirstSearch (ActionEvent)
     */
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

    /**
     * sends the user to the Wikipedia site after clicking on the white circle next to BestFirstSearch
     * The link is an information about the BestFirstSearch algorithm that can be used to solve the maze
     * @param actionEvent pressing the white circle next to BestFirstSearch
     */
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

