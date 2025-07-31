package network.dto.order;

public class PlaceOrderItemDto {
    public Long id;
    public Integer quantity;

    public PlaceOrderItemDto() {}

    public PlaceOrderItemDto(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
