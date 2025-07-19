package dto;

import model.User;

public class UserDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private User.Role role;
    private User.Status status;

    public UserDTO() {}

    public UserDTO(Long id, String name, String phone, String address, User.Role role, User.Status status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }

    public User.Status getStatus() { return status; }
    public void setStatus(User.Status status) { this.status = status; }
}
