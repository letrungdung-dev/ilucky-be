package burundi.ilucky.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDTO {
    
    private UUID id;
    private Long userId;
    private Integer quantity;
    private Boolean paymentStatus;
    
}
