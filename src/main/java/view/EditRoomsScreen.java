package view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;

public class EditRoomsScreen implements ScreenInterface {

    static GridPane grid = new GridPane();

    Label title = new Label("Edit Rooms");

    Button addRoom = new Button("Add new Room");
    Button deleteRoom = new Button("Remove Room");
    Button editRoom = new Button("Edit Room");
    Button goBack = new Button("Go Back");

    private final ComboBox<String> floorSelectBox = new ComboBox<>();

    public EditRoomsScreen() {
        // setup screen
        Insets gridPadding = new Insets(20,20,20,20);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(title,0,0);

        int width = 200;
        int height = 40;

        addRoom.setPrefSize(width, height);
        deleteRoom.setPrefSize(width, height);
        editRoom.setPrefSize(width, height);
        goBack.setPrefSize(width, height);

        grid.add(addRoom, 0, 2);
        grid.add(deleteRoom, 0, 3);
        grid.add(editRoom, 0, 4);
        grid.add(goBack, 0, 6);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(addRoom, HPos.CENTER);
        GridPane.setHalignment(deleteRoom, HPos.CENTER);
        GridPane.setHalignment(editRoom, HPos.CENTER);
        GridPane.setHalignment(goBack, HPos.CENTER);

        addRoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.launchAddRoomScreen();
            }
        });

        editRoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.launchEditARoomScreen();
            }
        });

        deleteRoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.launchRemoveRoomScreen();
            }
        });

        goBack.setOnAction(new  EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.launchAdminDashboardScreen();
            }
        });

    }
    public void dispose(){
        grid.getChildren().clear();
    }

    public GridPane getView(){
        return grid;
    }

}
