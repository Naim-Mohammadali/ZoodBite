package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "restaurant_id"})
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private int score; // between 1 and 5

    private String comment;

    private LocalDateTime createdAt;

    public Rating() {}

    public Rating(Customer customer, Restaurant restaurant, int score, String comment) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.score = score;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setScore(int score) {
        if (score < 0.0 || score > 5.0) {
            throw new IllegalArgumentException("Rating score must be between 0.0 and 5.0");
        }
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
