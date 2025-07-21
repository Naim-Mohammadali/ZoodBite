package dao;

import model.Coupon;

public interface CouponDAO {
    void save(Coupon c);
    Coupon findByCode(String code);
    void incrementUsage(Coupon c);
}

