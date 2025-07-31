package network.dto.user;

public class LoginRequestDto {
    public String phone;
    public String password;

    public LoginRequestDto() {}

    public LoginRequestDto(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
