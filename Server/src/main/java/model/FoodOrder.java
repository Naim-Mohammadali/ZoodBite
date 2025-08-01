package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class FoodOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.PLACED;

    private double total;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
            name = "order_menu_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> items;

    private String couponCode;
    private String comment = null;

    public String getCouponCode() { return couponCode; }

    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getComment() {
        return comment;
    }


    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;


    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public List<Long> getItemIds() {
        List<Long> items = new ArrayList<>();
        for (MenuItem item : getItems()){
            items.add(item.getId());
        }
        return items;
    }


    public enum Status {
        PLACED,
        ACCEPTED,
        REJECTED,
        PREPARING,
        READY_FOR_PICKUP,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public User getCourier() { return courier; }
    public void setCourier(User courier) { this.courier = courier; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}
