package Services;

import java.io.FileInputStream;
import java.util.Scanner;
import java.io.IOException;

public class AdminUserValidator {
    public static boolean isValid(String userName) {
        if (userName == null || userName.length() < 1 || userName.length() > 20) {
            return false;
        }
        if (userName.contains("||")){
            return false;
        }

        try (FileInputStream credsFile = new FileInputStream(DB.path("AdminData"));
             Scanner fileReader = new Scanner(credsFile);){
            fileReader.nextLine();

            while (fileReader.hasNext()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                // using escape characters to represent ||
                String[] parts = line.split("\\|\\|");
                if (parts[0].equals(userName)) {
                    // username already exists
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            System.out.print(e);
            return false;
        }
    }
}
