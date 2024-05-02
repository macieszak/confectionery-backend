package app.confectionery.order.model.DTO;

import app.confectionery.order.model.Order;
import app.confectionery.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDTO.class);

    private Long orderId;
    private String addressDetails;
    private List<ProductDTO> products;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;


    public OrderDTO(Order order) {
        this.orderId = order.getId();
        this.addressDetails = order.getDeliveryAddress().getAddressName();
        this.products = order.getItems().stream()
                .map(cartItem -> new ProductDTO(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().name();
        this.orderDate = order.getOrderDate();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductDTO {
    private Long productId;
    private String name;
    private int quantity;

    public ProductDTO(Product product, int quantity) {
        this.productId = product.getId();
        this.name = product.getName();
        this.quantity = quantity;
    }
}

