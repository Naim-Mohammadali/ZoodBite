package example.org.tst1;

public class Restaurant extends User{
    private String address;

    public Restaurant(String username ,String name, String lastName, String phoneNumber, String email, String password, String address) {
        super(username,name, lastName, phoneNumber, email, password);
        this.address = address;
    }

    public Restaurant(String username ,String name, String lastName, String phoneNumber, String password, String address) {
        super (username ,name, lastName, phoneNumber, password);
        this.address = address;
    }
}
