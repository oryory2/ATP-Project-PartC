package View;

import Model.MyModel;
import Server.Configurations;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.search.ISearchingAlgorithm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;

public class PropertiesController
{

    public ChoiceBox GeneratingBar;
    public ChoiceBox SolvingBar;
    public ChoiceBox ThreadsBar;
    public ChoiceBox Mode;
    public Button cancelButton;
    public Button confirmButton;

    ObservableList<String> ModeList = FXCollections.observableArrayList("Easy","Hard");
    ObservableList<String> ThreadsList = FXCollections.observableArrayList("3","5","10");
    ObservableList<String> GeneratingBarList = FXCollections.observableArrayList("EmptyMazeGenerator","SimpleMazeGenerator","MyMazeGenerator");
    ObservableList<String> SolvingBarList = FXCollections.observableArrayList("DepthFirstSearch","BreadthFirstSearch","BestFirstSearch");


    @FXML
    /**
     * first displaying the Properties window
     * set the current properties to start with
     */
    private void initialize()
    {
        Object[] properties = Configurations.getInstance().LoadProp();

        String mode;
        if(MyModel.easyMode)
            mode = "Easy";
        else
            mode = "Hard";
        Mode.setValue(mode);
        Mode.setItems(ModeList);


        int numOfThreads = (int)properties[0];
        ThreadsBar.setValue(numOfThreads + "");
        ThreadsBar.setItems(ThreadsList);

        IMazeGenerator generator = (IMazeGenerator)properties[1];
        String s1 = generator.getClass().toString();
        s1 = s1.substring(32);

        GeneratingBar.setValue(s1);
        GeneratingBar.setItems(GeneratingBarList);

        ISearchingAlgorithm searcher = (ISearchingAlgorithm)properties[2];
        String s2 = searcher.getClass().toString();
        s2 = s2.substring(24);
        SolvingBar.setValue(s2);
        SolvingBar.setItems(SolvingBarList);
    }

    /**
     * cancel any changes that the user has made
     * go back to the previous properties before the changes(if there were any)
     * and go back to the game
     * @param actionEvent the user pressing the cancel button (ActionEvent)
     * @throw IOException
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }

    /**
     * Confirm that these are the settings the user wants
     * set the mode, number of threads, generating algorithm and solving algorithm accordingly
     * go back to the game and apply these new settings
     * @param actionEvent the user pressing the confirm button (ActionEvent)
     * @throw IOException
     */
    public void confirmButton(ActionEvent actionEvent) throws IOException {
        String  numOfThreads = (String) ThreadsBar.getValue();
        String generator = (String) GeneratingBar.getValue();
        String solver = (String) SolvingBar.getValue();
        String mode = (String) Mode.getValue();

        int modeChanged;

        if(MyModel.easyMode)
            modeChanged = 1;
        else
            modeChanged = 2;
        
        if(mode.equals("Easy"))
        {
            MyModel.easyMode = true;
            if(modeChanged == 2)
                modeChanged = 4;
        }
        else
        {
            MyModel.easyMode = false;
            if(modeChanged == 1)
                modeChanged = 3;
        }

        Configurations c = Configurations.getInstance();
        c.writeProp(numOfThreads, generator, solver, "MyCompressorOutputStream");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(modeChanged == 4)
            alert.setContentText("Properties successfully loaded \nSwitched to Easy Mode");
        else if (modeChanged == 3)
            alert.setContentText("Properties successfully loaded \nSwitched to Hard Mode");
        else
            alert.setContentText("Properties successfully loaded");
        alert.show();

        Main.backToMain();
    }
}
