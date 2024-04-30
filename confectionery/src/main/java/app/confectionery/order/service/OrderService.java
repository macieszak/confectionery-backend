package app.confectionery.order.service;

import app.confectionery.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(UUID userId, Integer addressId, List<Long> cartItemIds);


}
