package burundi.ilucky.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import burundi.ilucky.jwt.JwtTokenProvider;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.UserDTO;
import burundi.ilucky.model.dto.UserRankingDTO;
import burundi.ilucky.payload.AuthRequest;
import burundi.ilucky.payload.Response;
import burundi.ilucky.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;

	public User findByUserName(String username) {
		try {
			return userRepository.findByUsername(username.toLowerCase());
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<UserRankingDTO> getTopUsersByStars(int top) {
        List<User> users = userRepository.findTopUsersByTotalStar(PageRequest.of(0, top));
        return users.stream()
                .map(UserRankingDTO::new)
                .collect(Collectors.toList());
    }
	
	public void addDailyFreePlay(User user) {
	    LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
	    
	    if (user.getLastFreePlayDate() == null || user.getLastFreePlayDate().isBefore(today)) {
	        user.setTotalPlay(user.getTotalPlay() + 5);
	        user.setLastFreePlayDate(today);
	        userRepository.save(user);
	    }
	}

	public String validateRegistration(AuthRequest authRequest) {
	    String username = authRequest.getUsername().toLowerCase();
	    if (username == null || username.trim().isEmpty()) {
	        return "Tên đăng nhập không được để trống!";
	    }

	    if (userRepository.findByUsername(username) != null) {
	        return "Tên đăng nhập đã tồn tại!";
	    }

	    return null;
	}

	public String performRegistration(AuthRequest authRequest) {
	    User user = new User();
	    user.setUsername(authRequest.getUsername().toLowerCase());
	    user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
	    user.setAddTime(new Date());
	    user.setLastUpdate(new Date());
	    user.setTotalPlay((long) 0);
	    user.setTotalStar((long) 0);
	    user.setTotalVnd((long) 0);
	    
	    user = userRepository.save(user);
	    return tokenProvider.generateToken(user);
	}
	
}
