package app.confectionery.order.service;

import app.confectionery.address.repository.AddressRepository;
import app.confectionery.cart.model.ShoppingCart;
import app.confectionery.cart.repository.ShoppingCartRepository;
import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.model.ItemStatus;
import app.confectionery.cart_item.repository.CartItemRepository;
import app.confectionery.order.model.DTO.OrderDTO;
import app.confectionery.order.model.Order;
import app.confectionery.order.model.OrderStatus;
import app.confectionery.order.repository.OrderRepository;
import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import app.confectionery.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal SHIPPING_COST = new BigDecimal("5.00");

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public List<OrderDTO> getUserOrders(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Order> orders = orderRepository.findAllByUser(user);

        return orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order createOrder(UUID userId, Integer addressId, List<Long> cartItemIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (cartItemIds.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot create order.");
        }

        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        if (cartItems.size() != cartItemIds.size()) {
            throw new IllegalArgumentException("Some products not found");
        }

        BigDecimal totalPrice = calculateTotalPrice(cartItemIds);
        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new IllegalStateException("Insufficient funds to place the order.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(addressRepository.findById(addressId).orElseThrow());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        cartItems.forEach(item -> {
            item.setStatus(ItemStatus.COMPLETED);
            item.setOrder(savedOrder);
            item.setCart(null);
            cartItemRepository.save(item);
        });

        user.setBalance(user.getBalance().subtract(totalPrice));
        userRepository.save(user);
        transactionService.recordTransaction(userId, totalPrice.negate(), "ORDER_PLACED");
        clearShoppingCart(user.getCart());

        return savedOrder;
    }

    private void clearShoppingCart(ShoppingCart cart) {
        cart.setTotalPrice(0.0);
        cart.setTotalItems(0);
        cart.setCartItems(new ArrayList<>());
        shoppingCartRepository.save(cart);
    }

    private BigDecimal calculateTotalPrice(List<Long> cartItemIds) {
        return cartItemRepository.findAllById(cartItemIds).stream()
                .map(item -> BigDecimal.valueOf(item.getUnitPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(SHIPPING_COST);
    }

}
