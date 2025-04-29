package burundi.ilucky.payload;

import lombok.Data;

@Data
public class AuthResponse {
	
    private String accessToken;
    private String tokenType = "Bearer";
    private String status = "0";
    private String message = "OK";
    
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public AuthResponse(String accessToken, String status, String message) {
        this.accessToken = accessToken;
        this.status = status;
        this.message = message;
    }
    
}
