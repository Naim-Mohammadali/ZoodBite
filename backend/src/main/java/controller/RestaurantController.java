package controller;

import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantCreateDto;
import dto.restaurant.RestaurantUpdateDto;
import dto.restaurant.RestaurantResponseDto;
import jakarta.validation.*;
import model.Restaurant;
import model.Seller;
import service.RestaurantService;
import util.mapper.RestaurantMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RestaurantController {


    private final RestaurantService restaurantService;
    private final Validator         validator;

    public RestaurantController() {
        this(new RestaurantService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public RestaurantController(RestaurantService restaurantService,
                                Validator validator) {
        this.restaurantService = restaurantService;
        this.validator         = validator;
    }
    public RestaurantResponseDto register(Seller currentSeller,
                                          RestaurantCreateDto dto) {
        validate(dto);

        Restaurant saved = new Restaurant(
                dto.name(), dto.address(), dto.phone(),
                currentSeller, dto.logoBase64(),
                dto.taxFee(), dto.additionalFee());

        restaurantService.registerRestaurant(saved, currentSeller);
        return RestaurantMapper.toDto(saved);
    }

    public RestaurantResponseDto update(long restaurantId,
                                        Seller currentSeller,
                                        RestaurantUpdateDto dto) throws Exception {
        validate(dto);

        Restaurant r = restaurantService.findByIdAndSeller(restaurantId,
                currentSeller.getId());
        if (r == null)
            throw new IllegalArgumentException("Seller does not own restaurant #" + restaurantId);

        if (dto.name()    != null) r.setName(dto.name());
        if (dto.address() != null) r.setAddress(dto.address());
        if (dto.phone()   != null) r.setPhone(dto.phone());
        if (dto.logoBase64()    != null) r.setLogoBase64(dto.logoBase64());
        if (dto.taxFee()        != null) r.setTaxFee(dto.taxFee());
        if (dto.additionalFee() != null) r.setAdditionalFee(dto.additionalFee());

        restaurantService.updateRestaurant(r);
        return RestaurantMapper.toDto(r);
    }

    public List<RestaurantResponseDto> listMine(Seller currentSeller) {
        return restaurantService.getRestaurantsBySeller(currentSeller)
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponseDto> browse() {
        return restaurantService.listActive()
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    /** View one restaurant by id (regardless of status). */
    public RestaurantResponseDto view(long id) {
        return RestaurantMapper.toDto(restaurantService.findById(id));
    }

    public RestaurantResponseDto approve(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.approveRestaurant(r);      // PENDING → ACTIVE
        return RestaurantMapper.toDto(r);
    }

    public RestaurantResponseDto block(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.blockRestaurant(r);        // ACTIVE  → BLOCKED
        return RestaurantMapper.toDto(r);
    }

    public RestaurantResponseDto unblock(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.unblockRestaurant(r);      // BLOCKED → ACTIVE
        return RestaurantMapper.toDto(r);
    }

    public List<RestaurantBriefDto> search(String kw, String cat,
                                           Double min, Double max) {
        return restaurantService.listActive().stream() // fallback
                .map(RestaurantMapper::toBriefDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
