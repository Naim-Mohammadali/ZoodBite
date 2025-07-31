package network.dto.user;

public class VendorSearchRequestDto {
    private String search;

    public VendorSearchRequestDto() {
    }

    public VendorSearchRequestDto(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
