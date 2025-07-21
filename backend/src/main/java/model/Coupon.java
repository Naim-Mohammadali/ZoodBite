package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private int discountPercent; // e.g. 10 = 10%
    private boolean active = true;

    @Column(unique = true, nullable = false) public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    private LocalDate validFrom;
    private LocalDate validUntil;
    @Column(nullable = false)
    private int usageLimit;     // max total uses allowed
    private int usedCount = 0;  // incremented per use

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }

    public int getUsageLimit() { return usageLimit; }
    public void setUsageLimit(int usageLimit) { this.usageLimit = usageLimit; }

    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }
}
