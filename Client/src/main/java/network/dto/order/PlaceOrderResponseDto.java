package network.dto.order;

import java.util.List;

// DTO representing the full response after placing an order
public class PlaceOrderResponseDto {
    public Long id;
    public String restaurantName;
    public List<Integer> createdAt; // [year, month, day, hour, minute, second, nanos]
    public String status;
    public String couponCode;
    public Double total;
    public String address;
    public List<Long> itemIds;
}
