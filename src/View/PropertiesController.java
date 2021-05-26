package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class PropertiesController
{

    public ChoiceBox GeneratingBar;
    public ChoiceBox SolvingBar;
    public ChoiceBox CompressorBar;
    ObservableList<String> GeneratingBarList = FXCollections.observableArrayList("SimpleMazeGenerator","MyMazeGenerator");

    private void initialize(){
        GeneratingBar.setItems(GeneratingBarList);
    }
}
