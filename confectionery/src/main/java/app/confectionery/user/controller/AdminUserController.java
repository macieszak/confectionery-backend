package app.confectionery.user.controller;

import app.confectionery.order.model.DTO.OrderDetailsDTO;
import app.confectionery.order.service.OrderService;
import app.confectionery.user.model.UserSummaryDTO;
import app.confectionery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<UserSummaryDTO>> getUserSummaries() {
        List<UserSummaryDTO> userSummaries = userService.getAllUserSummaries();
        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDetailsDTO>> getUserOrders(@PathVariable UUID userId) {
        List<OrderDetailsDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }


}
