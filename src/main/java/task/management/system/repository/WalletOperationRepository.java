package task.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.management.system.model.WalletOperation;

public interface WalletOperationRepository extends JpaRepository<WalletOperation, Long> {
}
