package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    public Customer() {
        super(); // JPA needs this
    }

    public Customer(String name, String phone, String password, String address) {
        super(model.Role.CUSTOMER, name, phone, password, address);
    }
    @ManyToMany
    @JoinTable(
            name = "customer_favorite",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favorites = new HashSet<>();

    public Set<Restaurant> getFavorites() {
        return favorites;
    }
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();


}
