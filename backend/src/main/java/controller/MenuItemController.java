package controller;

import dto.menuitem.*;
import jakarta.validation.*;
import model.MenuItem;
import model.Restaurant;
import model.Seller;
import service.MenuItemService;
import util.mapper.MenuItemMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuItemController {

    private final MenuItemService service;
    private final Validator       validator;

    public MenuItemController() {
        this(new MenuItemService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public MenuItemController(MenuItemService service, Validator validator) {
        this.service    = service;
        this.validator  = validator;
    }

    public MenuItemResponse add(Seller seller,
                                Restaurant restaurant,
                                MenuItemCreateRequest dto) throws Exception {
        validate(dto);

        MenuItem entity = new MenuItem();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());
        entity.setImgURL(dto.imgURL());
        entity.setCategory(dto.category());

        service.addMenuItem(seller, restaurant, entity);
        return MenuItemMapper.toDto(entity);
    }

    public MenuItemResponse update(Seller seller,
                                   long itemId,
                                   MenuItemUpdateRequest dto) throws Exception {
        validate(dto);

        MenuItem item = service.getById(itemId);

        if (dto.name()        != null) item.setName(dto.name());
        if (dto.description() != null) item.setDescription(dto.description());
        if (dto.price()       != null) item.setPrice(dto.price());
        if (dto.quantity()    != null) item.setQuantity(dto.quantity());
        if (dto.imgURL()      != null) item.setImgURL(dto.imgURL());
        if (dto.category()    != null) item.setCategory(dto.category());

        service.updateMenuItem(seller, item);
        return MenuItemMapper.toDto(item);
    }

    public void delete(Seller seller, long itemId) throws Exception {
        MenuItem item = service.getById(itemId);
        service.deleteMenuItem(seller, item);
    }

    public List<MenuItemResponse> list(Restaurant restaurant) throws Exception {
        return service.getRestaurantMenu(restaurant)
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
