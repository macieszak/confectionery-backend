package app.confectionery.modules.order.controller;

import app.confectionery.modules.order.model.DTO.OrderDTO;
import app.confectionery.modules.order.model.DTO.OrderRequest;
import app.confectionery.modules.order.model.Order;
import app.confectionery.modules.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable UUID userId, @RequestBody OrderRequest orderRequest) {
        try {
            if (orderRequest.getAddressId() == null || orderRequest.getCartItemIds().isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid order request data.");
            }

            Order order = orderService.createOrder(
                    userId,
                    orderRequest.getAddressId(),
                    orderRequest.getCartItemIds()
            );
            OrderDTO orderDTO = new OrderDTO(order);

            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getUserOrders(@PathVariable UUID userId) {
        try {
            List<OrderDTO> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving orders: " + e.getMessage());
        }
    }

}
