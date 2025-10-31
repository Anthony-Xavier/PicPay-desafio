package com.xavier.picpaysimplificado.services;

import com.xavier.picpaysimplificado.domain.transaction.Transaction;
import com.xavier.picpaysimplificado.domain.user.User;
import com.xavier.picpaysimplificado.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.xavier.picpaysimplificado.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransation(TransactionDto transaction) throws Exception {
        User sender = this.userService.findByUserId(transaction.senderId());
        User receiver = this.userService.findByUserId(transaction.receiverId());

        userService.validateTranscation(sender, transaction.value());

        boolean isAuthorized = this.authorizationService.authotizeTransaction(sender, transaction.value());
        if (!isAuthorized) {
            throw new Exception("Transacao nao autorizada");
        }

        Transaction newTrasaction = new Transaction();
        newTrasaction.setAmount(transaction.value());
        newTrasaction.setSender(sender);
        newTrasaction.setReceiver(receiver);
        newTrasaction.setTimestamp(java.time.LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTrasaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transacao realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transacao recebida com sucesso");
        return newTrasaction;
    }





}
