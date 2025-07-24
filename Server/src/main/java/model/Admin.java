package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    public Admin() {}

    public Admin(String name, String phone, String password) {
        super(model.Role.ADMIN, name, phone, password, null);
    }
}
