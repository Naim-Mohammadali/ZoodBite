package dao;

import model.Coupon;

import java.util.List;

public interface CouponDAO {
    void save(Coupon c);
    Coupon findByCode(String code);
    void incrementUsage(Coupon c);
    List<Coupon> findAll();
    List<Coupon> findValid();

}

