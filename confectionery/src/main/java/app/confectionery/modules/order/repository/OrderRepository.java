package app.confectionery.modules.order.repository;

import app.confectionery.modules.order.model.Order;
import app.confectionery.modules.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    List<Order> findByUserId(UUID userId);

    long countByUserId(UUID userId);

}
