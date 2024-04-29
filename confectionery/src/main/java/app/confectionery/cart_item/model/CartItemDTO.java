package app.confectionery.cart_item.model;

import app.confectionery.product.model.FileData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long productId;
    private Long cartItemId;
    private String productName;
    private FileData image;
    private double price;
    private int quantity;

}
