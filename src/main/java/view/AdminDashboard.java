package view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.event.EventHandler;

public class AdminDashboard implements ScreenInterface{

    static GridPane grid = new GridPane();

    Label header = new Label("Admin Portal");

    Button viewActiveRequests = new Button("View Active Requests");
    Button viewRequestHistory  = new Button("View Request History");
    Button editRooms = new Button("Edit Rooms");
    Button addAdmin = new Button("Add Admin");
    Button signOut = new Button("Sign Out");

    public AdminDashboard(){

        // setup layout

        Insets gridPadding = new Insets(20,20,20,20);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(header,0,0);
        GridPane.setHalignment(header, HPos.CENTER);

        int width = 200;
        int height = 40;

        viewActiveRequests.setPrefSize(width, height);
        grid.add(viewActiveRequests,0,2);
        GridPane.setHalignment(viewActiveRequests, HPos.CENTER);

        viewRequestHistory.setPrefSize(width, height);
        grid.add(viewRequestHistory,0,3);
        GridPane.setHalignment(viewRequestHistory, HPos.CENTER);

        editRooms.setPrefSize(width, height);
        grid.add(editRooms,0,4);
        GridPane.setHalignment(editRooms, HPos.CENTER);

        addAdmin.setPrefSize(width, height);
        grid.add(addAdmin,0,5);
        GridPane.setHalignment(addAdmin, HPos.CENTER);

        signOut.setPrefSize(width, height);
        grid.add(signOut,0,7);
        GridPane.setHalignment(signOut, HPos.CENTER);

        viewActiveRequests.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchActiveRequestsScreen(); //Make active requests
            }
        });

        viewRequestHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchRequestHistoryScreen(); //Make request history
            }
        });

        editRooms.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchEditRoomsScreen(); //Make edit rooms
            }
        });

        addAdmin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchAdminRegisterScreen(); //OPTIONAL registration
            }
        });


        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchBaseScreen();

            }
        });

    }


    public void dispose(){
        viewActiveRequests.setOnAction(null);
        viewRequestHistory.setOnAction(null);
        editRooms.setOnAction(null);
        signOut.setOnAction(null);
        grid.getChildren().clear();
        addAdmin.setOnAction(null);
    }

    public GridPane getView(){
        return grid;
    }

}
