package model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COURIER")
public class Courier extends User {

    @Column(name = "available")
    private boolean available = true;

    public Courier() {}

    public Courier(String name, String phone, String password, String address) {
        super(name, phone, password, address);
        this.available = true;
    }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public Role getRole() {
        return Role.COURIER;
    }

}
