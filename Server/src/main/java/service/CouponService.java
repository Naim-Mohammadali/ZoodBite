package service;

import dao.CouponDAO;
import dao.CouponDAOImpl;
import model.Coupon;

import java.time.LocalDate;
import java.util.List;

public class CouponService {

    private final CouponDAO dao = new CouponDAOImpl();

    public Coupon findValidCoupon(String code) throws Exception {
        Coupon c = dao.findByCode(code);
        if (c == null) throw new Exception("Invalid coupon.");
        if (LocalDate.now().isBefore(c.getValidFrom()) || LocalDate.now().isAfter(c.getValidUntil()))
            throw new Exception("Coupon is expired.");
        if (c.getUsedCount() >= c.getUsageLimit())
            throw new Exception("Coupon usage limit reached.");
        return c;
    }

    public double applyDiscount(Coupon coupon, double total) {
        double discount = total * coupon.getDiscountPercent() / 100.0;
        return total - discount;
    }

    public void incrementUsage(Coupon c) {
        dao.incrementUsage(c);
    }

    public void create(Coupon coupon) {
        dao.save(coupon);
    }

    public List<Coupon> findAll() {
        return dao.findAll();
    }

    public List<Coupon> findValid() {
        return dao.findValid();
    }

    public Coupon findByCode(String code) {
        return dao.findByCode(code);
    }
}
