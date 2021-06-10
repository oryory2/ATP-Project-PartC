package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import java.io.IOException;


public interface IView
{
    void newBar(ActionEvent actionEvent);
    void loadBar(ActionEvent actionEvent);
    void saveBar(ActionEvent actionEvent) throws IOException;
    void propertiesBar(ActionEvent actionEvent)throws IOException;
    void userGuide(ActionEvent actionEvent) throws IOException;
    void AppInfo(ActionEvent actionEvent) throws IOException;
    void exit(ActionEvent actionEvent);
    void keyPressed(KeyEvent keyEvent) throws IOException;
    void mouseClicked(MouseEvent mouseEvent);
    void scrollMouse(ScrollEvent scrollEvent);
    void ctrlReleased(KeyEvent keyEvent);
    void mouseDragged(MouseEvent mouseEvent) throws IOException;

    void setViewModel(MyViewModel viewModel);
    void generateMaze(ActionEvent actionEvent);
    void solveMaze(ActionEvent actionEvent);
}
