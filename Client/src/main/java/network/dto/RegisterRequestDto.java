package network.dto;

public class RegisterRequestDto {
    public String name;
    public String phone;
    public String email;
    public String password;
    public String address;
    public String role;
    public BankInfoDto bank_info;

    public RegisterRequestDto() {}

    public RegisterRequestDto(String name, String phone, String email, String password, String address, String role, BankInfoDto bankInfo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.bank_info = bankInfo;
    }
}
