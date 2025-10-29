package services;

import domain.transaction.Transaction;
import domain.user.User;
import dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repositories.TransactionRepository;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransation(TransactionDto transaction) throws Exception {
        User sender = this.userService.findByUserId(transaction.senderId());
        User receiver = this.userService.findByUserId(transaction.receiverId());

        userService.validateTranscation(sender, transaction.value());

        boolean isAuthorized = authotizeTransaction(sender, transaction.value());
        if(!isAuthorized) {
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
    }

    public boolean authotizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse =restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
           String message = (String) authorizationResponse.getBody().get("message");
           return "Autorizado".equalsIgnoreCase(message);
        }else{
            return false;
        }
    }
}
