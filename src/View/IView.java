package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;

import java.io.IOException;

public interface IView
{
    public void setViewModel(MyViewModel viewModel);
    public void generateMaze(ActionEvent actionEvent);
    public void mazeGenerated();
    public void solveMaze(ActionEvent actionEvent);
    public void mazeSolved();
    public void newBar(ActionEvent actionEvent);
    public void loadBar(ActionEvent actionEvent);
    public void propertiesBar(ActionEvent actionEvent) throws IOException;
    public void saveBar(ActionEvent actionEvent) throws IOException;
    public void userGuide(ActionEvent actionEvent) throws IOException;
    public void AppInfo(ActionEvent actionEvent) throws IOException;
    public void exit(ActionEvent actionEvent);
}
