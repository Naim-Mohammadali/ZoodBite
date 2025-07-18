package model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COURIER")
public class Courier extends User {

    @Column(name = "available", nullable = false)
    private boolean available;

    public Courier() {
        super();
        this.available = true;
    }

    public Courier(String name, String phone, String password, String address) {
        super(Role.COURIER, name, phone, password, address);
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
