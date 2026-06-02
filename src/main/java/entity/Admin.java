package entity;

public class Admin{
    String userName;
    String password;

    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
