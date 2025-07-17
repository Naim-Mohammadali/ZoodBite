package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    public Admin() {}

    public Admin(String name, String phone, String password) {
        super(name, phone, password, null);
    }
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

}
