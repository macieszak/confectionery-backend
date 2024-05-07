package app.confectionery.order.service;

import app.confectionery.order.model.DTO.OrderDTO;
import app.confectionery.order.model.DTO.OrderDetailsDTO;
import app.confectionery.order.model.Order;
import app.confectionery.order.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDTO> getUserOrders(UUID userId);

    List<OrderDetailsDTO> getOrdersByUserId(UUID userId);

    List<OrderDetailsDTO> getAllOrders();

    Order createOrder(UUID userId, Integer addressId, List<Long> cartItemIds);

    OrderDetailsDTO updateOrderStatus(Long orderId, OrderStatus newStatus);

}
