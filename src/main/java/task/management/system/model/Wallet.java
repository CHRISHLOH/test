package task.management.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@OptimisticLocking(type = OptimisticLockType.VERSION)
@Table(name = "wallet", schema = "test")
public class Wallet {

    @Id
    @Column(name = "id", nullable = false)
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
