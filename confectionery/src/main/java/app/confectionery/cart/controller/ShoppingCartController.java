package app.confectionery.cart.controller;

import app.confectionery.cart.service.ShoppingCartService;
import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.model.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cart/")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add/{userId}/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable UUID userId, @PathVariable Long productId, @PathVariable int quantity) {
        try {
            CartItem cartItem = shoppingCartService.addProductToCart(userId, productId, quantity);
            return ResponseEntity.ok("Product added successfully. New quantity: " + cartItem.getQuantity());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product to cart: " + e.getMessage());
        }
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable UUID userId) {
        try {
            int itemCount = shoppingCartService.getCartItemCount(userId);
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable UUID userId) {
        try {
            List<CartItemDTO> items = shoppingCartService.getCartItems(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/increment/{userId}/{productId}")
    public ResponseEntity<String> incrementProductQuantity(@PathVariable UUID userId, @PathVariable Long productId) {
        try {
            shoppingCartService.incrementQuantity(userId, productId);
            return ResponseEntity.ok("Quantity incremented successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error incrementing quantity: " + e.getMessage());
        }
    }

    @PostMapping("/decrement/{userId}/{productId}")
    public ResponseEntity<String> decrementProductQuantity(@PathVariable UUID userId, @PathVariable Long productId) {
        try {
            shoppingCartService.decrementQuantity(userId, productId);
            return ResponseEntity.ok("Quantity decremented successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error decrementing quantity: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable UUID userId, @PathVariable Long productId) {
        try {
            shoppingCartService.removeCartItem(userId, productId);
            return ResponseEntity.ok("Product removed from cart");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing product from cart: " + e.getMessage());
        }
    }


}
