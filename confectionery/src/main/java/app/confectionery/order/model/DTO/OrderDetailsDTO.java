package app.confectionery.order.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private Long orderId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
}
