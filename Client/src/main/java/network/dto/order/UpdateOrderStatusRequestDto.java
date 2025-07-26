package network.dto.order;

public class UpdateOrderStatusRequestDto {
    private String status;

    public UpdateOrderStatusRequestDto() {}
    public UpdateOrderStatusRequestDto(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
