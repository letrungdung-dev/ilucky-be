package burundi.ilucky.model.dto;

import burundi.ilucky.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingDTO {
	
    private String username;
    private long totalStar;
    
	public UserRankingDTO(User user) {
		this.username = user.getUsername();
		this.totalStar = user.getTotalStar();
	}
    
}

