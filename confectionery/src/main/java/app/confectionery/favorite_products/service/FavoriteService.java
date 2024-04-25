package app.confectionery.favorite_products.service;

import app.confectionery.product.model.Product;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {

    void addFavorite(UUID userId, Long favoriteProductId);

    void removeFavorite(UUID userId, Long productId);

    List<Product> getFavoritesByUserId(UUID userId);
}
