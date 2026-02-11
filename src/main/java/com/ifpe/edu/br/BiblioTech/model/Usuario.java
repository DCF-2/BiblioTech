package com.ifpe.edu.br.BiblioTech.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(unique = true)
    private String login; // email ou matrícula
    
    private String senha;
    
    private String cargo; // ALUNO, FUNCIONARIO, ADMIN (TC010)
}
