package view;

import Services.DB;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.event.EventHandler;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;




public class AddRoomScreen implements ScreenInterface{

    static GridPane grid = new GridPane();

    Label title = new Label("Add Room");
    Label floorLabel = new Label("Floor: ");
    Label roomLabel = new Label("Room: ");
    Label capacityLabel = new Label("Capacity: ");
    Label instruction = new Label("Room must be three digits long and on the right floor. \nCapacity must be between 1 and 99 inclusive");

    TextField roomNumber = new TextField();
    TextField capacityField = new TextField();
    ComboBox<String> floorSelectBox = new ComboBox<>();

    Button addRoomButton = new Button("Add Room");
    Button goBackButton = new Button("Go Back");


    public AddRoomScreen(){


        Insets gridPadding = new Insets(20,20,20,20);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(title,0,0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);

        floorSelectBox.getItems().addAll("1", "2", "3");
        floorSelectBox.setPromptText("Choose Floor");
        floorSelectBox.setEditable(true);

        grid.add(floorLabel,0,2);
        grid.add(floorSelectBox,1,2);
        grid.add(roomLabel,0,3);
        grid.add(roomNumber,1,3);
        grid.add(capacityLabel,0,4);
        grid.add(capacityField,1,4);
        grid.add(instruction,0,5);
        GridPane.setHalignment(instruction, HPos.CENTER);

        grid.add(addRoomButton,0,6);
        GridPane.setHalignment(addRoomButton, HPos.RIGHT);
        grid.add(goBackButton,1,6);
        GridPane.setHalignment(goBackButton, HPos.LEFT);

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchEditRoomsScreen();
            }
        });

        addRoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validate(floorSelectBox.getValue(), roomNumber.getText(), capacityField.getText())) {
                    if (checkExistence(roomNumber.getText())){
                        addRoom(floorSelectBox.getValue(), roomNumber.getText(), capacityField.getText());
                        instruction.setText("Room Added!");
                        instruction.setStyle("-fx-text-fill: green;");
                        addRoomButton.setDisable(true);

                    }
                    else{
                        instruction.setText("Room already exists");
                        instruction.setStyle("-fx-text-fill: red;");
                    }
                }
                else{
                    instruction.setStyle("-fx-text-fill: red;");
                }
            }
        });


    }
// check if room entered is valid
    public boolean validate(String floor, String room, String capacity){
        
        if (floor == null || floor.trim().isEmpty()) {
            return false;
        }

        try {
            int selectedRoom = Integer.parseInt(room);
            int selectedCapacity = Integer.parseInt(capacity);
            int selectedFloor = Integer.parseInt(floor);

            if (selectedFloor < 1 || selectedFloor > 3) {
                return false;
            }

            if (selectedRoom < 100 || selectedRoom > 999) {
                return false;
            }

            if (selectedCapacity < 1 || selectedCapacity > 99) {
                return false;
            }

            int floorFromRoomNumber = selectedRoom / 100;
            if (floorFromRoomNumber != selectedFloor) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
// check if room already exists
    public boolean checkExistence(String room){
        try {
            FileInputStream roomsFile = new FileInputStream(DB.path("RoomsData"));
            Scanner fileReader = new Scanner(roomsFile);
            String line =  fileReader.nextLine();

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens[0].equals(room)) {
                    return false;
                }
            }
            fileReader.close();

            return true;


        } catch (IOException e) {
            return false;
        }

    }
// add room to file
    public void addRoom(String floor, String room, String capacity){

        try {
            FileOutputStream roomsFile = new FileOutputStream(DB.path("RoomsData"), true);
            PrintWriter roomsWriter = new PrintWriter(roomsFile);

            String seperator = ",";

            String roomLine = String.join(seperator, room, floor, capacity);
            roomsWriter.println(roomLine);
            roomsWriter.close();



        }catch (IOException e){
        System.out.println(e);
    }

}
// clear screen
    public void dispose(){
        instruction.setText("");
        grid.getChildren().clear();
    }

    public GridPane getView(){
        return grid;
    }

}
