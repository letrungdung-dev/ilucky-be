package burundi.ilucky.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_history")
public class PurchaseHistory {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer quantity; 

    @Column(nullable = false)
    private Boolean paymentStatus; 
    
}
