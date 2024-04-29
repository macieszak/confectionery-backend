package app.confectionery.cart.service;

import app.confectionery.cart.model.ShoppingCart;
import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.model.CartItemDTO;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    CartItem addProductToCart(UUID userId, Long productId, int quantity);

    int getCartItemCount(UUID userId);

    List<CartItemDTO> getCartItems(UUID userId);

    void incrementQuantity(UUID userId, Long productId);

    void decrementQuantity(UUID userId, Long productId);

    void removeCartItem(UUID userId, Long productId);

    CartItem findCartItem(UUID userId, Long productId);

    void updateCartTotal(ShoppingCart cart);

}
