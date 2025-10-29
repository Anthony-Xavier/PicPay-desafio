package com.xavier.picpaysimplificado.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(BigDecimal value, Long senderId, Long receiverId, LocalDateTime timestamp) {
}
