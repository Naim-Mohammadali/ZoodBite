package dto;

import model.User;

public class RegisterDTO {
    private String name;
    private String phone;
    private String password;
    private String address;
    private User.Role role;

    public RegisterDTO() {}

    public RegisterDTO(String name, String phone, String password, String address, User.Role role) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
}
