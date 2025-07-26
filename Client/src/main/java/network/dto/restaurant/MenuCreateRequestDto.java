package network.dto.restaurant;

public class MenuCreateRequestDto {
    public String title;

    public MenuCreateRequestDto(String title) {
        this.title = title;
    }

    public MenuCreateRequestDto() {} // needed by Jackson
}
