package app.confectionery.product.controller;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.ProductRequestDTO;
import app.confectionery.product.service.ProductService;
import app.confectionery.product.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final StorageService storageService;
    private final ProductService productService;

    @PostMapping("/add")
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

}
