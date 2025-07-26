package network.dto.order;

import java.util.List;

public class PlaceOrderRequestDto {
    public Long vendor_id;
    public String address;
    public List<PlaceOrderItemDto> items;
    public String coupon;

    public PlaceOrderRequestDto() {}

    public PlaceOrderRequestDto(Long vendor_id, String address, List<PlaceOrderItemDto> items, String couponCode) {
        this.vendor_id = vendor_id;
        this.address = address;
        this.items = items;
        this.coupon = couponCode;
    }
}
