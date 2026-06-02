package Services;

import entity.Admin;

import java.io.*;


public class AdminRegisterService {
// delimiter in file
    private static final String seperator = "||";

    public static void registerAdmin(Admin admin){
        if (admin.getUserName() != null && admin.getUserName().contains("||") ){
            throw new IllegalArgumentException("Admin username cannot contain '||'");
        }
        if (admin.getPassword() != null && admin.getPassword().contains("||") ){
            throw new IllegalArgumentException("Admin password cannot contain '||'");
        }
            

        try(FileOutputStream toFile = new FileOutputStream(DB.path("AdminData"), true);
            PrintWriter writer = new PrintWriter(toFile);){

            String adminData = String.join(seperator,
                    admin.getUserName(),
                    admin.getPassword()
            );
            writer.println(adminData);

        } catch (IOException e){
            System.out.println(e);
        }
    }

}
