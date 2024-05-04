package app.confectionery.order.service;

import app.confectionery.order.model.DTO.OrderDTO;
import app.confectionery.order.model.DTO.OrderDetailsDTO;
import app.confectionery.order.model.Order;
import app.confectionery.order.model.OrderStatus;
import app.confectionery.order.model.StatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(UUID userId, Integer addressId, List<Long> cartItemIds);

    List<OrderDTO> getUserOrders(UUID userId);

    List<OrderDetailsDTO> getOrdersByUserId(UUID userId);

    List<OrderDetailsDTO> getAllOrders();

    OrderDetailsDTO updateOrderStatus(Long orderId, OrderStatus newStatus);

}
