package burundi.ilucky.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import burundi.ilucky.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
    User findByUsername(String username);
    
    @Query("SELECT u FROM User u ORDER BY u.totalStar DESC")
    List<User> findTopUsersByTotalStar(Pageable pageable);
    
}
