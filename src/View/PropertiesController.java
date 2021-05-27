package View;

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
import java.io.OutputStream;

public class PropertiesController
{

    public ChoiceBox GeneratingBar;
    public ChoiceBox SolvingBar;
    public ChoiceBox CompressorBar;
    public Button cancelButton;
    public Button confirmButton;
    ObservableList<String> GeneratingBarList = FXCollections.observableArrayList("EmptyMazeGenerator","SimpleMazeGenerator","MyMazeGenerator");
    ObservableList<String> SolvingBarList = FXCollections.observableArrayList("DepthFirstSearch","BreadthFirstSearch","BestFirstSearch");


    @FXML
    private void initialize()
    {
        Object[] properties = Configurations.getInstance().LoadProp();

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

    public void cancelButton(ActionEvent actionEvent) throws IOException
    {
        Main.backToMain();
    }

    public void confirmButton(ActionEvent actionEvent) throws IOException {
        String generator = (String) GeneratingBar.getValue();
        String solver = (String) SolvingBar.getValue();
        Configurations c = Configurations.getInstance();
        c.writeProp("10", generator, solver, "MyCompressorOutputStream");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Properties successfully loaded");
        alert.show();

        Main.backToMain();
    }
}
