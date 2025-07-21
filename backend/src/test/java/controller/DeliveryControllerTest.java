package controller;

import dto.delivery.DeliveryStatusPatchRequest;
import dto.order.OrderResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.Courier;
import model.FoodOrder;
import model.MenuItem;
import model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.OrderService;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private OrderService orderService;
    private Validator validator;
    private DeliveryController controller;

    private Courier courier = new Courier("Sam","+9617","pw","Bike");
    private FoodOrder order = new FoodOrder();

    @BeforeEach
    void setUp() {
        validator   = Validation.buildDefaultValidatorFactory().getValidator();
        controller  = new DeliveryController(orderService, validator);

        // --- minimal order wiring ---
        Restaurant rest = new Restaurant();
        rest.setId(1L);

        MenuItem burger = new MenuItem();
        burger.setId(99L);
        burger.setPrice(9.9);
        burger.setRestaurant(rest);

        order.setRestaurant(rest);
        order.setItems(List.of(burger));   // ← prevents getItems() NPE
    }


    @Test
    void list_deliveries() {
        when(orderService.listByCourier(courier, null))
                .thenReturn(List.of(order));

        List<OrderResponse> list = controller.myDeliveries(courier, null);

        assertEquals(1, list.size());
        verify(orderService).listByCourier(courier, null);
    }

    @Test
    void patch_status() throws Exception {
        order.setStatus(FoodOrder.Status.IN_TRANSIT);
        when(orderService.updateStatusByCourier(
                eq(courier), eq(order), eq(FoodOrder.Status.IN_TRANSIT)))
                .thenReturn(order);

        OrderResponse resp = controller.patchStatus(
                courier, order, new DeliveryStatusPatchRequest("IN_TRANSIT"));

        assertEquals("IN_TRANSIT", resp.status());
    }

}
