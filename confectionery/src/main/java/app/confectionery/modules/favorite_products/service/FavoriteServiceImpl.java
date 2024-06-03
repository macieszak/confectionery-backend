package app.confectionery.modules.favorite_products.service;

import app.confectionery.modules.favorite_products.model.Favorite;
import app.confectionery.modules.favorite_products.repository.FavoriteRepository;
import app.confectionery.modules.product.model.Product;
import app.confectionery.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Override
    public void addFavorite(UUID userId, Long favoriteProductId) {
        if (favoriteRepository.findByUserIdAndProductId(userId, favoriteProductId).isEmpty()) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setProductId(favoriteProductId);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFavorite(UUID userId, Long productId) {
        favoriteRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(favoriteRepository::delete);
    }

    @Override
    public List<Product> getFavoritesByUserId(UUID userId) {
        List<Long> productIds = favoriteRepository.findAllByUserId(userId).stream()
                .map(Favorite::getProductId)
                .collect(Collectors.toList());
        return productRepository.findAllById(productIds);
    }

}
