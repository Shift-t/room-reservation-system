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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class EditARoomScreen implements ScreenInterface {

    static GridPane grid = new GridPane();

    Label title = new Label("Edit Room Capacity");
    Label floorLabel = new Label("Floor: ");
    Label roomLabel = new Label("Room: ");
    Label currentCapacityLabel = new Label("Current Capacity: ");
    Label newCapacityLabel = new Label("New Capacity: ");
    Label instruction = new Label("Select floor and room to edit capacity");
    Label currentCapacityValue = new Label("");

    TextField roomNumber = new TextField();
    TextField newCapacityField = new TextField();
    ComboBox<String> floorSelectBox = new ComboBox<>();

    Button findRoomButton = new Button("Find Room");
    Button updateButton = new Button("Update Capacity");
    Button goBackButton = new Button("Go Back");

    public EditARoomScreen() {
        Insets gridPadding = new Insets(20, 20, 20, 20);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(title, 0, 0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);

        floorSelectBox.getItems().addAll("1", "2", "3");
        floorSelectBox.setPromptText("Choose Floor");

        grid.add(floorLabel, 0, 2);
        grid.add(floorSelectBox, 1, 2);
        grid.add(roomLabel, 0, 3);
        grid.add(roomNumber, 1, 3);
        grid.add(findRoomButton, 0, 4, 2, 1);
        GridPane.setHalignment(findRoomButton, HPos.CENTER);

        grid.add(currentCapacityLabel, 0, 5);
        grid.add(currentCapacityValue, 1, 5);
        grid.add(newCapacityLabel, 0, 6);
        grid.add(newCapacityField, 1, 6);
        grid.add(updateButton, 0, 7);
        grid.add(goBackButton, 1, 7);
        grid.add(instruction, 0, 8, 2, 1);
        GridPane.setHalignment(instruction, HPos.CENTER);

        currentCapacityLabel.setVisible(false);
        currentCapacityValue.setVisible(false);
        newCapacityLabel.setVisible(false);
        newCapacityField.setVisible(false);
        updateButton.setVisible(false);

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchEditRoomsScreen();
            }
        });

        findRoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validate(floorSelectBox.getValue(), roomNumber.getText())) {
                    String capacity = getRoomCapacity(roomNumber.getText());
                    if (capacity != null) {
                        currentCapacityValue.setText(capacity);
                        currentCapacityLabel.setVisible(true);
                        currentCapacityValue.setVisible(true);
                        newCapacityLabel.setVisible(true);
                        newCapacityField.setVisible(true);
                        updateButton.setVisible(true);
                        instruction.setText("Enter new capacity (1-99)");
                    } else {
                        instruction.setText("Room not found!");
                        instruction.setStyle("-fx-text-fill: red;");
                    }
                } else {
                    instruction.setText("Invalid room number");
                    instruction.setStyle("-fx-text-fill: red;");
                }
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validateCapacity(newCapacityField.getText())) {
                    updateRoomCapacity(roomNumber.getText(), newCapacityField.getText());
                    instruction.setText("Capacity updated successfully!");
                    instruction.setStyle("-fx-text-fill: green;");
                    updateButton.setDisable(true);
                } else {
                    instruction.setText("Capacity must be between 1 and 99");
                    instruction.setStyle("-fx-text-fill: red;");
                }
            }
        });
    }

    public boolean validate(String floor, String room) {

        if (floor == null) {
            return false;
        }

        try {
            int selectedFloor = Integer.parseInt(floor);
            int selectedRoom = Integer.parseInt(room);

            if (selectedRoom < 100 || selectedRoom > 999) {
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

    public boolean validateCapacity(String capacity) {
        // must be integer between 1 and 99 inclusive
        try {
            int cap = Integer.parseInt(capacity);
            return cap > 0 && cap < 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getRoomCapacity(String room) {

        try {
            FileInputStream roomsFile = new FileInputStream(DB.path("RoomsData"));
            Scanner fileReader = new Scanner(roomsFile);
            String line = fileReader.nextLine();

            while (fileReader.hasNextLine()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens[0].equals(room)) {
                    fileReader.close();
                    return tokens[2];
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public void updateRoomCapacity(String room, String newCapacity) {
// string builder temp
        String temp = "";
// reads current contents of file into temp
        try {
            FileInputStream roomsFile = new FileInputStream(DB.path("RoomsData"));
            Scanner fileReader = new Scanner(roomsFile);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens[0].equals(room)) {
                    // change desired room's capacity
                    line = tokens[0] + "," + tokens[1] + "," + newCapacity;
                }
                temp = temp + line + "\n";
            }
            fileReader.close();

            // Rewrite the file with updated capacity
            FileOutputStream toFile = new FileOutputStream(DB.path("RoomsData"), false);
            PrintWriter fileWriter = new PrintWriter(toFile);
            fileWriter.print(temp);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void dispose() {
        instruction.setText("");
        grid.getChildren().clear();
    }

    public GridPane getView() {
        return grid;
    }
}
