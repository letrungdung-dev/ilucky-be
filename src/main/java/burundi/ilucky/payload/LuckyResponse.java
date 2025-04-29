package burundi.ilucky.payload;

import burundi.ilucky.model.dto.GiftDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LuckyResponse {
	
    public LuckyResponse(String status, GiftDTO gift) {
        this.status = status;
        this.gift = gift;
    }

    private String status;
    private GiftDTO gift;
    private Long totalPlay;
    
}
