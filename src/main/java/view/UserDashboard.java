package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import Services.RoomController;
import Services.DB;
import entity.Room;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class UserDashboard implements ScreenInterface {
    //declare instance variables
    private final String currentUsername;
    private final RoomController roomController = new RoomController();
    private final GridPane grid = new GridPane();
    private final StackPane roomsPane = new StackPane();

    private final ComboBox<String> floorSelectBox = new ComboBox<>();
    private final ComboBox<String> timeSelectBox = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();

    private final Label selectedRoomLabel = new Label("Selected Room: ");
    private final TextField reasonField = new TextField();

    private Button selectedButton = null;
    private Room selectedRoom = null;
    private LocalDate selectedDate = null;
    private String selectedTimeSlot = null;

    private final Button displayRoomsButton = new Button("Display Rooms");
    private final Button confirmButton = new Button("Confirm Selection");
    private final Button signOutButton = new Button("Sign Out");
    private final Button viewHistoryButton = new Button("View Previous Requests");


    Label header = new Label("Welcome to Room Booking System");
    Label title = new Label("User Dashboard");
    Label dateLabel = new Label("Enter Date: ");
    Label floorLabel = new Label("Choose Floor: ");
    Label timeLabel = new Label("Choose Time Slot: ");

    Label reasonLabel = new Label("Reason for Request: ");

    private final Map<String, String> timeBoxValues = getSlotsMap();

//constructor
    public UserDashboard(String username) {
        this.currentUsername = username;
        setupButtons();
        setupComboBoxes();
        setupDatePicker();
        setupGridPane();
    }

    //sets up date picker
    private void setupDatePicker(){
        datePicker.setPromptText("Select Date");
        LocalDate today = LocalDate.now();
        LocalDate lastDate = today.plusDays(30);

// limits the selectable dates to a month
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setDisable(date.isBefore(today) || date.isAfter(lastDate));

                if (date.isBefore(today) || date.isAfter(lastDate)) {
                    setStyle("-fx-background-color: #ffc0cb");
                }
            }
        });
    }

    //sets up the multiple choice input boxes
    private void setupComboBoxes(){
        timeSelectBox.setPromptText("Choose Your Time Slot");
        timeSelectBox.getItems().addAll(timeBoxValues.keySet());

        floorSelectBox.getItems().addAll("Floor 1", "Floor 2", "Floor 3");
        floorSelectBox.setPromptText("Choose Floor");
        floorSelectBox.setEditable(false);
    }

    //maps the input of the combo box to smaller time codes
    private Map<String, String> getSlotsMap(){
        Map<String, String> slotsMap = new LinkedHashMap<>();
        slotsMap.put("7:00 AM - 7:50 AM", "7AM");
        slotsMap.put("8:00 AM - 8:50 AM", "8AM");
        slotsMap.put("9:00 AM - 9:50 AM", "9AM");
        slotsMap.put("10:00 AM - 10:50 AM", "10AM");
        slotsMap.put("11:00 AM - 11:50 AM", "11AM");
        slotsMap.put("12:00 PM - 12:50 PM", "12PM");
        slotsMap.put("1:00 PM - 1:50 PM", "1PM");
        slotsMap.put("2:00 PM - 2:50 PM", "2PM");
        slotsMap.put("3:00 PM - 3:50 PM", "3PM");
        slotsMap.put("4:00 PM - 4:50 PM", "4PM");
        slotsMap.put("5:00 PM - 5:50 PM", "5PM");
        slotsMap.put("6:00 PM - 6:50 PM", "6PM");
        slotsMap.put("7:00 PM - 7:50 PM", "7PM");
        slotsMap.put("8:00 PM - 8:50 PM", "8PM");
        slotsMap.put("9:00 PM - 9:50 PM", "9PM");
        slotsMap.put("10:00 PM - 10:50 PM", "10PM");

        return slotsMap;
    }

    //sets up all the buttons and adds event handlers for them
    private void setupButtons(){
        displayRoomsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                displayRooms();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (currentUsername != null && selectedRoom != null && selectedDate != null && selectedTimeSlot != null && !reasonField.getText().trim().isEmpty()) {
                    String reasonText = reasonField.getText();
                    if (reasonText.contains(",")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText(null);
                        alert.setContentText("The request reason cannot contain commas (',').");
                        alert.showAndWait();
                        return;
                    }
                    ScreenController.launchConfirmRequestScreen(currentUsername, selectedRoom, selectedDate, selectedTimeSlot, reasonText);
                }
            }
        });         

        signOutButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ScreenController.launchBaseScreen();
            }
        });

        viewHistoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchUserHistoryScreen(currentUsername);
            }
        });

    }

    //sets up and adds elements to the grid
    private void setupGridPane(){
        Insets gridPadding = new Insets(20,30,20,30);
        grid.setPadding(gridPadding);
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        HBox colorCodeBox = new HBox(25);

        //this is to show the color legend to the user
        Button unavailable = new Button("Unavailable");
        unavailable.setStyle("-fx-background-color: red; -fx-font-weight: bold;");
        Button pending = new Button("Pending Approval");
        pending.setStyle("-fx-background-color: #ffa200; -fx-font-weight: bold;");
        Button available = new Button("Available");
        available.setStyle("-fx-background-color: lightgreen; -fx-font-weight: bold;");
        colorCodeBox.getChildren().addAll(
                unavailable,
                pending,
                available
        );


        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        floorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        reasonLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        selectedRoomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e86de;");
        signOutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        reasonField.setPromptText("E.g., Study, Event Etc.");


        grid.add(header,0,0,6,1);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(title, 0, 1,6,1);
        GridPane.setHalignment(title, HPos.CENTER);
        grid.add(dateLabel, 0, 3,1,1);
        GridPane.setHalignment(dateLabel, HPos.LEFT);
        grid.add(datePicker, 1,3,1,1);
        grid.add(timeLabel, 2, 3,1,1);
        grid.add(timeSelectBox, 4, 3,1,1);
        grid.add(floorLabel, 0, 4,1,1);
        grid.add(floorSelectBox, 1, 4,1,1);
        grid.add(displayRoomsButton, 0, 5,6,1);
        GridPane.setHalignment(displayRoomsButton, HPos.RIGHT);
        grid.add(roomsPane,0,6,6,3);
        grid.add(colorCodeBox,0,11,6,1);
        colorCodeBox.setAlignment(Pos.CENTER);
        grid.add(selectedRoomLabel, 0, 12,1,1);
        grid.add(reasonLabel, 0, 13,1,1);
        grid.add(reasonField, 1, 13,4,2);
        GridPane.setValignment(reasonField, VPos.TOP);
        grid.add(confirmButton, 0, 15,6,1);
        GridPane.setHalignment(confirmButton, HPos.RIGHT);
        grid.add(signOutButton, 0,16,2,1);
        GridPane.setHalignment(signOutButton, HPos.LEFT);
        grid.add(viewHistoryButton, 4,16,2,1);
        GridPane.setHalignment(viewHistoryButton, HPos.RIGHT);
    }

    //clears the style of the previous button when another one is selected
    private void clearPreviousSelection(){
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: lightgreen;");
            selectedButton = null;
            selectedRoom = null;
        }
        selectedRoomLabel.setText("Selected Room: None");
    }

    //displays the roooms grid
    private void displayRooms(){
        String selectedFloor = floorSelectBox.getValue();
        this.selectedTimeSlot = timeSelectBox.getValue();
        String timeSlot = timeBoxValues.get(selectedTimeSlot);
        this.selectedDate = datePicker.getValue();

        if (selectedFloor !=null && timeSlot !=null && selectedDate != null) {
            clearPreviousSelection();
            try {
                int floor = Integer.parseInt(selectedFloor.replace("Floor ", ""));
                roomsPane.getChildren().clear();
                roomsPane.getChildren().add(createRoomsGrid(floor, timeSlot, selectedDate));
            } catch (NumberFormatException e) {
                roomsPane.getChildren().clear();
            }
        }

    }

    //creates the smaller grid of room tiles
    private GridPane createRoomsGrid(int floor, String timeSlot, LocalDate selectedDate) {
        GridPane roomsGrid = new GridPane();
        roomsGrid.setPadding(new Insets(10,10,10,10));
        roomsGrid.setHgap(10);
        roomsGrid.setVgap(10);
        roomsGrid.setAlignment(Pos.CENTER);

        try {
            ArrayList<Room> currentFloorRooms = roomController.checkRooms(selectedDate, timeSlot, floor);
            addRoomButtons(roomsGrid, currentFloorRooms);
        } catch (IOException error) { //fix this error branch
            System.out.println(error);
        }
        return roomsGrid;
    }

    //adds the room buttons to the smaller grid and changes their color
    private void addRoomButtons(GridPane roomsGrid, ArrayList<Room> rooms) {
        int columns = 5;
        int row = 0;
        int column = 0;

        for (Room room : rooms){
            boolean availability = room.getAvailability();
            Button roomButton = new Button("Room " + room.getRoomNumber() + "\n Cap.: " + room.getCapacity());
            roomButton.setPrefSize(80,80);
            roomButton.setWrapText(true);
            if (availability){
                if (checkPendingRooms(room)){
                    roomButton.setStyle("-fx-background-color: #ffa200;");
                    roomButton.setDisable(true);
                    roomButton.setOpacity(1);
                }
                else{
                    roomButton.setStyle("-fx-background-color: lightgreen;");
                    roomButton.setOnAction(e -> {
                        handleRoomSelection(roomButton, room);
                    });
                }
            }
            else{
                roomButton.setStyle("-fx-background-color: red;");
                roomButton.setDisable(true);
                roomButton.setOpacity(1);
            }

            roomsGrid.add(roomButton,column,row);
            column++;
            if (column == columns){
                column = 0;
                row++;
            }
        }
    }

    //checks if a room is already requested by another user
    private boolean checkPendingRooms(Room room){
        try {
            FileInputStream pendingRequestsFile = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRequestsFile);
            String line = fileReader.nextLine(); // Skip header

            Map<String, String> slotsMap = getSlotsMap();


            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 7) continue;
                if (selectedDate.isEqual(LocalDate.parse(tokens[0]))&&
                        slotsMap.get(selectedTimeSlot).equals(tokens[1])&&
                room.getRoomNumber() == Integer.parseInt(tokens[3])&&
                tokens[6].equals("P")){
                    return true;
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading PendingRequests: " + e.getMessage());
        }
        return false;
    }

    //handles the selection of one of the room tiles
    private void handleRoomSelection(Button clickedButton, Room room){
        if (selectedButton != null) selectedButton.setStyle("-fx-background-color: lightgreen;");
        clickedButton.setStyle("-fx-background-color: #26cdff;");
        selectedRoomLabel.setText("Selected Room: " + room.getRoomNumber());
        selectedButton = clickedButton;
        selectedRoom = room;
    }

    // returns the grid view of this screen
    @Override
    public GridPane getView(){
        return grid;
    }

    // helps clear all the elements of this screen when a user switches away from this screen
    @Override
    public void dispose(){
        displayRoomsButton.setOnAction(null);
        confirmButton.setOnAction(null);
        signOutButton.setOnAction(null);
        viewHistoryButton.setOnAction(null);

        if (selectedButton != null) {
            selectedButton.setOnAction(null);
        }

        reasonField.clear();
        selectedRoomLabel.setText("Selected Room: None");

        roomsPane.getChildren().clear();

        selectedRoom = null;
        selectedDate = null;
        selectedTimeSlot = null;

        grid.getChildren().clear();
    }
}
