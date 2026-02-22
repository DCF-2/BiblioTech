package com.ifpe.edu.br.BiblioTech.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @OneToOne
    private Emprestimo emprestimo;

    private BigDecimal valor;
    private String status; // Ex: "PENDENTE", "PAGA"
}