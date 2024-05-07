package app.confectionery.cart_item.repository;

import app.confectionery.cart.model.ShoppingCart;
import app.confectionery.cart_item.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(ShoppingCart cart);
}
