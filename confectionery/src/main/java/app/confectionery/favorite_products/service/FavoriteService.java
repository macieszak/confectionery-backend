package app.confectionery.favorite_products.service;

import app.confectionery.product.model.Product;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {

    List<Product> getFavoritesByUserId(UUID userId);

    void addFavorite(UUID userId, Long favoriteProductId);

    void removeFavorite(UUID userId, Long productId);

}
