package com.xavier.picpaysimplificado.services;

import com.xavier.picpaysimplificado.domain.user.User;
import com.xavier.picpaysimplificado.domain.user.UserType;
import com.xavier.picpaysimplificado.dto.TransactionDto;
import com.xavier.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should create transaction successfully when everything is ok")
    void createTransationCase1() throws Exception {
        User sender = new User(1l, "sender", "teste", "99999999901", "teste@gmail.com",
                "teste123", new BigDecimal(10), UserType.COMMON);

        User reciver = new User(2l, "reciver", "teste", "99999999902", "reciverteste@gmail.com",
                "teste123", new BigDecimal(10), UserType.COMMON);

        when(userService.findByUserId(1l)).thenReturn(sender);
        when(userService.findByUserId(2l)).thenReturn(reciver);

        when(authorizationService.authotizeTransaction(any(), any())).thenReturn(true);

        TransactionDto request = new TransactionDto(new BigDecimal(10), 1l, 2l);
        transactionService.createTransation(request);

        verify(transactionRepository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService,times(1)).saveUser(sender);

        reciver.setBalance(new BigDecimal(20));
        verify(userService,times(1)).saveUser(reciver);

        verify(notificationService,times(1)).sendNotification(sender, "Transacao realizada com sucesso");
        verify(notificationService,times(1)).sendNotification(reciver, "Transacao recebida com sucesso");
    }

    @Test
    @DisplayName("should create transaction is not allowed")
    void createTransationCase2() throws Exception {
        User sender = new User(1l, "sender", "teste", "99999999901", "teste@gmail.com",
                "teste123", new BigDecimal(10), UserType.COMMON);

        User reciver = new User(2l, "reciver", "teste", "99999999902", "reciverteste@gmail.com",
                "teste123", new BigDecimal(10), UserType.COMMON);

        when(userService.findByUserId(1l)).thenReturn(sender);
        when(userService.findByUserId(2l)).thenReturn(reciver);

        when(authorizationService.authotizeTransaction(any(), any())).thenReturn(false);

            Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDto request = new TransactionDto(new BigDecimal(10), 1l, 2l);
            transactionService.createTransation(request);
        });

        Assertions.assertEquals("Transacao nao autorizada", thrown.getMessage());
    }
}