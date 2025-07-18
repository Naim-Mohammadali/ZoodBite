package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    public Customer() {
        super(); // JPA needs this
    }

    public Customer(String name, String phone, String password, String address) {
        super(Role.CUSTOMER, name, phone, password, address);
    }
}
