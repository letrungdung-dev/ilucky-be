package burundi.ilucky.controller;

import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.UserDTO;
import burundi.ilucky.model.dto.UserRankingDTO;
import burundi.ilucky.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/info")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUserName(userDetails.getUsername());
            UserDTO userDTO = new UserDTO(user);
            userService.addDailyFreePlay(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            log.error("Error fetching user info", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/top-stars")
    public ResponseEntity<?> getTopUsersByStars(@RequestParam(defaultValue = "10") int top) {
        List<UserRankingDTO> topUsers = userService.getTopUsersByStars(top);
        return ResponseEntity.ok(topUsers);
    }
    
}
