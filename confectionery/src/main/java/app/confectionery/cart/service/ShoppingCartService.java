package app.confectionery.cart.service;

import app.confectionery.cart_item.model.CartItem;

import java.util.UUID;

public interface ShoppingCartService {

    CartItem addProductToCart(UUID userId, Long productId, int quantity);

    int getCartItemCount(UUID userId);

}
