package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "restaurants")
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    public enum Status {
        PENDING, ACTIVE, BLOCKED
    }

    public Restaurant() {} // JPA

    public Restaurant(String name, String address, String phone, Seller seller) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.seller = seller;
        this.status = Status.PENDING;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public Seller getSeller() { return seller; }

    public void setSeller(Seller seller) { this.seller = seller; }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sellerId=" + (seller != null ? seller.getId() : "null") +
                '}';
    }
}
