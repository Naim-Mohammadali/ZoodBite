package controller;

import dto.favorite.FavoriteActionDto;
import dto.restaurant.RestaurantBriefDto;
import jakarta.validation.*;
import model.*;
import service.FavoriteService;
import service.RestaurantService;
import util.mapper.RestaurantMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FavoriteController {

    private final FavoriteService   favService;
    private final RestaurantService restService;
    private final Validator         validator;

    public FavoriteController() {
        this(new FavoriteService(), new RestaurantService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public FavoriteController(FavoriteService favService,
                              RestaurantService restService,
                              Validator validator) {
        this.favService  = favService;
        this.restService = restService;
        this.validator   = validator;
    }

    public void add(Customer customer, FavoriteActionDto dto) throws Exception {
        validate(dto);
        Restaurant r = restService.findById(dto.restaurantId());
        favService.add(customer, r);
    }

    public void remove(Customer customer, long restaurantId) throws Exception {
        Restaurant r = restService.findById(restaurantId);
        favService.remove(customer, r);
    }

    public List<RestaurantBriefDto> list(Customer customer) {
        return favService.list(customer).stream()
                .map(RestaurantMapper::toBriefDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
