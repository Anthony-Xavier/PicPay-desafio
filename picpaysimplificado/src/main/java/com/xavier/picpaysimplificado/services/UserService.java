package com.xavier.picpaysimplificado.services;

import com.xavier.picpaysimplificado.domain.user.User;
import com.xavier.picpaysimplificado.domain.user.UserType;
import com.xavier.picpaysimplificado.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.xavier.picpaysimplificado.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;


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

    public User createUser(UserDto data){
        User newUser = new User (data);
        this.saveUser(newUser);
        return newUser;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users =  this.userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
