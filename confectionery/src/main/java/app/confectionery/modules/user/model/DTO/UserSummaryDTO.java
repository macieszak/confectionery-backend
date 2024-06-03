package app.confectionery.modules.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String mail;
    private BigDecimal amountOfMoney;
    private int numberOfOrders;
    private String accountStatus;

}
