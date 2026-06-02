package Services;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestRoomService {

    //creates a room request in the system once the user submits one
    public static void requestRoom(LocalDate date, String timeSlot, int floor, int roomId, String username, String reason) {
        if (reason != null && reason.contains(",")) {
            throw new IllegalArgumentException("Reason cannot contain commas");
        }
        String seperator = ",";

        //Hashmap to link the provided time slots and their shortcodes
        final Map<String, String> times = new LinkedHashMap<>();
        times.put("7:00 AM - 7:50 AM", "7AM");
        times.put("8:00 AM - 8:50 AM", "8AM");
        times.put("9:00 AM - 9:50 AM", "9AM");
        times.put("10:00 AM - 10:50 AM", "10AM");
        times.put("11:00 AM - 11:50 AM", "11AM");
        times.put("12:00 PM - 12:50 PM", "12PM");
        times.put("1:00 PM - 1:50 PM", "1PM");
        times.put("2:00 PM - 2:50 PM", "2PM");
        times.put("3:00 PM - 3:50 PM", "3PM");
        times.put("4:00 PM - 4:50 PM", "4PM");
        times.put("5:00 PM - 5:50 PM", "5PM");
        times.put("6:00 PM - 6:50 PM", "6PM");
        times.put("7:00 PM - 7:50 PM", "7PM");
        times.put("8:00 PM - 8:50 PM", "8PM");
        times.put("9:00 PM - 9:50 PM", "9PM");
        times.put("10:00 PM - 10:50 PM", "10PM");

        //Writes the request to the pending requests file
        try(FileOutputStream toFile = new FileOutputStream(DB.path("PendingRequests"), true);
            PrintWriter writer = new PrintWriter(toFile);){

            String requestData = String.join(seperator,
                    date.toString(),
                    times.get(timeSlot),
                    Integer.toString(floor),
                    Integer.toString(roomId),
                    username,
                    reason,
                    "P"
                    );
            writer.println(requestData);
        } catch (IOException e){
            System.out.println("Error reading pending requests file: " + e);
        }
    }
}
