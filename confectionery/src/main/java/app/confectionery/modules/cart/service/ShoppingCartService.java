package app.confectionery.modules.cart.service;

import app.confectionery.modules.cart.model.ShoppingCart;
import app.confectionery.modules.cart_item.model.CartItem;
import app.confectionery.modules.cart_item.model.DTO.CartItemDTO;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    List<CartItemDTO> getCartItems(UUID userId);

    CartItem addProductToCart(UUID userId, Long productId, int quantity);

    CartItem findCartItem(UUID userId, Long productId);

    int getCartItemCount(UUID userId);

    void incrementQuantity(UUID userId, Long productId);

    void decrementQuantity(UUID userId, Long productId);

    void removeCartItem(UUID userId, Long productId);

    void updateCartTotal(ShoppingCart cart);

}
