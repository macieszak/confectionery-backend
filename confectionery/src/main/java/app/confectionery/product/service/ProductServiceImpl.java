package app.confectionery.product.service;

import app.confectionery.cart_item.model.CartItem;
import app.confectionery.cart_item.repository.CartItemRepository;
import app.confectionery.favorite_products.model.Favorite;
import app.confectionery.favorite_products.repository.FavoriteRepository;
import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.DTO.ProductRequestDTO;
import app.confectionery.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    @Override
    public Product addNewProduct(ProductRequestDTO productRequestDTO, FileData fileData) {
        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .category(productRequestDTO.getCategory())
                .price(productRequestDTO.getPrice())
                .description(productRequestDTO.getDescription())
                .image(fileData)
                .build();

        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            List<CartItem> cartItems = cartItemRepository.findAllByProduct(product);
            if (cartItems != null) {
                for (CartItem cartItem : cartItems) {
                    cartItem.setProduct(null);
                }
            }

            List<CartItem> cartItemsWithCart =  cartItemRepository.findAllByProductAndCartIsNotNull(product);
            if (cartItemsWithCart != null) {
                cartItemRepository.deleteAllInBatch(cartItemsWithCart);
            }
        }

        List<Favorite> favorites = favoriteRepository.findAllByProductId(productId);
        favoriteRepository.deleteAllInBatch(favorites);

        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequestDTO productRequest, FileData fileData) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());

        product.setImage(fileData);

        return productRepository.save(product);
    }

    public List<Product> findProductsSorted(String sort) {
        Sort sortDirection = Sort.by("price");
        if ("cheapest".equals(sort)) {
            sortDirection = sortDirection.ascending();
        } else if ("expensive".equals(sort)) {
            sortDirection = sortDirection.descending();
        }
        return productRepository.findAll(sortDirection);
    }

    public List<Product> filterProducts(String category, double minPrice, double maxPrice) {
        if (category == null || category.isEmpty()) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
    }

    public List<Product> searchProductsByName(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

}
