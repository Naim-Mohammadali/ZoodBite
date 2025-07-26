package network.dto.rating;

public class CustomerRatingRequestDto {
    public Long orderId;
    public int score;
    public String comment;

    public CustomerRatingRequestDto(Long orderId, int score, String comment) {
        this.orderId = orderId;
        this.score = score;
        this.comment = comment;
    }
}
