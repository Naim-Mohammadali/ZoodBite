package example.org.tst1;

public class Buyer extends User{
    public Buyer(String username , String name, String lastName, String phoneNumber, String email, String password) {
        super(username, name,lastName, phoneNumber, email, password);
    }

    public Buyer(String username ,String name, String lastName, String phoneNumber, String password) {
        super(username,name, lastName, phoneNumber, password);
    }
}
