package com.ifpe.edu.br.BiblioTech.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String isbn;
    
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    
    // Flag para saber se tem reserva (TC009)
    private boolean temReserva; 
}
