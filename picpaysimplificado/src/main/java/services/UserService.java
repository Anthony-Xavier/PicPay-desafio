package services;

import domain.user.User;
import domain.user.UserType;
import domain.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UserRepository;

import java.math.BigDecimal;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTranscation(User sender, BigDecimal amount) throws Exception {
       if(sender.getUserType() == UserType.MERCHANT) {
           throw new Exception("Usuario do tipo logista nao esta autorizado a realizar transacoes");
       }

       if(sender.getBalance().compareTo(amount) < 0) {
           throw new Exception("Saldo insuficiente");
       }
    }

    public User findByUserId(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new Exception("Usuario nao encontrado"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
