package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User {
    public Seller() {}

    public Seller(String name, String phone, String password, String address) {
        super(name, phone, password, address);
    }
    @Override
    public Role getRole() {
        return Role.SELLER;
    }

}
