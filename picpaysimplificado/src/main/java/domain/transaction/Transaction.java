package domain.transaction;

import domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne //1 usario pode ter varias transacoes
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne //1 usario pode ter varias transacoes
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private LocalDateTime timestamp;
}
