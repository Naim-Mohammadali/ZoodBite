package network.dto.user;

public class UpdateUserStatusRequestDto {
    public String status;

    public UpdateUserStatusRequestDto() {}

    public UpdateUserStatusRequestDto(String status) {
        this.status = status;
    }
}
