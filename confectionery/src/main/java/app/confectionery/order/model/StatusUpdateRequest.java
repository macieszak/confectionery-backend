package app.confectionery.order.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatusUpdateRequest {
    private String newStatus;
}
