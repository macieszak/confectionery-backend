package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.model.Product;
import app.confectionery.product.model.ProductRequestDTO;

public interface ProductService {

    Product addNewProduct(ProductRequestDTO productRequestDTO, FileData fileData);

}
