package app.confectionery.order.repository;

import app.confectionery.cart_item.model.CartItem;
import app.confectionery.order.model.Order;
import app.confectionery.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    long countByUserId(UUID userId);

    List<Order> findByUserId(UUID userId);

    //List<Order> findByItems(List<CartItem> items);

}
