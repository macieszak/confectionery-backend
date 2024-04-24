package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.ProductRequestDTO;
import app.confectionery.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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



}
