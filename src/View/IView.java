package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;

import java.io.IOException;

public interface IView
{
    void setViewModel(MyViewModel viewModel);
    void generateMaze(ActionEvent actionEvent);
    void mazeGenerated();
    void solveMaze(ActionEvent actionEvent);
    void mazeSolved();
    void newBar(ActionEvent actionEvent);
    void loadBar(ActionEvent actionEvent);
    void propertiesBar(ActionEvent actionEvent) throws IOException;
    void saveBar(ActionEvent actionEvent) throws IOException;
    void userGuide(ActionEvent actionEvent) throws IOException;
    void AppInfo(ActionEvent actionEvent) throws IOException;
    void exit(ActionEvent actionEvent);
}
