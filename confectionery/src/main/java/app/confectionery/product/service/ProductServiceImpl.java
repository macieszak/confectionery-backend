package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.ProductRequestDTO;
import app.confectionery.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProduct(Long productId) {
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
        // Handle image update if necessary
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
