package burundi.ilucky.service;

import burundi.ilucky.model.PurchaseHistory;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.PurchaseHistoryDTO;
import burundi.ilucky.repository.PurchaseHistoryRepository;
import burundi.ilucky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public PurchaseHistoryDTO createPurchaseHistory(Long userId, Integer quantity, Boolean paymentStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        PurchaseHistory history = PurchaseHistory.builder()
                .user(user)
                .quantity(quantity)
                .paymentStatus(paymentStatus)
                .build();

        history = purchaseHistoryRepository.save(history);
        return convertToDTO(history);
    }
    
    @Transactional(readOnly = true)
    public PurchaseHistoryDTO findById(UUID purchaseHistoryId) {
        PurchaseHistory history = purchaseHistoryRepository.findById(purchaseHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseHistory not found with ID: " + purchaseHistoryId));

        return convertToDTO(history);
    }

    @Transactional
    public PurchaseHistoryDTO updatePaymentStatus(UUID purchaseHistoryId, Boolean newPaymentStatus) {
        PurchaseHistory history = purchaseHistoryRepository.findById(purchaseHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseHistory not found with ID: " + purchaseHistoryId));

        history.setPaymentStatus(newPaymentStatus);
        purchaseHistoryRepository.save(history);
        
        if (newPaymentStatus == true) {
        	User user = history.getUser();
        	user.setTotalPlay(user.getTotalPlay() + history.getQuantity());
        }

        return convertToDTO(history);
    }

    private PurchaseHistoryDTO convertToDTO(PurchaseHistory history) {
        return new PurchaseHistoryDTO(
                history.getId(),
                history.getUser().getId(),
                history.getQuantity(),
                history.getPaymentStatus()
        );
    }
}
