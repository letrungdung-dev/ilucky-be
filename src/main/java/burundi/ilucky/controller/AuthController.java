package burundi.ilucky.controller;

import burundi.ilucky.jwt.JwtTokenProvider;
import burundi.ilucky.model.User;
import burundi.ilucky.payload.*;
import burundi.ilucky.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserService userService;

	@PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername().toLowerCase(),
                            authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = userService.findByUserName(authRequest.getUsername());
            String jwt = tokenProvider.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (Exception ex) {
            log.warn("Authentication failed for user {}: {}", authRequest.getUsername(), ex.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "1", "Tên đăng nhập hoặc mật khẩu không chính xác!"));
        }
    }

	@PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest authRequest) {
        String error = userService.validateRegistration(authRequest);
        if (error != null) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "1", error));
        }

        String jwt = userService.performRegistration(authRequest);
        return ResponseEntity.ok(new AuthResponse(jwt, "0", "Đăng ký thành công!"));
    }
	
}
