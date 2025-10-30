package com.xavier.picpaysimplificado.repositories;

import com.xavier.picpaysimplificado.domain.user.User;
import com.xavier.picpaysimplificado.domain.user.UserType;
import com.xavier.picpaysimplificado.dto.UserDto;
import com.xavier.picpaysimplificado.services.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {


    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get user successfully from DB ")
    void findUserByDocumentCase1() {
        String document = "99999999901";
        UserDto data = new UserDto("thoney","teste",document,"anthonney@gmail.com",new BigDecimal(10), "123456", UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from Db not exist")
    void findUserByDocumentCase2() {
        String document = "99999999901";
        Optional<User> result = this.userRepository.findUserByDocument(document);
        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDto data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}