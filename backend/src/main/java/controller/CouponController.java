package controller;

import dto.coupon.CouponCreateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.Coupon;
import service.CouponService;

import java.util.Set;

@Path("/coupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CouponController {

    private final CouponService service;
    private final Validator     validator;

    public CouponController() {
        this(new CouponService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public CouponController(CouponService service, Validator validator) {
        this.service   = service;
        this.validator = validator;
    }

    @POST
    @RolesAllowed("admin")
    public void create(CouponCreateDto dto) {
        validate(dto);
        Coupon c = new Coupon();
        c.setCode(dto.code());
        c.setDiscountPercent(dto.discountPercent());
        c.setValidFrom(dto.validFrom());
        c.setValidUntil(dto.validUntil());
        c.setUsageLimit(dto.usageLimit());
        service.create(c);
    }

    @GET
    @Path("/{code}")
    public Coupon getCouponInfo(@PathParam("code") String code) throws Exception {
        return service.findValidCoupon(code);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
