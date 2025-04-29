package burundi.ilucky.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftDTO {
	
    private String id;
    private String name;
    private long noItem;
    private String type;
    
}
