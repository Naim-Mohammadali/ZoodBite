package network.dto.user;

public class CreateRestaurantRequestDto {
    public String name;
    public String address;
    public String phone;
    public Integer taxFee;
    public Integer additionalFee;

    public CreateRestaurantRequestDto() {}

    public CreateRestaurantRequestDto(String name, String address, String phone, Integer taxFee, Integer additionalFee) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.taxFee = taxFee;
        this.additionalFee = additionalFee;
    }
}
