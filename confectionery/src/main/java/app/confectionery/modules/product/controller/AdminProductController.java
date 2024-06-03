package app.confectionery.modules.product.controller;

import app.confectionery.modules.product.model.FileData;
import app.confectionery.modules.product.model.Product;
import app.confectionery.modules.product.model.DTO.ProductRequestDTO;
import app.confectionery.modules.product.service.ProductService;
import app.confectionery.modules.product.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final StorageService storageService;
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<?> addProductByAdmin(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) errors.add("Name cannot be empty");
        if (category == null || category.trim().isEmpty()) errors.add("Category cannot be empty");
        if (price == null || price <= 0) errors.add("Price cannot be null or smaller than 0");
        if (description == null || description.trim().isEmpty()) errors.add("Description cannot be empty");
        if (image == null || image.isEmpty()) errors.add("Image cannot be null");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join(", ", errors));
        }

        ProductRequestDTO productRequestDTO = new ProductRequestDTO(name, category, price, description, image);
        Product newProduct;

        try {
            FileData fileData = storageService.uploadImageToFileSystemAndReturnFileData(image);
            newProduct = productService.addNewProduct(productRequestDTO, fileData);
        } catch (IOException e) {
            throw new RuntimeException("Failure in image upload", e);
        }

        return ResponseEntity.ok(newProduct);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestParam("name") String name,
                                           @RequestParam("category") String category,
                                           @RequestParam("price") Double price,
                                           @RequestParam("description") String description,
                                           @RequestParam("image") MultipartFile image) {

        List<String> updateErrors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) updateErrors.add("Name cannot be empty");
        if (category == null || category.trim().isEmpty()) updateErrors.add("Category cannot be empty");
        if (price == null || price <= 0) updateErrors.add("Price cannot be null or smaller than 0");
        if (description == null || description.trim().isEmpty()) updateErrors.add("Description cannot be empty");
        if (image == null || image.isEmpty()) updateErrors.add("Image cannot be null");

        if (!updateErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join(", ", updateErrors));
        }
        ProductRequestDTO productRequestDTO = new ProductRequestDTO(name, category, price, description, image);
        Product newProduct;
        try {
            FileData fileData = storageService.uploadImageToFileSystemAndReturnFileData(image, productId);
            newProduct = productService.updateProduct(productId, productRequestDTO, fileData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(newProduct);
    }

}
