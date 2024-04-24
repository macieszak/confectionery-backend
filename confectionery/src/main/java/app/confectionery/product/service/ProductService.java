package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.ProductRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product addNewProduct(ProductRequestDTO productRequestDTO, FileData fileData);

    List<Product> findAllProducts();

    Optional<Product> findById(Long id);

    void deleteProduct(Long productId);

    Product updateProduct(Long productId, ProductRequestDTO productRequest, FileData fileData);

    List<Product> findProductsSorted(String sort);

    List<Product> filterProducts(String category, Double minPrice, Double maxPrice);

    List<Product> findProductsFiltered(String category, double minPrice, double maxPrice);

}
