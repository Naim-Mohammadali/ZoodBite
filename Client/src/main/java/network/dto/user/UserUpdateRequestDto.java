package network.dto.user;

public class UserUpdateRequestDto {
    public String name;
    public String email;
    public String address;
    public String password;
    public String bankName;
    public String accountNumber;

    public UserUpdateRequestDto() {}

    public UserUpdateRequestDto(String name, String email, String address, String password, String bankName, String accountNumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}
