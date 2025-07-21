package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "restaurants")
public class Restaurant implements Serializable {

    /* ---------- identity ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- business fields (spec-compliant) ---------- */
    @Column(nullable = false)
    private String name;                         // was already here

    @Column(nullable = false)
    private String address;                      // was already here

    @Column(unique = true, nullable = false)
    private String phone;                        // was already here

    /* NEW → optional logo stored as base64 */
    @Lob
    @Column(name = "logo_base64")
    private String logoBase64;                   // nullable until seller uploads

    /* NEW → mandatory fees with sane defaults */
    @Column(name = "tax_fee", nullable = false)
    private Integer taxFee = 0;

    @Column(name = "additional_fee", nullable = false)
    private Integer additionalFee = 0;

    /* ---------- status & relations ---------- */
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    /* ---------- enum ---------- */
    public enum Status { PENDING, ACTIVE, BLOCKED }

    /* ---------- constructors ---------- */
    public Restaurant() {}                       // JPA only

    public Restaurant(String name, String address, String phone,
                      Seller seller,
                      String logoBase64,
                      Integer taxFee,
                      Integer additionalFee) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.seller = seller;
        this.logoBase64 = logoBase64;
        if (taxFee != null)        this.taxFee = taxFee;
        if (additionalFee != null) this.additionalFee = additionalFee;
    }

    /* ---------- getters / setters ---------- */

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public String getAddress()        { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone()          { return phone; }
    public void setPhone(String phone){ this.phone = phone; }

    public String getLogoBase64()                     { return logoBase64; }
    public void setLogoBase64(String logoBase64)      { this.logoBase64 = logoBase64; }

    public Integer getTaxFee()                        { return taxFee; }
    public void setTaxFee(Integer taxFee)             { this.taxFee = taxFee; }

    public Integer getAdditionalFee()                 { return additionalFee; }
    public void setAdditionalFee(Integer additionalFee){ this.additionalFee = additionalFee; }

    public Status getStatus()         { return status; }
    public void setStatus(Status s)   { this.status = s; }

    public Seller getSeller()         { return seller; }
    public void setSeller(Seller s)   { this.seller = s; }

    /* ---------- toString ---------- */
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
