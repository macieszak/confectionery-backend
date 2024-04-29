package app.confectionery.cart.service;

import app.confectionery.cart.model.ShoppingCart;
import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.repository.CartItemRepository;
import app.confectionery.product.model.Product;
import app.confectionery.product.repository.ProductRepository;
import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional //ew do usuniecia
    public CartItem addProductToCart(UUID userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ShoppingCart cart = user.getCart();
        if (cart == null) {
            throw new IllegalStateException("User does not have a shopping cart");
        }

        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        Optional<CartItem> cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (cartItem.isEmpty()) {
            cartItem = Optional.of(new CartItem());
            cartItem.get().setProduct(product.get());   // może samo 'product' ???
            cartItem.get().setQuantity(quantity);
            cartItem.get().setUnitPrice(product.get().getPrice());
            cartItem.get().setCart(cart);
            cart.getCartItems().add(cartItem.get());
        } else {
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        }
        updateCartTotal(cart);
        return cartItemRepository.save(cartItem.get());
    }

    @Override
    public int getCartItemCount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ShoppingCart cart = user.getCart();
        if (cart == null) {
            return 0;
        }
        return cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }


    private void updateCartTotal(ShoppingCart cart) {
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(cart.getCartItems().size());
    }


}
