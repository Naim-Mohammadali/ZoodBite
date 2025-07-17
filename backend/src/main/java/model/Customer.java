package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    public Customer() {}

    public Customer(String name, String phone, String password, String address) {
        super(name, phone, password, address);
    }
    @Override
    public Role getRole() {
        return Role.CUSTOMER;
    }

}
