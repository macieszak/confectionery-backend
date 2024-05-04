package app.confectionery.order.controller;


import app.confectionery.order.model.DTO.OrderDetailsDTO;
import app.confectionery.order.model.OrderStatus;
import app.confectionery.order.model.StatusUpdateRequest;
import app.confectionery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderDetailsDTO>> getAllOrders() {
        List<OrderDetailsDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderDetailsDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody StatusUpdateRequest statusUpdate) {
        OrderStatus newStatus = OrderStatus.valueOf(statusUpdate.getNewStatus());
        OrderDetailsDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }


}
