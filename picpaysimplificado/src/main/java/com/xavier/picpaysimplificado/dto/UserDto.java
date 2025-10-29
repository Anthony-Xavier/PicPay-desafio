package com.xavier.picpaysimplificado.dto;

import com.xavier.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserDto(String firstName, String lastName, String document, String email, BigDecimal balance, String password, UserType userType) {
}
