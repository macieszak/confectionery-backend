package app.confectionery.favorite_products.controller;

import app.confectionery.favorite_products.service.FavoriteService;
import app.confectionery.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestParam UUID userId, @RequestParam Long favoriteProductId) {
        favoriteService.addFavorite(userId, favoriteProductId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}/product/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable UUID userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/show/{userId}")
    public ResponseEntity<List<Product>> getFavorites(@PathVariable UUID userId) {
        List<Product> favorites = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(favorites);
    }


}
