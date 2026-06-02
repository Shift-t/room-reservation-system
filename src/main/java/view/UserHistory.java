package view;

import Services.GetUserHistoryService;
import entity.Booking;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class UserHistory implements ScreenInterface{

// initializes the instance variables
    private final GetUserHistoryService GetUserHistoryService = new GetUserHistoryService();
    private final GridPane grid = new GridPane();
    private final String username;

    private final Button backButton = new Button("< Return to User Dashboard");


    private final TableView<Booking> bookingHistoryTable = new TableView<>();

    //constructor
    public UserHistory(String user){
        this.username = user;
        setupTable();
        setupGrid();
        setupButtons();
    }

    //sets up the main grid for this screen
    private void setupGrid() {
        Insets gridPadding = new Insets(10,10,10,10);
        grid.setPadding(gridPadding);
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        Label header = new Label("Booking History");
        Label userLabel = new Label("Booking history for " + username);

        grid.add(header,0,0,6,1);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(userLabel,0,2,6,1);
        GridPane.setHalignment(userLabel, HPos.LEFT);
        grid.add(bookingHistoryTable,0,3,6,10);
        grid.add(backButton,0,13,6,1);
        GridPane.setHalignment(backButton, HPos.LEFT);
    }

    //sets up a tableview to display the user history
    private void setupTable(){
        TableColumn<Booking, String> dateCol = new TableColumn<>("Booked Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time Slot");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("TimeSlot"));

        TableColumn<Booking, String> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("Floor"));

        TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("Room"));

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("WordStatus"));

        bookingHistoryTable.getColumns().addAll(dateCol,timeCol,floorCol,roomCol,statusCol);
        bookingHistoryTable.setEditable(false);

        bookingHistoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<Booking> userBookings = GetUserHistoryService.getHistory(username);
        bookingHistoryTable.getItems().setAll(userBookings);
    }

    //sets up all the buttons in the current screen and adds event handlers
    private void setupButtons(){
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ScreenController.launchUserDashboardScreen(username);
            }
        });
    }

    //returns the view for the current screen when called
    public GridPane getView(){
        return grid;
    }

    //helps clear all elements of the current screen once screen is switched
    public void dispose(){
        backButton.setOnAction(null);
        bookingHistoryTable.getItems().clear();
        grid.getChildren().clear();
    }

}
