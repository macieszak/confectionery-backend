package app.confectionery.modules.cart_item.repository;

import app.confectionery.modules.cart.model.ShoppingCart;
import app.confectionery.modules.cart_item.model.CartItem;
import app.confectionery.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart(ShoppingCart cart);

    List<CartItem> findAllByProduct(Optional<Product> product);

    List<CartItem> findAllByProductAndCartIsNotNull(Optional<Product> product);

}
