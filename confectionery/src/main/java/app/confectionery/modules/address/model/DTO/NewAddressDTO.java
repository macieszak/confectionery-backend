package app.confectionery.modules.address.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAddressDTO {

    @NotBlank(message = "Address cannot be empty")
    private String address;
    private UUID userId;

}
