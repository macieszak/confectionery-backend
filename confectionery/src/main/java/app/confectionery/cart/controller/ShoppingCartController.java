package app.confectionery.cart.controller;

import app.confectionery.cart.service.ShoppingCartService;
import app.confectionery.cart_item.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
