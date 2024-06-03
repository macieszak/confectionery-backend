package app.confectionery.modules.favorite_products.controller;

import app.confectionery.modules.favorite_products.service.FavoriteService;
import app.confectionery.modules.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<?> addFavorite(@PathVariable UUID userId, @RequestParam Long favoriteProductId) {
        favoriteService.addFavorite(userId, favoriteProductId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorites/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable UUID userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<Product>> getFavorites(@PathVariable UUID userId) {
        List<Product> favorites = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(favorites);
    }
}
