package app.confectionery.cart.service;

import app.confectionery.cart.model.ShoppingCart;
import app.confectionery.cart.repository.ShoppingCartRepository;
import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.model.CartItemDTO;
import app.confectionery.cart_item.repository.CartItemRepository;
import app.confectionery.product.model.Product;
import app.confectionery.product.repository.ProductRepository;
import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
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
            cartItem.get().setProduct(product.get());
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

    @Override
    public List<CartItemDTO> getCartItems(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ShoppingCart cart = user.getCart();
        if (cart == null) {
            return new ArrayList<>();
        }
        return cart.getCartItems().stream()
                .map(item -> CartItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .cartItemId(item.getId())
                        .productName(item.getProduct().getName())
                        .image(item.getProduct().getImage())
                        .price(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void incrementQuantity(UUID userId, Long productId) {
        CartItem cartItem = findCartItem(userId, productId);
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
        updateCartTotal(cartItem.getCart());
    }

    @Override
    public void decrementQuantity(UUID userId, Long productId) {
        CartItem cartItem = findCartItem(userId, productId);
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
            updateCartTotal(cartItem.getCart());
        }
    }

    @Override
    public void removeCartItem(UUID userId, Long productId) {
        CartItem cartItem = findCartItem(userId, productId);
        ShoppingCart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        updateCartTotal(cart);
    }

    @Override
    public CartItem findCartItem(UUID userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ShoppingCart cart = user.getCart();
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
    }

    @Override
    public void updateCartTotal(ShoppingCart cart) {
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        logger.debug("Updating cart total: " + totalPrice);
        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(cart.getCartItems().size());

        logger.debug("Cart updated with new total price");
        shoppingCartRepository.save(cart);
    }

}
