package dto;

public class LoginDTO {
    private String phone;
    private String password;

    public LoginDTO() {}

    public LoginDTO(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // Getters & Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
