package burundi.ilucky.repository;

import burundi.ilucky.model.PurchaseHistory;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, UUID> {
}
