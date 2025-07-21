package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id","restaurant_id"}))
public class Favorite implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Favorite() {}
    public Favorite(Customer c, Restaurant r) { this.customer = c; this.restaurant = r; }

    public Long getId()               { return id; }
    public Customer getCustomer()     { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
}
