package app.confectionery.order.repository;

import app.confectionery.order.model.DTO.OrderDetailsDTO;
import app.confectionery.order.model.Order;
import app.confectionery.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    long countByUserId(UUID userId);



}
