package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.DTO.ProductRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findProductsSorted(String sort);

    List<Product> filterProducts(String category, double minPrice, double maxPrice);

    List<Product> searchProductsByName(String query);

    List<Product> findAllProducts();

    Optional<Product> findById(Long id);

    Product addNewProduct(ProductRequestDTO productRequestDTO, FileData fileData);

    Product updateProduct(Long productId, ProductRequestDTO productRequest, FileData fileData);

    void deleteProduct(Long productId);

}
