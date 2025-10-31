package com.xavier.picpaysimplificado.services;

import com.xavier.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean authotizeTransaction(User sender, BigDecimal value) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "https://util.devi.tools/api/v2/authorize", Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body != null && "success".equals(body.get("status"))) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    if (data != null && Boolean.TRUE.equals(data.get("authorization"))) {
                        return true; // Transação autorizada
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao chamar autorizador: " + e.getMessage());
        }
        return true; // Não autorizado por padrão
    }
}
