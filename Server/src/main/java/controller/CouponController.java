package controller;

import dto.coupon.CouponCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Coupon;
import service.CouponService;

import java.util.List;
import java.util.Set;

@Path("/admin/coupons")
@RolesAllowed("admin")
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
    @Operation(summary = "Create a new coupon")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response create(@Valid CouponCreateDto dto)
    {
        validate(dto);
        Coupon c = new Coupon();
        c.setCode(dto.code());
        c.setDiscountPercent(dto.value());
        c.setValidFrom(dto.validFrom());
        c.setValidUntil(dto.validUntil());
        c.setUsageLimit((int) dto.user_count());
        c.setMin_price(dto.min_price());
        service.create(c);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{code}")
    @RolesAllowed("admin")
    @Operation(summary = "Get coupon details by code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coupon details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found or expired")
    })
    public Coupon getCouponInfo(@PathParam("code") String code) throws Exception {
        return service.findValidCoupon(code);
    }
    @GET
    @Path("/all")
    @RolesAllowed("admin")
    @Operation(summary = "List all coupons")
    public List<Coupon> listAll() {
        return service.findAll();
    }


    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
