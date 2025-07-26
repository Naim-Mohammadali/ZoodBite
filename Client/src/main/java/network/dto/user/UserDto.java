package network.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    public Long id;
    public String role;
    public String name;
    public String email;
    public String phone;
    public String address;
    public String password;
    @JsonProperty("bankName")
    public String bankName;
    @JsonProperty("accountNumber")
    public String accountNumber;
    public String status;
    public Boolean available;
}
