package view;

import Services.GetRequestHistoryService;
import entity.Booking;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

public class RequestHistoryScreen implements ScreenInterface {

    //initializing instance variables
    private final GridPane grid = new GridPane();
    private final GetRequestHistoryService requestService = new GetRequestHistoryService();
    private final TableView<Booking> requestTable = new TableView<>();
    private final Button backButton = new Button("< Return to Admin Dashboard");

    //constructor
    public RequestHistoryScreen() {
        setupGrid();
        setupTable();
        setupButtons();
    }

    //sets up the main grid for this screen
    private void setupGrid() {
        Insets gridPadding = new Insets(10, 10, 10, 10);
        grid.setPadding(gridPadding);
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        Label header = new Label("All Requests History");

        grid.add(header, 0, 0, 6, 1);
        grid.add(requestTable, 0, 1, 6, 10);
        grid.add(backButton, 0, 12, 6, 1);
    }

    //sets up a table view for all the previous bookings
    private void setupTable() {
        // Create columns for table
        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time Slot");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        TableColumn<Booking, String> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));

        TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));

        TableColumn<Booking, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("wordStatus"));

        requestTable.getColumns().addAll(dateCol, timeCol, floorCol, roomCol, userCol, statusCol);
        requestTable.setEditable(false);
        requestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<Booking> allRequests = requestService.getRequests();
        requestTable.getItems().setAll(allRequests);
    }

    //sets up the buttons in this screen and adds event handlers to them
    private void setupButtons() {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchAdminDashboardScreen();
            }
        });
    }

    //returns the grid for this screen
    public GridPane getView() {
        return grid;
    }

    //helps clear all elements of this screen before switching to another screen
    public void dispose() {
        backButton.setOnAction(null);
        requestTable.getItems().clear();
        grid.getChildren().clear();
    }
}
