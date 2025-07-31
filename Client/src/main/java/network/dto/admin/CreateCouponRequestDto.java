package network.dto.admin;

public class CreateCouponRequestDto {
    public String code;
    public Integer value;           // percentage discount
    public Integer min_price;
    public Integer user_count;
    public String validFrom;        // formatted: yyyy-MM-dd
    public String validUntil;
}
