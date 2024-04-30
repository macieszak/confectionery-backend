package app.confectionery.order.controller;

import app.confectionery.order.model.DTO.OrderDTO;
import app.confectionery.order.model.DTO.OrderRequest;
import app.confectionery.order.model.Order;
import app.confectionery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            if (orderRequest.getUserId() == null || orderRequest.getAddressId() == null || orderRequest.getCartItemIds().isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid order request data.");
            }

            Order order = orderService.createOrder(
                    orderRequest.getUserId(),
                    orderRequest.getAddressId(),
                    orderRequest.getCartItemIds()
            );
            OrderDTO orderDTO = new OrderDTO(order);

            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }



}
