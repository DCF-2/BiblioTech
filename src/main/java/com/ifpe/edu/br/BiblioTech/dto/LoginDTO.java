package com.ifpe.edu.br.BiblioTech.dto;

import lombok.Data;

@Data 
// Lombok gera Getters/Setters automaticamente
public class LoginDTO {
    private String login;
    private String senha;
}