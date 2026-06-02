package view;

//interface that all the screen's classes have to implement
public interface ScreenInterface {

    //get view in the class returns the GridPane of that specific screen
    public abstract javafx.scene.layout.GridPane getView();

    //dispose in the class helps cleanup after the screen is changed
    public abstract void dispose();
}
